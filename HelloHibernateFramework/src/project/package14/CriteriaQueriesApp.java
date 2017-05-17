package project.package14;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class CriteriaQueriesApp {

	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		CriteriaQueriesApp objCriteriaQueriesApp = new CriteriaQueriesApp();

		/* Add few employee records in database */
		Integer empID1 = objCriteriaQueriesApp.addEmployee("John", "Doe", 1000);
		Integer empID2 = objCriteriaQueriesApp.addEmployee("Kevin", "Smith", 2000);
		Integer empID3 = objCriteriaQueriesApp.addEmployee("Jane", "Doe", 3000);
		Integer empID4 = objCriteriaQueriesApp.addEmployee("John", "Smith", 4000);

		/* List down all the employees */
		objCriteriaQueriesApp.listEmployees();

		/* Print Total employee's count */
		objCriteriaQueriesApp.countEmployee();

		/* Print Toatl salary */
		objCriteriaQueriesApp.totalSalary();
	}

	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, int salary) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(fname, lname, salary);
			employeeID = (Integer) session.save(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return employeeID;
	}

	/* Method to READ all the employees having salary more than 2000 */
	public void listEmployees() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Employee.class);
			// Add restriction.
			cr.add(Restrictions.gt("salary", 2000));
			List<Employee> employees = cr.list();

			for (Employee employee : employees) {
				System.out.print("First Name: " + employee.getFirstName());
				System.out.print("  Last Name: " + employee.getLastName());
				System.out.println("  Salary: " + employee.getSalary());
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to print total number of records */
	public void countEmployee() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Employee.class);

			// To get total row count.
			cr.setProjection(Projections.rowCount());
			List rowCount = cr.list();

			System.out.println("Total Count: " + rowCount.get(0));
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to print sum of salaries */
	public void totalSalary() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Employee.class);

			// To get total salary.
			cr.setProjection(Projections.sum("salary"));
			List totalSalary = cr.list();

			System.out.println("Total Salary: " + totalSalary.get(0));
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

}

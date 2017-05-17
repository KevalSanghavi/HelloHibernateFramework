package project.package17;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class InterceptorsApp {

	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		InterceptorsApp objInterceptorsApp = new InterceptorsApp();

		/* Add few employee records in database */
		Integer empID1 = objInterceptorsApp.addEmployee("John", "Doe", 1000);
		Integer empID2 = objInterceptorsApp.addEmployee("Kevin", "Smith", 2000);
		Integer empID3 = objInterceptorsApp.addEmployee("Jane", "Doe", 3000);

		/* List down all the employees */
		objInterceptorsApp.listEmployees();

		/* Update employee's records */
		objInterceptorsApp.updateEmployee(empID1, 5000);

		/* Delete an employee from the database */
		objInterceptorsApp.deleteEmployee(empID2);

		/* List down new list of the employees */
		objInterceptorsApp.listEmployees();
	}

	/* Method to CREATE an employee in the database */
	public Integer addEmployee(String fname, String lname, int salary) {
		System.out.println("-- In addEmployee() --");
		Session session = factory.withOptions().interceptor(new MyInterceptor()).openSession();
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

	/* Method to READ all the employees */
	public void listEmployees() {
		System.out.println("-- In listEmployees() --");
		Session session = factory.withOptions().interceptor(new MyInterceptor()).openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Employee> employees = session.createQuery("FROM Employee").list();
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

	/* Method to UPDATE salary for an employee */
	public void updateEmployee(Integer EmployeeID, int salary) {
		System.out.println("-- In updateEmployee() --");
		Session session = factory.withOptions().interceptor(new MyInterceptor()).openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			employee.setSalary(salary);
			session.update(employee);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	/* Method to DELETE an employee from the records */
	public void deleteEmployee(Integer EmployeeID) {
		System.out.println("-- In deleteEmployee() --");
		Session session = factory.withOptions().interceptor(new MyInterceptor()).openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Employee employee = (Employee) session.get(Employee.class, EmployeeID);
			session.delete(employee);
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

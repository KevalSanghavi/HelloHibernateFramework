package project.package15;

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class NativeSQLApp {

	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		NativeSQLApp objNativeSQLApp = new NativeSQLApp();

		/* Add few employee records in database */
		Integer empID1 = objNativeSQLApp.addEmployee("John", "Doe", 1000);
		Integer empID2 = objNativeSQLApp.addEmployee("Kevin", "Smith", 2000);
		Integer empID3 = objNativeSQLApp.addEmployee("Jane", "Doe", 3000);
		Integer empID4 = objNativeSQLApp.addEmployee("John", "Smith", 4000);

		/* List down employees and their salary using Scalar Query */
		objNativeSQLApp.listEmployeesScalar();

		/* List down complete employees information using Entity Query */
		objNativeSQLApp.listEmployeesEntity();
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

	/* Method to READ all the employees using Scalar Query */
	public void listEmployeesScalar() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String sql = "SELECT first_name, salary FROM EMPLOYEE";
			SQLQuery query = session.createSQLQuery(sql);
			query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			List data = query.list();

			for (Object object : data) {
				Map row = (Map) object;
				System.out.print("First Name: " + row.get("first_name"));
				System.out.println(", Salary: " + row.get("salary"));
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

	/* Method to READ all the employees using Entity Query */
	public void listEmployeesEntity() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			String sql = "SELECT * FROM EMPLOYEE";
			SQLQuery query = session.createSQLQuery(sql);
			query.addEntity(Employee.class);
			List<Employee> employees = query.list();

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

}

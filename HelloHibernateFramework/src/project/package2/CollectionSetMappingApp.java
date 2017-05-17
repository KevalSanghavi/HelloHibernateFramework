package project.package2;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class CollectionSetMappingApp {

	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		CollectionSetMappingApp objCollectionSetMappingApp = new CollectionSetMappingApp();
		/* Let us have a set of certificates for the first employee */
		HashSet set1 = new HashSet();
		set1.add(new Certificate("MS"));
		set1.add(new Certificate("BE"));
		set1.add(new Certificate("BA"));

		/* Add employee records in the database */
		Integer empID1 = objCollectionSetMappingApp.addEmployee("John", "Doe", 1000, set1);

		/* Another set of certificates for the second employee */
		HashSet set2 = new HashSet();
		set2.add(new Certificate("MBA"));
		set2.add(new Certificate("BS"));

		/* Add another employee record in the database */
		Integer empID2 = objCollectionSetMappingApp.addEmployee("Kevin", "Smith", 2000, set2);

		/* List down all the employees */
		objCollectionSetMappingApp.listEmployees();

		/* Update employee's salary records */
		objCollectionSetMappingApp.updateEmployee(empID1, 5000);

		/* Delete an employee from the database */
		objCollectionSetMappingApp.deleteEmployee(empID2);

		/* List down all the employees */
		objCollectionSetMappingApp.listEmployees();

	}

	/* Method to add an employee record in the database */
	public Integer addEmployee(String fname, String lname, int salary, Set cert) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(fname, lname, salary);
			employee.setCertificates(cert);
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

	/* Method to list all the employees detail */
	public void listEmployees() {
		Session session = factory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List<Employee> employees = session.createQuery("FROM Employee").list();
			for (Employee employee : employees) {
				System.out.print("First Name: " + employee.getFirstName());
				System.out.print("  Last Name: " + employee.getLastName());
				System.out.println("  Salary: " + employee.getSalary());
				Set<Certificate> certificates = employee.getCertificates();
				for (Certificate certificate : certificates) {
					System.out.println("Certificate: " + certificate.getName());
				}
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

	/* Method to update salary for an employee */
	public void updateEmployee(Integer EmployeeID, int salary) {
		Session session = factory.openSession();
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

	/* Method to delete an employee from the records */
	public void deleteEmployee(Integer EmployeeID) {
		Session session = factory.openSession();
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

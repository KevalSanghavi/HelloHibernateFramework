package project.package8;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class ManyToOneMappingApp {

	private static SessionFactory factory;

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}

		ManyToOneMappingApp objManyToOneMappingApp = new ManyToOneMappingApp();

		/* Let us have one address object */
		Address address = objManyToOneMappingApp.addAddress("Marshall Drive", "Huntington", "WV", "25701");

		/* Add employee records in the database */
		Integer empID1 = objManyToOneMappingApp.addEmployee("John", "Doe", 1000, address);

		/* Add another employee record in the database */
		Integer empID2 = objManyToOneMappingApp.addEmployee("Kevin", "Smith", 2000, address);

		/* List down all the employees */
		objManyToOneMappingApp.listEmployees();

		/* Update employee's salary records */
		objManyToOneMappingApp.updateEmployee(empID1, 5000);

		/* Delete an employee from the database */
		objManyToOneMappingApp.deleteEmployee(empID2);

		/* List down all the employees */
		objManyToOneMappingApp.listEmployees();

	}

	/* Method to add an address record in the database */
	public Address addAddress(String street, String city, String state, String zipcode) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer addressID = null;
		Address address = null;
		try {
			tx = session.beginTransaction();
			address = new Address(street, city, state, zipcode);
			addressID = (Integer) session.save(address);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return address;
	}

	/* Method to add an employee record in the database */
	public Integer addEmployee(String fname, String lname, int salary, Address address) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer employeeID = null;
		try {
			tx = session.beginTransaction();
			Employee employee = new Employee(fname, lname, salary, address);
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
				Address add = employee.getAddress();
				System.out.println("Address ");
				System.out.println("\tStreet: " + add.getStreet());
				System.out.println("\tCity: " + add.getCity());
				System.out.println("\tState: " + add.getState());
				System.out.println("\tZipcode: " + add.getZipcode());
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

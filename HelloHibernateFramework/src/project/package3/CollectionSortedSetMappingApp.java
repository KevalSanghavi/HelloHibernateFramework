package project.package3;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class CollectionSortedSetMappingApp {
   private static SessionFactory factory; 
   public static void main(String[] args) {
      try{
         factory = new Configuration().configure().buildSessionFactory();
      }catch (Throwable ex) { 
         System.err.println("Failed to create sessionFactory object." + ex);
         throw new ExceptionInInitializerError(ex); 
      }
      
      CollectionSortedSetMappingApp objCollectionSortedSetMappingApp = new CollectionSortedSetMappingApp();
      /* Let us have a set of certificates for the first employee  */
      TreeSet set1 = new TreeSet();
      set1.add(new Certificate("MS"));
      set1.add(new Certificate("BE"));
      set1.add(new Certificate("BA"));
     
      /* Add employee records in the database */
      Integer empID1 = objCollectionSortedSetMappingApp.addEmployee("John", "Doe", 1000, set1);

      /* Another set of certificates for the second employee  */
      TreeSet set2 = new TreeSet();
      set2.add(new Certificate("MBA"));
      set2.add(new Certificate("BS"));

      /* Add another employee record in the database */
      Integer empID2 = objCollectionSortedSetMappingApp.addEmployee("Kevin", "Smith", 2000, set2);

      /* List down all the employees */
      objCollectionSortedSetMappingApp.listEmployees();

      /* Update employee's salary records */
      objCollectionSortedSetMappingApp.updateEmployee(empID1, 5000);

      /* Delete an employee from the database */
      objCollectionSortedSetMappingApp.deleteEmployee(empID2);

      /* List down all the employees */
      objCollectionSortedSetMappingApp.listEmployees();

   }

   /* Method to add an employee record in the database */
   public Integer addEmployee(String fname, String lname, 
                                      int salary, SortedSet cert){
      Session session = factory.openSession();
      Transaction tx = null;
      Integer employeeID = null;
      try{
         tx = session.beginTransaction();
         Employee employee = new Employee(fname, lname, salary);
         employee.setCertificates(cert);
         employeeID = (Integer) session.save(employee); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
      return employeeID;
   }

   /* Method to list all the employees detail */
   public void listEmployees( ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         List<Employee> employees = session.createQuery("FROM Employee").list(); 
         for (Employee employee : employees){
            System.out.print("First Name: " + employee.getFirstName()); 
            System.out.print("  Last Name: " + employee.getLastName()); 
            System.out.println("  Salary: " + employee.getSalary());
            SortedSet<Certificate> certificates = employee.getCertificates();
            for (Certificate certificate : certificates){
                  System.out.println("Certificate: " + certificate.getName()); 
            }
         }
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
   /* Method to update salary for an employee */
   public void updateEmployee(Integer EmployeeID, int salary ){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Employee employee = 
                    (Employee)session.get(Employee.class, EmployeeID); 
         employee.setSalary( salary );
         session.update(employee);
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
   /* Method to delete an employee from the records */
   public void deleteEmployee(Integer EmployeeID){
      Session session = factory.openSession();
      Transaction tx = null;
      try{
         tx = session.beginTransaction();
         Employee employee = 
                   (Employee)session.get(Employee.class, EmployeeID); 
         session.delete(employee); 
         tx.commit();
      }catch (HibernateException e) {
         if (tx!=null) tx.rollback();
         e.printStackTrace(); 
      }finally {
         session.close(); 
      }
   }
}

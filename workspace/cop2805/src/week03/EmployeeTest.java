/**
 * 
 */
package week03;

//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * @author scottl
 *
 */
public class EmployeeTest extends TestCase
{

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
	}

	/**
	 * Tests the default constructor.
	 * It expects that the defaults for the name are 
	 * "New First", "New Last" and 0.00 for the salary
	 */
	@Test
	public void testEmployeeDefaultConstructor()
	{
		String expectedFirst = "New First";
		String expectedLast = "New Last";
		String expectedSalary = "0.00";
		
		Employee emp = new Employee();
		if( !(expectedFirst.equals(emp.getFirstName()) && 
			expectedLast.equals(emp.getLastName())  && 
			expectedSalary.equals(emp.getFormattedSalary())))
		{
			String msg = String.format("First - name: Expected: %s, actual: %s\n", expectedFirst, emp.getFirstName());
			msg += String.format("Last name - Expected: %s, actual: %s\n", expectedLast, emp.getLastName());
			msg += String.format("Salary - Expected: %s, actual: %s\n", expectedSalary, emp.getFormattedSalary());
			fail("testEmployeeDefaultConstructor doesn't return expected values\n" + msg);
		}
	}
	
	/**
	 * Tests the two parameter constructor.
	 * It expects that the values for the name are 
	 * "Scott", "LaChance" and 0.00 for the salary
	 */
	@Test
	public void testEmployeeConstructor2()
	{
		String expectedFirst = "Scott";
		String expectedLast = "LaChance";
		String expectedSalary = "0.00";
		
		Employee emp = new Employee("Scott", "LaChance");
		if( !(expectedFirst.equals(emp.getFirstName()) && 
			expectedLast.equals(emp.getLastName())  && 
			expectedSalary.equals(emp.getFormattedSalary())))
		{
			String msg = String.format("First - name: Expected: %s, actual: %s\n", expectedFirst, emp.getFirstName());
			msg += String.format("Last name - Expected: %s, actual: %s\n", expectedLast, emp.getLastName());
			msg += String.format("Salary - Expected: %s, actual: %s\n", expectedSalary, emp.getFormattedSalary());
			fail("testEmployeeConstructor2 doesn't return expected values\n" + msg);
		}
	}
	
	/**
	 * Tests the two parameter constructor.
	 * It expects that the values for the name are 
	 * "Scott", "LaChance" and 5,005.65 for the salary
	 */
	@Test
	public void testEmployeeConstructor3()
	{
		String expectedFirst = "Scott";
		String expectedLast = "LaChance";
		String expectedSalary = "5,005.65";
		
		Employee emp = new Employee("Scott", "LaChance", 5005.65);
		if( !(expectedFirst.equals(emp.getFirstName()) && 
			expectedLast.equals(emp.getLastName())  && 
			expectedSalary.equals(emp.getFormattedSalary())))
		{
			String msg = String.format("First - name: Expected: %s, actual: %s\n", expectedFirst, emp.getFirstName());
			msg += String.format("Last name - Expected: %s, actual: %s\n", expectedLast, emp.getLastName());
			msg += String.format("Salary - Expected: %s, actual: %s\n", expectedSalary, emp.getFormattedSalary());
			fail("testEmployeeConstructor3 doesn't return expected values\n" + msg);
		}
	}
	
	/**
	 * Tests that two Employee instances are equal.
	 * Verifies that the equals method is overloaded correctly
	 */
	@Test
	public void testEmployeeEquals()
	{
		Employee emp = new Employee("Scott", "LaChance", 5005.65);
		Employee emp2 = new Employee("Scott", "LaChance", 5005.65);
		assertEquals("testEmployeeEquals failed", emp, emp2);
	}
	
	/**
	 * Tests thetwo Employee instances are not equal.
	 * Verifies that the equals method is overloaded correctly 
	 */
	@Test
	public void testEmployeeNotEquals()
	{
		Employee emp = new Employee("Scott", "LaChance", 5005.65);
		Employee emp2 = new Employee("Jodi", "Boeddeker", 5005.65);
		boolean equal = emp.equals(emp2);
		assertFalse("testEmployeeNotEquals failed", equal);
	}
	
	/**
	 * Tests the display name is correct.
	 * It expects that the values for the name are 
	 * "LaChance, Scott"
	 */
	@Test
	public void testDisplayName()
	{
		String expectedDisplayName = "LaChance, Scott";
		
		Employee emp = new Employee("Scott", "LaChance", 5005.65);
		if( !(expectedDisplayName.equals(emp.getDisplayName())))
		{
			String msg = String.format("Display name - name: Expected: %s, actual: %s\n", expectedDisplayName, emp.getDisplayName());			
			fail("testDisplayName doesn't return expected values\n" + msg);
		}
	}
	

	
	/**
	 * Tests the toString implementation is.
	 * It expects that the values for the name are 
	 * "LaChance, Scott Salary: $5,005.65"
	 */
	@Test
	public void testToString()
	{
		String expected = "LaChance, Scott Salary: $5,005.65";
		
		Employee emp = new Employee("Scott", "LaChance", 5005.65);
		if( !(expected.equals(emp.toString())))
		{
			String msg = String.format("toString - name: Expected: %s, actual: %s\n", expected, emp.toString());			
			fail("testDisplayName doesn't return expected values\n" + msg);
		}
	}
}

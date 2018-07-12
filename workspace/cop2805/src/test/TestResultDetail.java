package test;

/**
 * Defines the test result details interface
 * 
 * @author Scott LaChance
 */
public interface TestResultDetail
{
	/**
	 * Test name
	 * @return test name
	 */
	String getTestName();
	
	/**
	 * Pass/fail for this test
	 * @return true if passed, otherwise false
	 */
	boolean testResult();
	
	/**
	 * Formatted string of results from the test.
	 * May be empty if there were no errors.
	 * @return Formatted results, typically for detailed errors
	 */
	String testDetails();
}

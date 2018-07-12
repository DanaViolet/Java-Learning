package test;

import java.util.List;

/**
 * Test results interface
 * @author Scott LaChance
 */
public interface TestResult
{
	/**
	 * Overall status of test result
	 * @return true if all tests passed, otherwise false
	 */
	boolean passed();
	
	List<TestResultDetail> getTestResultDetails();
	
}

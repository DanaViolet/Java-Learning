package test.javadoc;

import test.AbstractTestResult;

/**
 * Concrete class for the test results
 * 
 * @author Scott LaChance
 */
public class JavadocTestResults extends AbstractTestResult
{
	/**
	 * Default constructor
	 */
	public JavadocTestResults()
	{		
	}

	/**
	 * Set the pass value
	 * Performed by the caller based on their test state
	 * @param passed true if passed, otherwise false
	 */
	protected void setPassed()
	{
		super.setTestResult(true);
	}

	/**
	 * Set tfail value
	 * Performed by the caller based on their test state
	 * @param passed true if passed, otherwise false
	 */
	protected void setFail()
	{
		super.setTestResult(false);
	}

	/**
	 * Set the pass/fail value
	 * Performed by the caller based on their test state
	 * @param passed true if passed, otherwise false
	 */
	protected void setResult(boolean result)
	{
		super.setTestResult(result);
	}

}

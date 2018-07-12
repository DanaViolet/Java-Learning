package test.javadoc;

import test.AbstractTestResultDetails;

/**
 * Concrete class for TestResultDetails
 * 
 * @author Scott LaChance
 */
public class JavadocTestResultDetail extends AbstractTestResultDetails
{
	/**
	 * Constructor
	 * @param testName Test name
	 */
	public JavadocTestResultDetail(String testName)
	{
		super(testName);
	}

}

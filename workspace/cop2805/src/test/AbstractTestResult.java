package test;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTestResult implements TestResult
{

	/**
	 * Default constructor
	 */
	protected AbstractTestResult()
	{		
	}

	public boolean passed()
	{
		// TODO Auto-generated method stub
		return m_passed;
	}

	public List<TestResultDetail> getTestResultDetails()
	{
		// TODO Auto-generated method stub
		return m_detailList;
	}
	
	/**
	 * Helper method
	 * @param detail New TestResultDetail instance to add 
	 */
	public void addTestResultDetail(TestResultDetail detail)
	{
		m_detailList.add(detail);
	}

	/**
	 * Set the pass/fail value
	 * Performed by the caller based on their test state
	 * @param passed true if passed, otherwise false
	 */
	public void setTestResult(boolean passed)
	{
		m_passed = passed;
	}
	
	protected boolean m_passed = false;
	protected List<TestResultDetail> m_detailList = new ArrayList<TestResultDetail>();
}

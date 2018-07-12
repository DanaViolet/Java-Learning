package test;

/**
 * Implements default behavior for a TestResultDetail.
 * Implementations must subclass this
 * 
 * @author Scott LaChance
 */
public abstract class AbstractTestResultDetails implements TestResultDetail
{
	/**
	 * Constructor
	 * @param name Test name
	 */
	protected AbstractTestResultDetails(String name)
	{
		m_testName = name;
		m_details = new StringBuilder();
	}
	
	@Override
	public String getTestName()
	{
		// TODO Auto-generated method stub
		return m_testName;
	}

	@Override
	public boolean testResult()
	{
		// TODO Auto-generated method stub
		return m_testResult;
	}

	@Override
	public String testDetails()
	{
		// TODO Auto-generated method stub
		return m_details.toString();
	}
	
	/**
	 * It is up to the caller to provide formatting like
	 * line returns and custom layout.
	 * This simply appends to the builder.
	 * @param msg Message to append
	 */
	public void addResultDetail(String msg)
	{
		m_details.append(msg);
		
	}
	
	protected String m_testName;
	protected boolean m_testResult = false;
	protected StringBuilder m_details;
	
	//private static String LRLF = System.getProperty("line.separator");

}

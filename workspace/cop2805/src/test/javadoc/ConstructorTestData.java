package test.javadoc;

/**
 * Encapsulates the details of a constructor under test
 * 
 * This is updated at runtime as the test executes. The name, parameters and
 * return type are validated against the source code for correctness and if they
 * match, the method is valid
 * Constructors have no return type so are treated slightly differently than methods
 * 
 * @author Scott LaChance
 */
public class ConstructorTestData
{
	/**
	 * Basic constructor
	 * Assumes a private visibility, void return type and no parameters
	 * 
	 * @param name Meth0d name
	 */
	public ConstructorTestData(String name)
	{
		this(name, 0);
	}

	public ConstructorTestData(String name, int parameterCount)
	{
		this(name, parameterCount, "private");
	}

	
	/**
	 * Parameterized constructor
	 * 
	 * @param methodName
	 *            Method name
	 * @param parameterCount
	 *            number of parameters
	 * @param returnType
	 *            return type
	 */
	public ConstructorTestData(String name, int parameterCount, String visibility)
	{
		this(name, parameterCount, visibility, false);
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param methodName
	 *            Method name
	 * @param parameterCount
	 *            number of parameters
	 * @param returnType
	 *            return type
	 */
	public ConstructorTestData(String name, int parameterCount,  String visibility,
			boolean isValid)
	{
		m_name = name;
		m_parameterCount = parameterCount;
		m_visibility = visibility;
		m_isValid = isValid;
	}
	
	/**
	 * This is updated at runtime as the test executes. The name, parameters and
	 * return type are validated against the source code for correctness and if
	 * they match, the method is valid
	 * 
	 * @return true if valid, otherwise false
	 */
	public boolean isValid()
	{
		return m_isValid;
	}

	/**
	 * Number of expected parameters
	 * @return Expected parameter count
	 */
	public int getParameterCount()
	{
		return m_parameterCount;
	}
	
	/**
	 * Called by the test if the source code method matches the state of this class
	 */
	public void setValid()
	{
		m_isValid = true;
	}

	/**
	 * Called by the test if the source code method matches the state of this class
	 */
	public void setInvalid()
	{
		m_isValid = false;
	}
	
	/**
	 * Expected method name
	 * @return Expected method name
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * Return the expected visibility of the method (public/private/protected)
	 * @return Visibility
	 */
	public String getVisibility()
	{
		return m_visibility;
	}
	
	private String m_name;
	private int m_parameterCount;
	private String m_visibility;
	private boolean m_isValid = false;
}

package test.javadoc;

/**
 * Encapsulates the details of a method
 * 
 * This is updated at runtime as the test executes. The name, parameters and
 * return type are validated against the source code for correctness and if they
 * match, the method is valid
 * 
 * @author Scott LaChance
 *
 */
public class MethodTestData
{
	/**
	 * Basic constructor
	 * Assumes a private visibility, void return type and no parameters
	 * 
	 * @param name Meth0d name
	 */
	public MethodTestData(String name)
	{
		this(name, 0);
	}

	public MethodTestData(String name, int parameterCount)
	{
		this(name, parameterCount, "void", "private");
	}

	public MethodTestData(String name, int parameterCount, String returnType)
	{
		this(name, parameterCount, returnType, "private");
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
	public MethodTestData(String methodName, int parameterCount, String returnType, String visibility)
	{
		this(methodName, parameterCount, returnType, visibility, false);
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
	public MethodTestData(String methodName, int parameterCount, String returnType, String visibility,
			boolean isValid)
	{
		m_methodName = methodName;
		m_parameterCount = parameterCount;
		m_returnType = returnType;
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
	public String getMethodName()
	{
		return m_methodName;
	}


	/**
	 * Expected method return type
	 * @return Expected method return type
	 */
	public String getReturnType()
	{
		return m_returnType;
	}
	
	/**
	 * Return the expected visibility of the method (public/private/protected)
	 * @return Visibility
	 */
	public String getVisibility()
	{
		return m_visibility;
	}

	private String m_methodName;
	private int m_parameterCount;
	private String m_returnType;
	private String m_visibility;
	private boolean m_isValid = false;
}

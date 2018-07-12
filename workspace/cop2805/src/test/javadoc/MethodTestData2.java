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
public class MethodTestData2
{
	/**
	 * Basic constructor
	 * Assumes a private visibility, void return type and no parameters
	 * 
	 * @param name Method name
	 */
	public MethodTestData2(String name)
	{
		this(name, "");
	}
	
	/**
	 * Two parameter constructor
	 * @param name Method name
	 * @param paramTypes concatenated string of the data types
	 */
	public MethodTestData2(String name, String paramTypes)
	{
		this(name, paramTypes, "void", "private");
	}

	/**
	 * three parameter constructor
	 * @param name Method name
	 * @param paramTypes concatenated string of the data types
	 * @param returnType method return type
	 */
	public MethodTestData2(String name, String paramTypes, String returnType)
	{
		this(name, paramTypes, returnType, "private");
	}
	
	/**
	 * Parameterized constructor
	 * 
	 * @param methodName
	 *            Method name
	 * @param parameterCount
	 *            concatenated string of the data types
	 * @param returnType
	 *            return type
	 */
	public MethodTestData2(String methodName, String paramTypes, String returnType, String visibility)
	{
		this(methodName, paramTypes, returnType, visibility, false);
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param methodName
	 *            Method name
	 * @param paramTypes
	 *            concatenated string of the data types
	 * @param returnType
	 *            return type
	 * @param visibility
	 *            visibility keyword
	 * @param isValid true if valid, otherwise false
	 */
	public MethodTestData2(String methodName, String paramTypes, String returnType, String visibility,
			boolean isValid)
	{
		m_methodName = methodName;
		m_parameterTypes = paramTypes;
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
	public String getParameterTypes()
	{
		return m_parameterTypes;
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
	private String m_parameterTypes;
	private String m_returnType;
	private String m_visibility;
	private boolean m_isValid = false;
}

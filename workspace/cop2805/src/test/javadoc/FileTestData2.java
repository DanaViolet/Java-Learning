package test.javadoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the source file information required
 * to evaluate the source file for correctness
 * 
 * @author Scott LaChance
 */
public class FileTestData2
{
	/**
	 * Parameterized constructor
	 * @param packageName Package file belongs to
	 * @param fileName Name of the file. Its path is established during execution
	 */
	public FileTestData2(String packageName, String fileName, List<MethodTestData2> methods)
	{
		this(packageName, fileName, methods, new ArrayList<ConstructorTestData>());
	}
	
	/**
	 * Parameterized constructor
	 * @param packageName Package file belongs to
	 * @param fileName Name of the file. Its path is established during execution
	 */
	public FileTestData2(String packageName, String fileName, List<MethodTestData2> methods, List<ConstructorTestData> constructors)
	{
		m_packageName = packageName;
		m_fileName = fileName;
		m_methods = methods;
		m_constructors = constructors;
		mapMethods();
		mapConstructors();
	}

	/** 
	 * Expected name of the source file
	 * @return Source file name
	 */
	public String getFileName()
	{
		return m_fileName;
	}
	
	/**
	 * Expected package name
	 * @return the package name
	 */
	public String getPackageName()
	{
		return m_packageName;
	}
	
	/** 
	 * Returns the list of expected method attributes/signatures
	 * @return List of expected methods
	 */
	public List<MethodTestData2> getExpectedMethods()
	{
		return m_methods;
	}
	
	/** 
	 * Returns the list of expected constructors attributes/signatures
	 * @return List of expected constructors
	 */
	public List<ConstructorTestData> getExpectedConstructors()
	{
		return m_constructors;
	}
	
	/**
	 * Retrieves a MethodTestData by name
	 * @param methodName The name of the method to locate
	 * @return MethodTestData reference
	 */
	public MethodTestData2 findMethodTestDataByName(String methodName)
	{
		MethodTestData2 testData = m_methodMap.get(methodName);
		
		return testData;
	}
	
	/**
	 * Retrieves a ConstructorTestData by name
	 * @param methodName The name of the method to locate
	 * @return ConstructorTestData reference
	 */
	public ConstructorTestData findConstructorTestDataByName(String name, int parameterCount)
	{
		ConstructorTestData testData = m_constructorMap.get(name + parameterCount);
		
		return testData;
	}
	
	private void mapMethods()
	{
		m_methodMap = new HashMap<String,MethodTestData2>();
		
		for(MethodTestData2 test : m_methods)
		{
			m_methodMap.put(test.getMethodName(), test);
		}
	}
	
	private void mapConstructors()
	{
		m_constructorMap = new HashMap<String,ConstructorTestData>();
		
		for(ConstructorTestData test : m_constructors)
		{
			m_constructorMap.put(test.getName() + test.getParameterCount(), test);
		}
	}
	
	private List<MethodTestData2> m_methods;
	private Map<String, MethodTestData2> m_methodMap;
	
	private List<ConstructorTestData> m_constructors;
	private Map<String, ConstructorTestData> m_constructorMap;
	
	// Required to build the fully qualified path the requested files
	private String m_packageName;
	// Name of the file, its path is established during execution
	private String m_fileName;
}

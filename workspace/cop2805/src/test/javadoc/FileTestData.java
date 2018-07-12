package test.javadoc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Encapsulates the source file information required
 * to evaluate the source file for correctness
 * 
 * @author Scott LaChance
 */
public class FileTestData
{
	/**
	 * Parameterized constructor
	 * @param packageName Package file belongs to
	 * @param fileName Name of the file. Its path is established during execution
	 */
	public FileTestData(String packageName, String fileName, List<MethodTestData> methods)
	{
		m_packageName = packageName;
		m_fileName = fileName;
		m_methods = methods;
		mapMethods();
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
	public List<MethodTestData> getExpectedMethods()
	{
		return m_methods;
	}
	
	/**
	 * Retrieves a MethodTestData by name
	 * @param methodName The name of the method to locate
	 * @return MethodTestData reference
	 */
	public MethodTestData findMethodTestDataByName(String methodName)
	{
		MethodTestData testData = m_map.get(methodName);
		
		return testData;
	}
	
	private void mapMethods()
	{
		m_map = new HashMap<String,MethodTestData>();
		
		for(MethodTestData test : m_methods)
		{
			m_map.put(test.getMethodName(), test);
		}
	}
	
	private List<MethodTestData> m_methods;
	private Map<String, MethodTestData> m_map;
	
	// Required to build the fully qualified path the requested files
	private String m_packageName;
	// Name of the file, its path is established during execution
	private String m_fileName;
}

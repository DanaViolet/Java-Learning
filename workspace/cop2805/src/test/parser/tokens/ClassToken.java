package test.parser.tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassToken
{
	public ClassToken(String className, CommentToken comment)
	{
		m_comment = comment;
		m_className = normalizeClassName(className);
		m_map = new HashMap<String, MethodToken>();
		m_constructorMap = new HashMap<String, ConstructorToken>();
		m_constructors = new ArrayList<ConstructorToken>();
		m_methods = new ArrayList<MethodToken>();
		m_isAbstract = false;
		m_isStatic = false;

		initialize();
	}

	public String getClassName()
	{
		return m_className;
	}

	public String getVisibility()
	{
		return m_visibility;
	}

	public CommentToken getComment()
	{
		return m_comment;
	}

	public List<MethodToken> getMethodTokens()
	{
		return m_methods;
	}

	public List<ConstructorToken> getConstructorTokens()
	{
		return m_constructors;
	}

	public void addMethod(MethodToken cToken)
	{
		m_methods.add(cToken);
		mapMethods();
		mapMethods2();
	}

	public void addConstructor(ConstructorToken cToken)
	{
		m_constructors.add(cToken);
		mapConstructors();
	}

	public void setConstructors(List<ConstructorToken> constructors)
	{
		m_constructors = constructors;
		mapConstructors();
	}

	public void setMethods(List<MethodToken> methods)
	{
		m_methods = methods;
		mapMethods();
	}

	public void setVisibility(String visibility)
	{
		m_visibility = visibility;
	}

	/**
	 * Retrieves a MethodToken by name
	 * 
	 * @param methodName
	 *            The name of the method to locate
	 * @return MethodToken reference
	 */
	public MethodToken findMethodTokenByName(String methodName, int paramCount)
	{
		MethodToken testData = m_map.get(methodName + paramCount);

		return testData;
	}

	/**
	 * Retrieves a MethodToken by name
	 * 
	 * @param methodName
	 *            The name of the method to locate
	 * @return MethodToken reference
	 */
	public MethodToken findMethodTokenByName(String methodName, String paramTypes)
	{
		String key = methodName + paramTypes;
		MethodToken testData = m_map.get(key);

		return testData;
	}

	/**
	 * Retrieves a MethodToken by name
	 * 
	 * @param methodName
	 *            The name of the method to locate
	 * @return MethodToken reference
	 */
	public ConstructorToken findConstructorTokenByName(String methodName,
			int paramCount)
	{
		ConstructorToken testData = m_constructorMap
				.get(methodName + paramCount);

		return testData;
	}

	public void setAbstract()
	{
		m_isAbstract = true;
	}

	public void setStatic()
	{
		m_isStatic = true;
	}

	public boolean isAbstract()
	{
		return m_isAbstract;
	}

	public boolean isStatic()
	{
		return m_isStatic;
	}

	/**
	 * The parser returns the generic portion of the class name MyClass<E>. Need
	 * to strip the <E> portion in order to have the class name match the file
	 * name
	 * 
	 * @param className
	 * @return
	 */
	private String normalizeClassName(String className)
	{
		String normalizedName = className;

		int angleIndex = className.indexOf("<");
		if(angleIndex != -1)
		{
			normalizedName = className.substring(0, angleIndex).trim();
		}

		return normalizedName;
	}

	/**
	 * Maps the methods for easy access The key is the methods name + the
	 * parameter count (constructor0, constructor1, etc). This allows for
	 * handling overloaded methods
	 */
	private void mapMethods()
	{
		m_map.clear(); // reset
		for(MethodToken method : m_methods)
		{
			m_map.put(method.getName() + method.getParameters().size(), method);
		}
	}	
	
	/**
	 * Maps the methods for easy access The key is the methods name + the
	 * parameter Types (constructorint, constructorintint, etc). This allows for
	 * handling overloaded methods with different data types but same number of parameters
	 */
	private void mapMethods2()
	{
		m_map.clear(); // reset
		for(MethodToken method : m_methods)
		{
			m_map.put(method.getName() + method.getParameterTypes(), method);
		}
	}

	/**
	 * Maps the constructors for easy access The key is the constructor name +
	 * the parameter count (constructor0, constructor1, etc). This allows for
	 * handling overloaded constructors
	 */
	private void mapConstructors()
	{
		m_constructorMap.clear(); // reset
		for(ConstructorToken test : m_constructors)
		{
			m_constructorMap.put(test.getName() + test.getParameters().size(),
					test);
		}
	}

	private void initialize()
	{
	}

	@Override
	public String toString()
	{
		String fmt = "%s\nVisibility: %s\nAbstract: %s\nStatic: %s\nComment:\n%s\nConstructors:\n%s\nMethods:\n%s";

		String methodList = "";
		for(MethodToken method : m_methods)
		{
			methodList += method.toString() + System.lineSeparator();
		}

		String constructorList = "";
		for(ConstructorToken constructor : m_constructors)
		{
			constructorList += constructor.toString() + System.lineSeparator();
		}

		String msg = String.format(fmt, m_className, m_visibility, m_isAbstract,
				m_isStatic, m_comment.toString(), constructorList, methodList );
		return msg;
	}

	private String m_className;
	private String m_visibility;
	private boolean m_isAbstract;
	private boolean m_isStatic;
	private CommentToken m_comment;
	private List<ConstructorToken> m_constructors;
	private List<MethodToken> m_methods;
	private Map<String, MethodToken> m_map;
	private Map<String, ConstructorToken> m_constructorMap;
}

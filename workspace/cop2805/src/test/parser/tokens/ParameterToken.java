package test.parser.tokens;

public class ParameterToken
{
	public ParameterToken(String type, String name)
	{
		m_type = type;
		m_name = name;
	}

	public String getName()
	{
		return m_name;
	}

	public String getType()
	{
		return m_type;
	}

	/**
	 * Override the toString
	 * 
	 * @return formatted string
	 */
	@Override
	public String toString()
	{
		return "[" + m_type + " " + m_name + "]";
	}

	private String m_type;
	private String m_name;
}

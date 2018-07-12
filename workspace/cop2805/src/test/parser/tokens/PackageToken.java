package test.parser.tokens;

public class PackageToken
{
	public PackageToken(String packageName)
	{
		m_packageName = packageName;
	}

	public String getPackageName()
	{
		return m_packageName;
	}

	@Override
	public String toString()
	{
		return m_packageName;
	}

	private String m_packageName;
}

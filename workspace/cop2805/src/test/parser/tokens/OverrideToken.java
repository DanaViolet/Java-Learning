package test.parser.tokens;

public class OverrideToken
{
	public OverrideToken()
	{
		this("");
	}

	public OverrideToken(String override)
	{
		m_override = override;
	}

	public String geOverride()
	{
		return m_override;
	}

	private String m_override;
}


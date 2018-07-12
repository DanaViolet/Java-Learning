package test.parser.tokens;

public abstract class Token
{
	protected Token(String name, CommentToken comment, String visiblity)
	{
		m_name = name;
		m_comment = comment;
		m_visibiity = visiblity;
	}

	public String getName()
	{
		return m_name;
	}

	public String getVisibility()
	{
		return m_visibiity;
	}

	public CommentToken getComment()
	{
		return m_comment;
	}

	@Override
	public String toString()
	{
		String fmt = String.format("[%s, Visibility: %s\n%s", m_name,
				m_visibiity, m_comment);
		return fmt;
	}

	private String m_name;
	private CommentToken m_comment;
	private String m_visibiity;
}


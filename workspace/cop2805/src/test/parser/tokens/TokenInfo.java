package test.parser.tokens;

/**
 * Contains the token and the last character processed.
 * This allows for some back tracing when there are no spaces between
 * the token and a special character 
 * @author Scott LaChance
 */
public class TokenInfo
{
	/**
	 * Constructor
	 * @param token The token string
	 * @param last last character processed
	 */
	public TokenInfo(String token, char last)
	{
		this.m_token = token;
		m_lastChar = last;
	}
	
	/**
	 * Getter that returns the token string
	 * @return token string
	 */
	public String getToken()
	{
		return m_token;
	}
	
	/**
	 * Getter that returns the last character processed after the token
	 * @return character after the token.
	 */
	public char getChar()
	{
		return m_lastChar;
	}

	private String m_token;
	private char m_lastChar;
}

package test.parser.tokens;

/**
 * Manages a two character string. When a character is added, the right position
 * shifts to the left and the new character takes the right position Example:
 * 
 * Original op Add r Update: pr
 * 
 * @author Scott LaChance
 */
public class TwoCharString
{
	/**
	 * Default constructor
	 */
	public TwoCharString()
	{
		m_chars = new char[2];
		m_length = 0;
	}

	/**
	 * Adds a character to the class.
	 * The array is shifted left to accommodate the new 
	 * character and the first character is simply dropped
	 * 
	 * @param ch new character to add
	 */
	public void addChar(char ch)
	{
		switch(m_length)
		{
			case 0:
				m_chars[0] = ch;
				m_length = 1;
				break;
			case 1:
				m_chars[1] = ch;
				m_length = 2;
				break;
			case 2:
				m_chars[0] = m_chars[1];
				m_chars[1] = ch;
				break;
		}
	}

	/**
	 * Returns the string respresentation of the two character array
	 */
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		return new String(m_chars);
	}

	private char[] m_chars;
	private int m_length = 0;

}

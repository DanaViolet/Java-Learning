package test.parser.tokens;

import java.io.CharArrayReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Javadoc comment
 * 
 * @author Scott LaChance
 */
public class CommentToken
{
	/**
	 * Default constructor
	 * 
	 * @param javadoc The full raw content of the javadoc
	 */
	public CommentToken(String javadoc)
	{
		m_javadoc = javadoc;
		m_hasAuthor = false;
		m_hasReturn = false;
		m_return = "";
		m_params = new ArrayList<ParameterInfo>();
		initialize();
	}

	/**
	 * Returns the full raw content of the Javadoc
	 * @return raw javadoc
	 */
	public String getJavadoc()
	{
		return m_javadoc;
	}

	/**
	 * Returns true if @return is present
	 * @return true if @return present, otherwise false
	 */
	public boolean hasReturn()
	{
		return m_hasReturn;
	}

	/**
	 * Returns true if @author is present
	 * @return true if @author present, otherwise false
	 */
	public boolean hasAuthor()
	{
		return m_hasAuthor;
	}


	/**
	 * Returns the text associated with @author
	 * @return @author text
	 */
	public String getAuthor()
	{
		return m_author;
	}

	/**
	 * Returns the text associated with @return
	 * @return @return text
	 */
	public String getReturn()
	{
		return m_return;
	}

	/**
	 * Returns list of @param in the javadoc
	 * @return List of @param names
	 */
	public List<ParameterInfo> getParameters()
	{
		return m_params;
	}

	/**
	 * Number of @params
	 * @return count of the @param 
	 */
	public int getParameterCount()
	{
		return m_params.size();
	}
	
	/**
	 * Returns the comment description 
	 * 
	 * @return The comment description
	 */ 
	public String getDescription()
	{
		return m_description;
	}

	@Override
	public String toString()
	{
		boolean hasParams = m_params.size() > 0;
		String fmt = String.format(
				"@author: %s - %s\n@return %s - %s\n@params: %s\n", m_hasAuthor,
				m_author, m_hasReturn, m_return, hasParams);
		String paramList = "";
		for(ParameterInfo param : m_params)
		{
			m_return += param.m_name + " " + param.m_description + System.lineSeparator();
		}
		return fmt + paramList;
	}

	private void initialize()
	{
		char[] content = m_javadoc.toCharArray();
		CharArrayReader reader = new CharArrayReader(content);
		try
		{
			scan(reader);
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int authorIndex = m_javadoc.indexOf("@author");
		if(authorIndex != -1)
		{
			// has author get Name
			m_hasAuthor = true;
			int eolIndex = m_javadoc.indexOf("\n", authorIndex);
			if(authorIndex + "@author".length() < eolIndex)
			{
				m_author = m_javadoc
						.substring(authorIndex + "@author".length(), eolIndex)
						.trim();
			}
		}

		// get the @param
		int paramIndex = 0;
		paramIndex = m_javadoc.indexOf("@param", paramIndex);

		// @return
		int returnIndex = m_javadoc.indexOf("@return");
		if(returnIndex != -1)
		{
			// has author get Name
			m_hasReturn = true;
			int eolIndex = m_javadoc.indexOf("\n", returnIndex);
			if(authorIndex + "@return".length() < eolIndex)
			{
				m_return = m_javadoc
						.substring(authorIndex + "@return".length(), eolIndex)
						.trim();
			}
		}
	}
	
	private void scan(Reader reader) throws IOException
	{
		String curToken = "";
		String retToken = "";
		TokenInfo tokenInfo = null;
		
		boolean fDone = false;
				
		while(!fDone)
		{
			tokenInfo = getToken(reader);
			curToken = tokenInfo.getToken();
			retToken = processToken(curToken, reader);
			if( retToken.equals("*/") ||
				retToken.equals(""))
			{
				fDone = true;
			}
			else if(retToken.equals("@param"))
			{
				retToken = processAtParam(retToken, reader);
			}
		}
	}

	private String processToken(String curToken, Reader reader) throws IOException
	{
		String workingToken = curToken;
		
		if(curToken.equals("/**"))
		{
			workingToken = processCommentDescription(reader);
			workingToken = processToken(workingToken, reader);
		}
		else if(workingToken.equals("@param"))
		{
			workingToken = processAtParam(workingToken, reader);
			workingToken = processToken(workingToken, reader);
		}
		else if(workingToken.equals("@return"))
		{
			workingToken = processAtReturn(workingToken, reader);
			workingToken = processToken(workingToken, reader);
		}
		else if(workingToken.equals("@author"))
		{
			workingToken = processAtAuthor(workingToken, reader);
			workingToken = processToken(workingToken, reader);
		}
		
		return workingToken;
	}
	
	private String processAtAuthor(String curToken, Reader reader) throws IOException
	{
		boolean fDone = false;
		StringBuilder comment = new StringBuilder();
		String token = "";
		TokenInfo tokenInfo = null;
		while(!fDone)
		{
			tokenInfo = getToken(reader);
			token = tokenInfo.getToken();
			
			if(token.equals("@param") ||
			   token.equals("@return") ||
			   token.equals("@exception") ||
			   token.equals("@author" ) ||
			   token.equals("*/" ))
			{
				// end of getting comment description
				fDone = true;
			}
			else if(token.equals("*") ||
					token.equals("") )
			{
				// eat it
			}
			else
			{
				comment.append(token);
				comment.append(" "); // separate tokens
			}
		}
		
		m_author = comment.toString().trim();
		m_hasAuthor = true;
		
		return token;
	}

	private String processCommentDescription(Reader reader) throws IOException
	{
		boolean fDone = false;
		StringBuilder comment = new StringBuilder();
		String token = "";
		TokenInfo tokenInfo = null;
		while(!fDone)
		{
			tokenInfo = getToken(reader);
			token = tokenInfo.getToken();
			
			if(token.equals("@param") ||
			   token.equals("@return") ||
			   token.equals("@exception") ||
			   token.equals("@author") ||
			   token == null ||
			   token.equals("*/")) 
			{
				// end of getting comment description
				fDone = true;
			}
			else if(token.equals("*") ||
					token.equals("") )
			{
				// eat it
			}
			else
			{
				comment.append(token);
				comment.append(" "); // separate tokens
			}
		}
		
		m_description = comment.toString();
		
		return token; // the terminating token
	}
	
	private String processAtReturn(String curToken, Reader reader) throws IOException
	{
		String token = "";
		StringBuilder tokenizer = new StringBuilder();
		boolean fDone = false;
		TokenInfo tokenInfo = null;
		while(!fDone)
		{
			tokenInfo = getToken(reader);
			token = tokenInfo.getToken();
			if(token.equals("@param") 		||
			   token.equals("@return")		||
			   token.equals("@exception") 	||
			   token.equals("@author") 		||
			   token == null 				||
			   token.equals("*/")) 
			{
				// end of getting comment description
				fDone = true;
			}
			else if(token.equals("*") ||
					token.equals("") )
			{
				// eat it
			}
			else
			{
				tokenizer.append(token);
				tokenizer.append(" "); // separate tokens				
			}
		}
		
		m_return = tokenizer.toString();
		m_hasReturn = true;
		
		return token;
	}

	private String processAtParam(String curToken, Reader reader) throws IOException
	{
		String token = "";
		boolean gotParamName = false;
		String paramName = "";
		String paramDescription = "";
		StringBuilder tokenizer = new StringBuilder();
		boolean fDone = false;
		TokenInfo tokenInfo = null;
		TwoCharString twoChar = new TwoCharString();
		char lastChar = '\0';
		
		while(!fDone)
		{			
			tokenInfo = getToken(reader);
			token = tokenInfo.getToken();
			lastChar = tokenInfo.getChar();
			twoChar.addChar(lastChar);
			if(lastChar == '*')
			{
				trace("got a * during atParam processing");
			}
			if(token.equals("@param") 		||
			   token.equals("@return")		||
			   token.equals("@exception") 	||
			   token.equals("@author") 		||
			   token == null 				||
			   token.equals("*/")) 
			{
				// end of getting comment description
				fDone = true;
			}
			else if(token.equals("*") ||
					token.equals("") )
			{
				// eat it
			}
			else
			{
				if(token.endsWith("*/"))
				{
					// trim the token
					int index = token.indexOf("*/");
					token = token.substring(0, index);
					fDone = true;
				}
				
				if(gotParamName)
				{
					tokenizer.append(token);
					tokenizer.append(" "); // separate tokens
				}
				else
				{
					paramName = token;
					gotParamName = true;
				}				
			}
		}
		
		paramDescription = tokenizer.toString();
		m_params.add(new ParameterInfo(paramName, paramDescription));
		
		return token;
	}

	private TokenInfo getToken(Reader reader) throws IOException
	{
		String token = "";
		StringBuilder tokenizer = new StringBuilder();
		char ch = 0x0;
		int value = -1;

		TwoCharString twoChar = new TwoCharString();
		
		while(((value = reader.read()) != -1))
		{
			ch = (char)value;
			twoChar.addChar(ch);
			if(isWhitespace(ch))
			{
				// end of token
				break;
			}	
			else if(tokenizer.toString().equals("/**"))
			{
				// found opening comment
				break;
			}
			else if(twoChar.toString().equals("*/"))
			{
				// found a comment close characters	
				tokenizer.append(ch);
				int size = tokenizer.toString().length();
				if( size > 2)
				{
					tokenizer.delete(0, size - 2);
				}
				
				//tokenizer.delete(0, 1);
				break;
			}
			else
			{
				tokenizer.append(ch);
			}
		}		

		token = tokenizer.toString();
		
		return new TokenInfo(token,ch);
	}
	
	private void trace(String msg)
	{
		System.out.println(msg);
	}
	
	private boolean isEndofLine(char ch)
	{
		return ch == ';';
	}

	/**
	 * Determines if character is ASCII whitespace
	 * 
	 * @param ch
	 *            Character to examine
	 * @return true if whitespace, otherwise false;
	 */
	private boolean isWhitespace(char ch)
	{
		return Character.isWhitespace(ch);
	}
	
	private String m_javadoc;
	private String m_description = "";
	private List<ParameterInfo> m_params;
	private String m_author;
	private String m_return;
	private boolean m_hasAuthor;
	private boolean m_hasReturn;
	
	public class ParameterInfo
	{
		public ParameterInfo(String name, String description)
		{
			m_name = name;
			m_description = description;
		}
		
		public String m_name;
		public String m_description;
	}
}


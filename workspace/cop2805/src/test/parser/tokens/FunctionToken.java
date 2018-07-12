package test.parser.tokens;

import java.util.List;

public abstract class FunctionToken extends Token
{
	protected FunctionToken(String name, CommentToken comment,
			String visibility, List<ParameterToken> parameters)
	{
		super(name, comment, visibility);
		m_parameters = parameters;
	}

	/**
	 * Return the paraemter list
	 * @return List of ParameterTokens
	 */
	public List<ParameterToken> getParameters()
	{
		return m_parameters;
	}

	/**
	 * Get the number of parameters
	 * @return number of parameters
	 */
	public int getParameterCount()
	{
		return m_parameters.size();
	}

	/**
	 * Get the number of parameters
	 * @return number of parameters
	 */
	public String getParameterTypes()
	{
		String types = "";
//		for(ParameterToken pToken : m_parameters)
//		{
//			types += pToken.getType();
//		}
		
		ParameterToken[] parameters = m_parameters.toArray(new ParameterToken[0]);

		for(int i = parameters.length -1; i >= 0; i--)
		{
			types+=parameters[i].getType();
		}
		
		return types;
	}

	@Override
	public String toString()
	{
		String fmt = String.format("%s\nParameter Count: %d\n",
				super.toString(), getParameterCount());
		String paramList = "";
		for(ParameterToken token : m_parameters)
		{
			paramList += token.toString() + System.lineSeparator();
		}

		return fmt + paramList;
	}

	protected List<ParameterToken> m_parameters;
}


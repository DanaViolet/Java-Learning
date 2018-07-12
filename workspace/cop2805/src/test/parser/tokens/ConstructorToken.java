package test.parser.tokens;

import java.util.List;

public class ConstructorToken extends FunctionToken
{
	public ConstructorToken(String name, CommentToken comment, String visibility,
			List<ParameterToken> parameters)
	{
		super(name, comment, visibility, parameters);
	}
}
package test.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import test.parser.tokens.ClassToken;
import test.parser.tokens.CommentToken;
import test.parser.tokens.ConstructorToken;
import test.parser.tokens.FileToken;
import test.parser.tokens.MethodToken;
import test.parser.tokens.OverrideToken;
import test.parser.tokens.PackageToken;
import test.parser.tokens.ParameterToken;
import test.parser.tokens.TokenInfo;
import test.parser.tokens.TwoCharString;

/**
 * Parses a java source file. This assumes the file is valid, so syntax errors
 * are not captured or reported. The purpose is to take an existing, compiled
 * java source and extract out the contents so we can examine the file for
 * conformance to the assignment instructions and coding standards.
 * 
 * @author Scott LaChance
 *
 */
public class JavaParser
{
	public JavaParser()
	{
		m_visibilityStack = new Stack<String>();
		m_parameterStack = new Stack<String>();
	}

	/**
	 * Suppress the trace output
	 * 
	 * @param suppress
	 *            true to suppress, false to display
	 */
	public void suppressTrace(boolean suppress)
	{
		m_suppressTrace = suppress;
	}

	/**
	 * Parse the java file and generate the structure for processing.
	 * 
	 * @param javaFile
	 *            File reference to the java file
	 * @return FileToken reference for the specified file
	 */
	public FileToken parse(File javaFile)
	{
		FileReader fReader = null;
		BufferedReader reader = null;

		m_curState = STATE.SCANNNING;

		try
		{
			m_fileToken = new FileToken(javaFile.getName());

			if(javaFile.exists())
			{
				fReader = new FileReader(javaFile);
				reader = new BufferedReader(fReader);
				scan(reader);
			}
			else
			{
				trace("   ** File doesn't exist: "
						+ javaFile.getAbsolutePath());
			}
		}
		catch(IOException ex)
		{
			trace("IO error: " + ex.getMessage());
		}
		catch(Exception ex)
		{
			trace("Unexpected error: " + ex.getCause() + " " + ex.getMessage()
					+ "toString(): " + ex.toString());
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException ex)
				{
				}
			}
		}

		return m_fileToken;
	}

	private void scan(BufferedReader reader) throws IOException
	{
		char ch = 0x0;
		int value = -1;
		StringBuilder tokenizer = new StringBuilder();
		while((value = reader.read()) != -1)
		{
			ch = (char)value;

			if(!isWhitespace(ch))
			{
				if(ch == '/')
				{
					tokenizer.append(ch);
					// comment start
					processComment(tokenizer, reader);
				}
				else
				{
					tokenizer.append(ch);
					TokenInfo tokenInfo = getToken(tokenizer, reader);
					String token = tokenInfo.getToken();

					// process token
					processToken(token, reader);
				}

				// clear tokenizer
				tokenizer = new StringBuilder();
			}
		}
	}

	private void processComment(StringBuilder tokenizer, BufferedReader reader)
			throws IOException
	{
		// String token = "";
		char ch = 0x0;
		boolean fDone = false;

		String commentStart = tokenizer.toString();

		while(!fDone && (ch = (char)reader.read()) != -1)
		{
			if(ch == '*' || ch == '/') // comment start characters
			{
				commentStart += ch;
			}
			else if(isWhitespace(ch))
			{
				// see if the comment start is a c-style comment or Javadoc
				if(commentStart.equals("/**"))
				{
					// process javadoc
					CommentToken javadoc = processJavadoc(commentStart,
							tokenizer, reader);

					// need to set aside for the next class, constructor or
					// method to associate with
					m_currentCommentToken = javadoc;
					fDone = true;
				}
				else if(commentStart.equals("/*"))
				{
					// process c-style comment
					processCStyleComment(reader);
					fDone = true;
				}
				else if(commentStart.equals("//"))
				{
					// process C++ single line comment
					processCPlusPlusStyleComment(reader);
					fDone = true;
				}
				else
				{
					trace("Unknow comment processing: " + commentStart);
					fDone = true;
				}
			}
			else if(commentStart.equals("//")) // no space between comment start
												// and text
			{
				// process C++ single line comment
				processCPlusPlusStyleComment(reader);
				fDone = true;
			}
		}
	}

	private CommentToken processJavadoc(String commentStart,
			StringBuilder tokenizer, BufferedReader reader) throws IOException
	{
		StringBuilder commentContent = new StringBuilder();
		commentContent.append(commentStart);

		char ch = (char)-1;

		TwoCharString endCommentPair = new TwoCharString();
				
		while((ch = (char)reader.read()) != -1)
		{
			// keep track of the last two characters so we can look for */
			endCommentPair.addChar(ch);

			if( endCommentPair.toString().equals("*/"))
			{
				commentContent.append(ch);
				break;
			}
			else
			{
				commentContent.append(ch); // comment body
			}
		}

		trace("   -- parsed javadoc comment");
		String commentBody = commentContent.toString();
		return new CommentToken(commentBody);
	}

	/**
	 * Read to the end of the current line. Eat the comment
	 * 
	 * @param reader
	 *            BufferedReader to process
	 * @throws IOException on error
	 */
	private void processCPlusPlusStyleComment(BufferedReader reader)
			throws IOException
	{
		char ch = (char)-1;
		int value = -1;

		// Note: casting to char in the while test
		// hides the -1 end of file resulting in an infinite loop
		while((value = reader.read()) != -1)
		{
			ch = (char)value;
			if(ch == '\n')
			{
				break;
			}
		}

		// trace(" -- ate C++ comment");
	}

	/**
	 * Read until we get the closing comment. Eat the comment
	 * 
	 * @param reader
	 *            BufferedReader to process
	 * @throws IOException on error
	 */
	private void processCStyleComment(BufferedReader reader) throws IOException
	{
		StringBuilder commentBuilder = new StringBuilder();
		String endComment = "";

		char ch = (char)-1;

		while((ch = (char)reader.read()) != -1)
		{
			if(ch == '*')
			{
				commentBuilder = new StringBuilder();
				commentBuilder.append(ch);
				char nextChar = (char)reader.read();
				commentBuilder.append(nextChar);
				endComment = commentBuilder.toString();

				if(endComment.equals("*/"))
				{
					break;
				}
			}
		}

		trace("-- ate C-style comment");
	}

	private void processToken(String token, BufferedReader reader)
			throws IOException
	{
		trace("   -- processToken: " + token);
		if(token.equals("package"))
		{
			// process package
			getPackageToken(reader);
		}
		else if(token.equals("/**"))
		{
			// process javadoc comment
		}
		else if(token.equals("import"))
		{
			eatImport(reader);
		}
		else if(token.equals("public") || token.equals("private")
				|| token.equals("protected"))
		{
			// push the visibility token onto the stack for later processing
			m_visibilityStack.push(token);
		}
		else if(token.equals("class"))
		{
			// getClassToken
			switch(m_curState)
			{
				case SCANNNING: // haven't started processing main class yet
					
					ClassToken classToken = getClassToken(reader);
					processClassBody(classToken, reader);
					break;
					
				case CLASSBODY: 	// encountered an embedded class
					// eat embedded classes
					eatEmbeddedClass(reader);
					break;
					
				case METHODBODY:
					break;
			}
		}
		else if(token.equals("interface"))
		{
			// getClassToken
			ClassToken classToken = getClassToken(reader);
			processClassBody(classToken, reader);
		}
		else if(token.equals("enum"))
		{
			switch(m_curState)
			{
				case SCANNNING:
					ClassToken classToken = getClassToken(reader);
					// process enumeration body
					processEnumBody(classToken, reader);
					break;
				default:
					// ignore embedded enumeration declarations
					eatEnum(reader);
					break;
			}

		}
		else if(token.equals("@Override"))
		{
			// getClassToken
			m_currentOverrideToken = new OverrideToken(token);
		}
		else
		{
			trace("   -- pushing '" + token + "' onto visibiliy stack");
			// this would be return type or method/attribute name
			m_visibilityStack.push(token);
		}
	}

	private void eatEmbeddedClass(BufferedReader reader) throws IOException
	{
		char ch = 0x0;
		int value = -1;
		
		while((value = reader.read()) != -1)
		{
			ch = (char)value;
			if(!isWhitespace(ch))
			{
				if(ch == '{')
				{
					eatBody(reader);
				}
			}
		}
		
	}

	private void processEnumBody(ClassToken classToken, BufferedReader reader)
			throws IOException
	{
		char ch = 0x0;
		int value = -1;
		boolean fDone = false;
		StringBuilder tokenizer = new StringBuilder();
		// m_curState = STATE.CLASSBODY;

		while(!fDone && (value = reader.read()) != -1)
		{
			ch = (char)value;
			if(!isWhitespace(ch))
			{
				if(ch == '/')
				{
					tokenizer.append(ch);
					// comment start
					processComment(tokenizer, reader);
				}
				else if(ch == '{') // open brace body start
				{
					// ignore this character and keep going until
					// we find a token
					switch(m_curState)
					{
						case SCANNNING:
							m_curState = STATE.CLASSBODY;
							m_visibilityStack.clear();
							eatEnum(reader); // normally enumeration settings
												// are first
							break;
						case CLASSBODY:
							eatBody(reader);
							break;
						case METHODBODY:
							break;
					}

				}
				else if(ch == '}')
				{
					// body close brace
					fDone = true;
				}
				else
				{
					tokenizer.append(ch);
					TokenInfo tokenInfo = getToken(tokenizer, reader);

					if(tokenInfo.getChar() == '(')
					{
						if(tokenInfo.getToken().equals(
								m_fileToken.getClassToken().getClassName()))
						{
							processConstructor(tokenInfo.getToken(), reader);
						}
						else
						{
							processMethod(tokenInfo.getToken(), reader);
						}
					}
					else if(tokenInfo.getChar() == ';') // an attribute was found
					{
						// clear the stack, want to start fresh after the
						// attribute.
						m_visibilityStack.clear();
					}
					else
					{
						// process token
						processToken(tokenInfo.getToken(), reader);
					}
				}

				// clear tokenizer
				tokenizer = new StringBuilder();
			}
		}

	}

	private void eatEnum(BufferedReader reader) throws IOException
	{
		eatToSemicolon(reader);
	}

	/**
	 * Reads to the end of the line denoted by the semicolon ;
	 * 
	 * @param reader
	 *            Reader to use
	 * @throws IOException on error
	 */
	private void eatImport(Reader reader) throws IOException
	{
		eatToSemicolon(reader);
	}

	private void eatToSemicolon(Reader reader) throws IOException
	{
		int value = -1;
		while((value = reader.read()) != -1)
		{
			char ch = (char)value;
			if(ch == ';')
			{
				break;
			}
		}
	}

	/**
	 * Read the constructor declaration, create a ConstructorToken and parse the
	 * parameters The reader is just past the open parenthesis so any parameters
	 * will be read as tokens Push these onto the stack. This will be in pairs:
	 * 
	 * <pre>
	 * value
	 * type
	 * value
	 * type
	 * </pre>
	 * 
	 * @param constructorName
	 * @param reader
	 * @throws IOException
	 */
	private void processConstructor(String constructorName,
			BufferedReader reader) throws IOException
	{
		String token = "";
		StringBuilder tokenizer = new StringBuilder();

		char ch = 0x0;

		while((ch = (char)reader.read()) != -1)
		{
			if(isValidJavaChar(ch))
			{
				tokenizer.append(ch);
			}
			else if(ch == ')')
			{
				if(tokenizer.length() > 0)
				{
					token = tokenizer.toString();
					// push onto the parameter list
					m_parameterStack.push(token);
				}

				List<ParameterToken> paramList = new ArrayList<ParameterToken>();

				// get all the parameters
				while(!m_parameterStack.isEmpty())
				{
					String param = m_parameterStack.pop();
					String type = m_parameterStack.pop();
					ParameterToken paramToken = new ParameterToken(type, param);
					paramList.add(paramToken);
				}

				String visibility = m_visibilityStack.size() > 0
						? m_visibilityStack.pop() : "";
				ConstructorToken cToken = new ConstructorToken(constructorName,
						m_currentCommentToken, visibility, paramList);
				trace("  -- adding constructor: " + cToken.getName());
				m_fileToken.getClassToken().addConstructor(cToken);
				break;
			}
			else
			{
				if(tokenizer.length() > 0)
				{
					if(ch == '<')
					{
						tokenizer.append(ch);
					}
					else
					{
						if(ch == '>')
						{
							tokenizer.append(ch);
						}

						token = tokenizer.toString();
						// push onto the parameter list
						m_parameterStack.push(token);
						tokenizer = new StringBuilder();
					}
				}
				// break;
			}
		}
	}

	/**
	 * This will process the constructors and methods of the class It will reuse
	 * helper methods.
	 * 
	 * @param classToken
	 *            ClassToken to update
	 * @param reader
	 *            Current BufferedReader used for parsing
	 * @throws IOException
	 */
	private void processClassBody(ClassToken classToken, BufferedReader reader)
			throws IOException
	{
		trace("** Starting processing class body **");
		char ch = 0x0;
		int value = -1;
		boolean fDone = false;
		StringBuilder tokenizer = new StringBuilder();
		while(!fDone && (value = reader.read()) != -1)
		{
			ch = (char)value;
			if(!isWhitespace(ch))
			{
				if(ch == '/')
				{
					tokenizer.append(ch);
					// comment start
					processComment(tokenizer, reader);
				}
				else if(ch == '{') // open brace body start
				{
					// ignore this character and keep going until
					// we find a token
					switch(m_curState)
					{
						case SCANNNING:
							m_curState = STATE.CLASSBODY;
							m_visibilityStack.clear();
							break;
						case CLASSBODY:
							eatBody(reader);
							break;
						case METHODBODY:
							break;
					}

				}
				else if(ch == '}')
				{
					// body close brace
					m_curState = STATE.SCANNNING;
					fDone = true;
				}
				else if(ch == '"')
				{
					// process string
					tokenizer.append(ch); // append the opening quote
					getStringToken(tokenizer, reader);
				}
				else if(ch == ';')
				{
					// reached the end of a statement
					// drop all previous state info
					m_visibilityStack.clear();
				}
				else if(ch == '=')
				{
					// assignment statement - read to the end
					getAssignmentToken(tokenizer, reader);
					// completed statement
					m_visibilityStack.clear();
				}
				else
				{
					tokenizer.append(ch);
					TokenInfo tokenInfo = getToken(tokenizer, reader);

					if(tokenInfo.getChar() == '(')
					{
						if(tokenInfo.getToken().startsWith("@"))
						{
							// eat it
							eatAttribute(reader);
						}
						else if(tokenInfo.getToken().equals(
								m_fileToken.getClassToken().getClassName()))
						{
							processConstructor(tokenInfo.getToken(), reader);
						}						
						else
						{
							processMethod(tokenInfo.getToken(), reader);
						}
					}
					else if(tokenInfo.getChar() == ';') // an attribute was found
					{
						// clear the stack, want to start fresh after the
						// attribute.
						m_visibilityStack.clear();
					}
					else
					{
						// process token
						processToken(tokenInfo.getToken(), reader);
					}
				}

				// clear tokenizer
				tokenizer = new StringBuilder();
			}
		}
		
		trace("** Completed processing class body **");
	}

	private void eatAttribute(BufferedReader reader) throws IOException
	{
		int value = -1;
		TwoCharString eol = new TwoCharString();
		
		while((value = reader.read()) != -1)
		{
			char ch = (char)value;
			eol.addChar(ch);
			if(System.lineSeparator().equals(eol.toString()) || // microsoft \r\n
			   System.lineSeparator().equals(ch)) 				// unix/linux \n
			{
				break;
			}
		}
		
	}

	private void getAssignmentToken(StringBuilder tokenizer,
			BufferedReader reader) throws IOException
	{
		char ch = (char)-1;
		boolean fDone = false;
		while(!fDone)
		{
			ch = (char)reader.read();
			if(ch == '"') // closing quote
			{
				tokenizer.append(ch);
				getStringToken(tokenizer, reader);
			}
			else if(ch == ';')
			{
				// end of statement
				fDone = true;
			}
			else
			{
				tokenizer.append(ch);
			}
		}

	}

	private void getStringToken(StringBuilder tokenizer, BufferedReader reader)
			throws IOException
	{
		char ch = (char)-1;
		boolean fDone = false;
		while(!fDone)
		{
			ch = (char)reader.read();
			if(ch == '"') // closing quote
			{
				fDone = true;
				tokenizer.append(ch); // append closing quote
			}
			else
			{
				tokenizer.append(ch);
			}
		}
	}

	private void processMethod(String token, BufferedReader reader)
			throws IOException
	{
		char ch = (char)-1;
		boolean fDone = false;
		StringBuilder tokenizer = new StringBuilder();

		String visibility = ""; // package
		String methodName = token;
		String returnType = "";
		boolean isAbstract = false;
		boolean isStatic = false;

		while(!m_visibilityStack.isEmpty())
		{
			String value = m_visibilityStack.pop();
			if(value.equals("public") || value.equals("protected")
					|| value.equals("private"))
			{
				visibility = value;
			}
			else if(value.equals("abstract"))
			{
				isAbstract = true;
			}
			else if(value.equals("static"))
			{
				isStatic = true;
			}
			else
			{
				returnType = value;
			}
		}

		// done with this set
		m_visibilityStack.clear();

		while((ch = (char)reader.read()) != -1 && !fDone)
		{
			// m_content += ch;
			if(isValidJavaChar(ch))
			{
				tokenizer.append(ch);
			}
			else if(ch == ')')
			{
				if(tokenizer.length() > 0)
				{
					token = tokenizer.toString();
					// push onto the parameter list
					m_parameterStack.push(token);
				}

				List<ParameterToken> paramList = new ArrayList<ParameterToken>();

				// get all the parameters
				while(!m_parameterStack.isEmpty())
				{
					String param = m_parameterStack.pop();
					String paramType = m_parameterStack.pop();
					ParameterToken paramToken = new ParameterToken(paramType,
							param);
					paramList.add(paramToken);
				}

				OverrideToken override = m_currentOverrideToken == null
						? new OverrideToken() : m_currentOverrideToken;
				MethodToken cToken = new MethodToken(methodName, override,
						m_currentCommentToken, visibility, returnType,
						paramList);
				if(isAbstract)
					cToken.setAbstract();
				if(isStatic)
					cToken.setStatic();
				trace("  -- Adding method: " + cToken.getName());
				m_fileToken.getClassToken().addMethod(cToken);				
				break;
			}
			else
			{
				if(tokenizer.length() > 0)
				{
					if(ch == '<')
					{
						tokenizer.append(ch);
						getGenerics(tokenizer, reader);
						token = tokenizer.toString();
						// push onto the parameter list
						m_parameterStack.push(token);
						tokenizer = new StringBuilder();
					}
					else
					{
//						if(ch == '>')
//						{
//							tokenizer.append(ch);
//						}

						token = tokenizer.toString();
						// push onto the parameter list
						m_parameterStack.push(token);
						tokenizer = new StringBuilder();
					}
				}
			}
		}
	}
	
	/**
	 * Encountered an opening < so keep building string until closing > is found.
	 * Any embedded opening < angle brackets and their corresponding closing > angle bracket are included
	 * in the processing.
	 * @param tokenizer StringBuilder to keep appending to
	 * @param reader Buffered reader to read
	 * @throws IOException 
	 */
	private void getGenerics(StringBuilder tokenizer, BufferedReader reader) throws IOException
	{
		Stack<Character> angleBrackets = new Stack<Character>();
		angleBrackets.push('<'); // got here because we encountered on
		char ch = 0;
		boolean fDone = false;
		while(!fDone)
		{
			ch = (char)reader.read();
			if(ch == -1)
			{
				fDone = true;
				trace("  *** Unexpectedly reached end of file in getGenerics methd *** ");
			}
			else if( ch == '>')
			{
				angleBrackets.pop(); // reached a closing angle bracket
				tokenizer.append(ch); // still need to append it.
			}
			else if(ch == '<')
			{
				// found an embedded open angle bracket
				angleBrackets.push(ch);
			}
			else
			{
				tokenizer.append(ch);
			}
			
			if(angleBrackets.size() == 0)
			{
				fDone = true;
			}
		}
	}

	/**
	 * This eats the body of a method. Methods can have embedded open/close
	 * braces so when we encounter an open brace we need to track its matching
	 * close brace.
	 * 
	 * @param reader
	 * @throws IOException
	 */
	private void eatBody(BufferedReader reader) throws IOException
	{
		m_visibilityStack.clear();
		
		char ch = (char)-1;
		Stack<String> braceStack = new Stack<String>();
		braceStack.push("{"); // initialize to the brace that started it all
		boolean fDone = false;
		while(!fDone)
		{
			ch = (char)reader.read();
			if(ch == '{')
			{
				braceStack.push("{");
			}
			else if(ch == '}')
			{
				braceStack.pop();
			}

			if(braceStack.isEmpty())
			{
				fDone = true;
			}
		}
	}

	private ClassToken getClassToken(BufferedReader reader) throws IOException
	{
		CommentToken commentToken = m_currentCommentToken == null
				? new CommentToken("") : m_currentCommentToken;
		StringBuilder tokenizer = new StringBuilder();
		TokenInfo tokenInfo = getToken(tokenizer, reader);
		String className = tokenInfo.getToken();
		ClassToken classToken = new ClassToken(className, commentToken);

		if(tokenInfo.getChar() == '{')
		{
			// need to possibly change processing state
			switch(m_curState)
			{
				case SCANNNING:
					m_curState = STATE.CLASSBODY;
					m_visibilityStack.clear();
					eatEnum(reader); // normally enumeration settings
										// are first
					break;
				case CLASSBODY:
					eatBody(reader);
					break;
				case METHODBODY:
					break;
			}
		}
		while(!m_visibilityStack.isEmpty())
		{
			String value = m_visibilityStack.pop();
			if(value.equals("public") || value.equals("protected")
					|| value.equals("private"))
			{
				classToken.setVisibility(value);
			}
			else if(value.equals("abstract"))
			{
				classToken.setAbstract();
			}
			else if(value.equals("static"))
			{
				classToken.setStatic();
			}
		}

		m_currentCommentToken = null;
		m_fileToken.setClassToken(classToken);

		return classToken;
	}

	private void getPackageToken(BufferedReader reader) throws IOException
	{
		StringBuilder tokenizer = new StringBuilder();
		String packageName = getToken(tokenizer, reader).getToken();

		PackageToken token = new PackageToken(packageName);
		m_fileToken.setPackageToken(token);
	}

	/**
	 * Start reading a string of characters and stop when we get to whitespace
	 * 
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	private TokenInfo getToken(StringBuilder tokenizer, BufferedReader reader)
			throws IOException
	{
		String token = "";
		char ch = 0x0;

		while((ch = (char)reader.read()) != -1)
		{
			// m_content += ch;
			if(isValidJavaChar(ch))
			{
				tokenizer.append(ch);
			}
			else if(isEndofLine(ch))
			{
				token = tokenizer.toString();
				break;
			}
			else
			{
				if(ch == '<')
				{
					tokenizer.append(ch);
					processGeneric(tokenizer, reader);
					// if(!)
					// {
					// token = tokenizer.toString();
					// }
				}
				else if(ch == '.')
				{
					// qualified name
					tokenizer.append(ch);
					processQualifiedName(tokenizer, reader);
				}

				token = tokenizer.toString();
				break;
			}
		}

		// return token;
		return new TokenInfo(token, ch);
	}

	/**
	 * Read all valid characters, including periods up to the next space
	 * 
	 * @param tokenizer
	 *            Reference to tokenizer being processed
	 * @param reader
	 *            The Reader stream
	 * @throws IOException
	 */
	private void processQualifiedName(StringBuilder tokenizer,
			BufferedReader reader) throws IOException
	{
		char ch = 0x0;
		int value = -1;
		boolean fDone = false;

		while(!fDone)
		{
			value = reader.read();
			if(value == -1)
			{
				fDone = true;
			}
			else
			{
				ch = (char)value;
				if(ch == '<')
				{
					// encountered a generic definition
					// this will read to the end of the declaration
					tokenizer.append(ch);
					processGeneric(tokenizer, reader);
					fDone = true;
				}
				else if(Character.isWhitespace(ch))
				{
					fDone = true;
				}
				else
				{
					tokenizer.append(ch);
				}
			}
		}

	}

	/**
	 * Process a line with an open angle bracket < This can be part of generic
	 * declarations like Set<E extends Comaparable<E>> or it can be a bit
	 * assignment like MAXIMUM_CAPACITY = 1 << 30; for an attribute. The latter
	 * should be ignored.
	 * 
	 * @param tokenizer
	 *            the tokenizer we are appending to
	 * @param reader
	 *            The reader we are pulling from
	 * @return true if this is a method declaration, otherwise false
	 * @throws IOException
	 */
	private boolean processGeneric(StringBuilder tokenizer,
			BufferedReader reader) throws IOException
	{
		Stack<String> angleStack = new Stack<String>();
		boolean fContinue = true;
		boolean fDone = false;

		// we got here because we encountered <
		angleStack.push("<");
		char ch = 0x0;
		int value = -1;

		while(!fDone && ((value = reader.read()) != -1) )
		{
			if(ch == ';')
			{
				// found end of line, this is an attribute assignment
				fDone = true;
				fContinue = false;
			}
			else
			{
				ch = (char)value;
				if(ch == '<')
				{
					// another embedded angle bracket
					angleStack.push("<");
				}
				else if(ch == '>')
				{
					// closing angle bracket
					angleStack.pop();
				}
				else if(ch == ';')
				{
					// found end of line, this is an attribute assignment
					fDone = true;
				}

				tokenizer.append(ch);

				if(angleStack.size() == 0)
				{
					// reached the end of angle brackets
					fDone = true;
				}
			}
		}

		return fContinue;
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

	/**
	 * Determines if character is ASCII whitespace
	 * 
	 * @param ch
	 *            Character to examine
	 * @return true if whitespace, otherwise false;
	 */
	private boolean isValidJavaChar(char ch)
	{
		return Character.isJavaIdentifierPart(ch);
	}

	private void trace(String msg)
	{
		if(!m_suppressTrace)
		{
			System.out.println(msg);
		}
	}

	enum STATE
	{
		SCANNNING, CLASSBODY, METHODBODY
	}

	STATE m_curState;
	FileToken m_fileToken;
	CommentToken m_currentCommentToken;
	OverrideToken m_currentOverrideToken;
	Stack<String> m_visibilityStack;
	Stack<String> m_parameterStack;
	boolean m_suppressTrace;
}

//class TokenInfo
//{
//	TokenInfo(String token, char last)
//	{
//		this.token = token;
//		lastChar = last;
//	}
//
//	String token;
//	char lastChar;
//}

/**
 * Manages a two character string. When a character is added, the right position
 * shifts to the left and the new character takes the right position Example:
 * 
 * Original op Add r Update: pr
 * 
 * @author Scott LaChance
 */
//class TwoCharString
//{
//	TwoCharString()
//	{
//		m_chars = new char[2];
//		m_length = 0;
//	}
//
//	public void addChar(char ch)
//	{
//		switch(m_length)
//		{
//			case 0:
//				m_chars[0] = ch;
//				m_length = 1;
//				break;
//			case 1:
//				m_chars[1] = ch;
//				m_length = 2;
//				break;
//			case 2:
//				m_chars[0] = m_chars[1];
//				m_chars[1] = ch;
//				break;
//		}
//	}
//
//	@Override
//	public String toString()
//	{
//		// TODO Auto-generated method stub
//		return new String(m_chars);
//	}
//
//	private char[] m_chars;
//	private int m_length = 0;
//}

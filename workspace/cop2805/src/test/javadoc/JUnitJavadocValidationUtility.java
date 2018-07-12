package test.javadoc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import test.TestResult;

/**
 * Implements the logic to generically process a java source file and evaluate
 * its compliance with the course Javadoc rules
 * 
 * Engineering note: While using the internal Java libraries to parse the code
 * would be a good implementation, the dependency on tools.js means another
 * library dependency required for the course project. I haven't determined if
 * this is really OK. The complexity of the API set makes it difficult to
 * integrate right now so this is an intermediate option (more brute force).
 *
 * 
 * Validates the following:
 * <ul>
 * <li>file package name</li>
 * <li>@author attribute with student name (not instructor)</li>
 * <li>Validates all the public methods have javadoc comments</li>
 * <li>Matches the required methods and signatures for the specified files</li>
 * </ul>
 * 
 * @author Scott LaChance
 *
 */
public class JUnitJavadocValidationUtility
{
	public JUnitJavadocValidationUtility(String testeNam,
			List<FileTestData> testFiles)
	{
		m_testeName = testeNam;
		m_testFiles = testFiles;
		m_stream = System.out; // Standard out
		m_testResult = new JavadocTestResults();
	}

	/**
	 * Name provided for thist test
	 * 
	 * @return
	 */
	public String getTestName()
	{
		return m_testeName;
	}

	/**
	 * Execute the test
	 * 
	 * @return
	 */
	public TestResult runTest()
	{
		trace("Running " + getTestName());
		m_testResult.setPassed();
		boolean result = true;
		for(FileTestData testFile : m_testFiles)
		{
			// if the result of validateJavadoc is false,
			// the result will never get reset to true
			result = result && validateJavadoc(testFile);
		}

		m_testResult.setResult(result);
		return m_testResult;
	}

	/**
	 * Set the list of expected method results.
	 * 
	 * @param list
	 */
	public void setTestFiles(List<FileTestData> list)
	{
		m_testFiles = list;
	}

	/**
	 * Validates @author has been inserted into code and no void main is
	 * contained in the file
	 * 
	 * @return true if code is properly implemented
	 */
	private boolean validateJavadoc(FileTestData file)
	{
		//trace(" -- validateCode --");
		boolean result = true;

		String codeFilePath = "";
		try
		{
			codeFilePath = initFilePath(file);
			trace("  -- codeFilePath: " + codeFilePath);

			if(codeFilePath == null)
			{
				trace(" ** No file found");
				result = false;
			}
			else
			{
				File srcFile = new File(codeFilePath);

				if(!srcFile.exists())
				{
					// trace(" ** Source file doesn't exist at " +
					// codeFilePath);
					JavadocTestResultDetail detail = new JavadocTestResultDetail(
							"Getting source file");
					detail.addResultDetail("   ** Source file doesn't exist at "
							+ codeFilePath);
					m_testResult.addTestResultDetail(detail);
					result = false;
				}
				else
				{
					// evaluate file
					trace("Reading file " + srcFile);
					StringBuilder content = readFileToEnd(srcFile);
					
					trace("parsing ... ");
					FileToken fileToken = parseContent(srcFile.getName(),
							content);
					
					trace("evaluating ... ");
					result = evaluateFileToken(fileToken, file);
					//result = evaluateCodeFile(content, file);
				}
			}
		}
		catch(IOException ex)
		{
			trace(ex.getMessage());
			result = false;
		}

		return result;
	}

	private StringBuilder readFileToEnd(File file)
	{
		FileReader reader = null;
		BufferedReader buffer = null;
		StringBuilder content = new StringBuilder();
		try
		{
			reader = new FileReader(file);
			buffer = new BufferedReader(reader);

			String line = "";
			while((line = buffer.readLine()) != null)
			{
				content.append(line);
				content.append(CRLF);
			}
		}
		catch(IOException ex)
		{
			trace(ex.getMessage());
		}
		finally
		{
			if(buffer != null)
				try
				{
					buffer.close();
				}
				catch(IOException ex)
				{
				}
		}

		return content;
	}

	/**
	 * Setup the test file path. Dev environment and runtime environment have
	 * different current directory behavior. For runtime testing, it is the bin
	 * folder For development, it is the current project folder. The test will
	 * create a new File instance for the current directory. If the directory is
	 * bin, the we use the simple file name for the test If it isn't we append
	 * the .\src\week00\ to the file path. That way this will work in
	 * development and testing.
	 * 
	 * @throws IOException
	 */
	private String initFilePath(FileTestData file) throws IOException
	{
		String filePath = "";
		String fileName = file.getFileName();
		String packageName = file.getPackageName();
		File curDir = new File(".");
		File curDir1 = new File(curDir.getAbsolutePath());
		boolean exists = curDir1.exists();
		boolean isDir = curDir1.isDirectory();
		String bin = curDir1.getParentFile().getName();
		boolean b = bin.equals("bin");
		File srcFolder = null;
		if(exists && isDir && b)
		{
			// get the src folder
			File binParent = curDir1.getParentFile().getParentFile();
			File[] fileList = binParent.listFiles();
			srcFolder = findSrcFolder(fileList);
			filePath = srcFolder.getCanonicalPath() + File.separator + fileName;
		}
		else
		{
			File[] fileList = curDir1.listFiles();
			srcFolder = findSrcFolder(fileList);

			filePath = srcFolder.getCanonicalPath() + File.separator
					+ packageName + File.separator + fileName;
		}

		return filePath;
	}

	/**
	 * Searches the file list to find the src folder. Used for both assignment
	 * evaluation and Eclipse JUnit processing
	 * 
	 * @param fileList
	 *            File array to examine
	 * @return the src File reference if found, otherwise null
	 */
	private File findSrcFolder(File[] fileList)
	{
		File srcFolder = null;
		for(File f : fileList)
		{
			if(f.getName().equals("src"))
			{
				srcFolder = f;
				break;
			}
		}

		return srcFolder;
	}

	private boolean evaluateFileToken(FileToken fileToken, FileTestData file)
	{
		String expectedFileName = file.getFileName();
		String actualFileName = fileToken.getFileName();

		boolean fileNameMatch = expectedFileName.equals(actualFileName);
		boolean fileNameClassNameMatch = verifyFileNameClassNameMatch(
				fileToken);

		boolean hasClassComment = verifyClassComment(
				fileToken.getClassToken().getComment());
		String expectedPackageName = file.getPackageName();
		String actualPackageName = fileToken.getPackageName();
		boolean correctPackage = expectedPackageName.equals(actualPackageName);
		if(!correctPackage)
		{
			String msg = String.format("    -- Package mismatch - expected: %s, actual: %s", expectedPackageName, actualPackageName) + CRLF;
			updateResultStatus("package validation", msg);
		}

		List<MethodToken> methodTokenList = fileToken.getClassToken()
				.getMethodTokens();
		List<MethodTestData> expectedMethodsList = file.getExpectedMethods();

		// verify the parsed methods match the expected methods
		boolean methodsMatch = true;
		for(MethodToken methodToken : methodTokenList)
		{
			// find the corresponding method
			String methodName = methodToken.getName();
			MethodTestData expectedMethod = file
					.findMethodTestDataByName(methodName);
			if(expectedMethod != null)
			{
				boolean nameMatch = true;
				boolean visbilityMatch = methodToken.getVisibility()
						.equals(expectedMethod.getVisibility());
				boolean returnTypeMatch = methodToken.getReturnType()
						.equals(expectedMethod.getReturnType());
				boolean validComment = verifyMethodComment(methodToken);
				boolean parameterCountMatch = 
						methodToken.getParameterCount() == expectedMethod.getParameterCount();
				
				boolean currentMethod = nameMatch && visbilityMatch && returnTypeMatch && validComment
						&& parameterCountMatch;
				methodsMatch = methodsMatch && currentMethod;
				if(!currentMethod)
				{
					StringBuilder msg = new StringBuilder();
					if(!visbilityMatch)
					{
						msg.append(String.format("    -- Visibility mismatch - expected: %s, actual: %s", expectedMethod.getVisibility(), methodToken.getVisibility()));
						msg.append(CRLF);
					}
					
					if(!returnTypeMatch)
					{
						msg.append(String.format("    -- Return type mismatch - expected: %s, actual: %s", expectedMethod.getReturnType(), methodToken.getReturnType()));
						msg.append(CRLF);
					}
					
					if(!parameterCountMatch)
					{
						msg.append(String.format("    -- Parameter count mismatch - expected: %d, actual: %d", expectedMethod.getParameterCount(), methodToken.getParameterCount()));
						msg.append(CRLF);
					}
					
					updateResultStatus("method validation", msg.toString());
				}
			}
			else
			{
				// no name match
				String msg = String.format("    -- Missing method - %s", methodName);
				updateResultStatus("method validation", msg);
			}
		}
		
		if(!fileNameMatch)
		{
			// no name match
			String msg = String.format("    -- File name mismatch - expected: %s, actual: %s", expectedFileName, actualFileName);
			updateResultStatus("file name validation", msg);
		}

		return fileNameMatch && fileNameClassNameMatch && hasClassComment && methodsMatch
				&& correctPackage;
	}
	
	private void updateResultStatus(String description, String msg)
	{

		JavadocTestResultDetail detail = new JavadocTestResultDetail(
				description);
		detail.addResultDetail(msg);
		m_testResult.addTestResultDetail(detail);
	}

	private boolean verifyFileNameClassNameMatch(FileToken fileToken)
	{
		String className = fileToken.getClassToken().getClassName();
		String fileName = fileToken.getFileName();
		int index = fileName.indexOf(".");
		String fileNameOnly = fileName.substring(0, index);
		boolean match = className.equals(fileNameOnly);
		if(!match)
		{
			String msg = String.format("    -- Class name / File name mismatch - expected %s", className);
			updateResultStatus("class name / file name validation", msg);
		}
		
		return match;
	}

	private boolean verifyMethodComment(MethodToken token)
	{
		// verify a comment with description exists and any parameters
		// have a @param and @return as appropriate
		CommentToken comment = token.getComment();
		String javadoc = comment.getJavadoc();
		List<ParameterToken> parameterList = token.getParameters();
		String returnType = token.getReturnType();
		
		boolean hasCommentTest = javadoc.length() > 0;
		boolean validParams = true;
		for(ParameterToken param : parameterList)
		{
			String paramName = param.getName();
			String lookFor = "@param " + paramName;
			//trace(token.getName() + " param type: " + param.getType() + ", param name: " + param.getName());
			boolean foundParam = javadoc.contains(lookFor);
			if(!foundParam)
			{
				String msg = String.format("Expected @param missing from method %s: param %s", token.getName(), paramName);
				updateResultStatus("method param validation", msg);
			}
			validParams = validParams && foundParam;
		}
				
		boolean validReturn = true;
		if(!returnType.equals("void"))
		{
			String lookFor ="@return";
			boolean foundReturn = javadoc.contains(lookFor);
			if(!foundReturn)
			{
				String msg = String.format("Expected @return missing from method %s", token.getName());
				updateResultStatus("method param validation", msg);
			}
			validReturn = validReturn && foundReturn;
		}

		return hasCommentTest && validParams && validReturn;
	}

	private boolean verifyClassComment(CommentToken comment)
	{
		boolean hasClassComment = comment.getJavadoc().length() > 0;
		String javadoc = comment.getJavadoc();
		boolean hasAuthor = javadoc.indexOf("@author") > 0 ? true : false;
		return hasClassComment && hasAuthor;
	}

	private FileToken parseContent(String fileName, StringBuilder content)
	{
		// use parsing concepts to generate the comment tokens, @Overrides and
		// method signatures
		// and return those as entities for testing evaluation
		// package
		// File information:
		//
		int curPos = 0; // analysis character position
		ParserContext parserContext = new ParserContext(fileName, curPos,
				content);
		FileToken fileToken = getFileToken(parserContext);
		ClassToken classToken = getClassToken(parserContext);
		fileToken.setClassToken(classToken);
		return fileToken;
	}

	private ClassToken getClassToken(ParserContext parserContext)
	{
		ClassToken classToken = new ClassToken("Unknown", null);
		String CLASS = "class";
		StringBuilder content = parserContext.getContent();
		CommentToken classJavadocComment = getCommentToken(parserContext);

		int curPos = parserContext.getCurPos();
		int startIndex = content.indexOf(CLASS, curPos);
		if(startIndex > -1)
		{
			int start = startIndex + CLASS.length();
			int endIndex = content.indexOf("{", start);
			String name = content.substring(start, endIndex).trim();
			name = normalizeClassName(name);
			classToken = new ClassToken(name, classJavadocComment);

			parserContext.setCurPosition(endIndex);

			getConstructorTokens(classToken, parserContext);
			getMethodTokens(classToken, parserContext);
		}

		return classToken;
	}
	
	/**
	 * Class name can be decorated with <T> which gets through the initial parse
	 * This causes a mismatch between the file name and the class which must be the same
	 * So this method strips the Generic characters if present
	 * 
	 * @param className Possibly decorated class name
	 * @return Raw class name
	 */
	private String normalizeClassName(String className)
	{
		String normalized = className;
		
		//              (Class)(Optional Generic <T>)
		String regex = "(\\w*)(<\\w>)?";
		// This works to find the name and the generic tag
		// in two groups. Only need the first group
		Pattern exp = Pattern.compile(regex);
		Matcher match = exp.matcher(className);
		
		if(match.matches())
		{
			MatchResult result = match.toMatchResult();
			String candidate = result.group(1);
			trace(" normalized candidate : " + candidate);	
			normalized = candidate;
		}
		else
		{
			trace(" not a regex match");
			// this might be inheritance
			int openAngleIndex = className.indexOf("<");
			if( openAngleIndex != -1)
			{
				String candidate = className.substring(0, openAngleIndex);
				trace(" normalized candidate : " + candidate);	
				normalized = candidate;
			}
		}
		
		return normalized;
	}

	private void getMethodTokens(ClassToken classToken,
			ParserContext parserContext)
	{
		List<String> OPEN_METHOD_TOKENS = new ArrayList<String>();
		OPEN_METHOD_TOKENS.add("public");
		OPEN_METHOD_TOKENS.add("private");
		OPEN_METHOD_TOKENS.add("protected");

		StringBuilder content = parserContext.getContent();
		String name = classToken.getClassName();

		List<MethodToken> methods = new ArrayList<MethodToken>();
		int curPos = parserContext.getCurPos();
		int endIndex = curPos;
		boolean fDone = false;
		while(!fDone)
		{
			int startIndex = -1;
			int methodStartIndex = -1;
			for(String openToken : OPEN_METHOD_TOKENS)
			{
				methodStartIndex = content.indexOf(openToken, endIndex);
				if(methodStartIndex > -1)
				{
					break; // break for loop
				}
			}
			startIndex = methodStartIndex;
			if(startIndex == -1)
			{
				fDone = true;
			}
			else
			{
				boolean isMethod = isMethodLine(parserContext,
						methodStartIndex);
				if(isMethod)
				{
					CommentToken comment = getCommentToken(parserContext);
					OverrideToken override = getOverrideToken(parserContext,
							methodStartIndex);
					startIndex = parserContext.getCurPos();
					endIndex = content.indexOf(CRLF,
							startIndex + name.length());
					String methodLine = content.substring(startIndex, endIndex)
							.trim();
					methodLine = normalizeLine(methodLine).trim();
					int begin = methodLine.indexOf(" ");
					String visibility = methodLine.substring(0, begin).trim();
					// begin = begin + visibility.length();
					int end = methodLine.indexOf(" ", begin + 1);
					String returnType = methodLine.substring(begin, end).trim();
					begin = end;
					end = methodLine.indexOf("(");
					String methodName = methodLine.substring(begin, end).trim();

					List<ParameterToken> parameters = getParameterTokens(
							methodLine);

					MethodToken method = new MethodToken(methodName, override,
							comment, visibility, returnType, parameters);

					methods.add(method);
				}
				else
				{
					// advance the cursor
					endIndex = content.indexOf(CRLF,
							startIndex + name.length());
					parserContext.setCurPosition(endIndex);
				}
			}

			if(parserContext.getCurPos() >= parserContext.getContent().length())
			{
				fDone = true;
			}
		}

		classToken.setMethods(methods);
		parserContext.setCurPosition(endIndex);
	}

	private List<ParameterToken> getParameterTokens(String methodLine)
	{
		List<ParameterToken> parametersList = new ArrayList<ParameterToken>();

		int openParenIndex = methodLine.indexOf("(");
		int closeParenIndex = methodLine.indexOf(")");
		String parameterFragment = methodLine.substring(openParenIndex + 1,
				closeParenIndex);
		// break by ,
		String[] parameterGroups = parameterFragment.split(",");
		for(String group : parameterGroups)
		{
			group = group.trim();
			// split by space
			if(group.length() > 0)
			{
				String[] elements = group.split(" ");
				ParameterToken newParam = new ParameterToken(elements[0],
						elements[1]);
				parametersList.add(newParam);
			}
		}

		return parametersList;
	}

	private boolean isMethodLine(ParserContext parserContext,
			int methodStartIndex)
	{
		StringBuilder content = parserContext.getContent();
		int eolIndex = content.indexOf(CRLF, methodStartIndex);
		String fragment = content.substring(methodStartIndex, eolIndex).trim();
		int openParenIndex = fragment.indexOf("(");
		int closeParenIndex = fragment.indexOf(")");
		int semiColonIndex = fragment.indexOf(";");

		boolean noOpenParen = openParenIndex == -1;
		boolean noCloseParen = closeParenIndex == -1;
		boolean semiColonExisits = semiColonIndex > 0;

		boolean isMethod = !noOpenParen && !noCloseParen && !semiColonExisits;

		return isMethod;
	}

	private String normalizeLine(String line)
	{
		// remove all preceding and lagging whitespace.
		// trim() doens't always remove leading /r/n/t characters
		String normalized = line.replace("\r\n\t", "");
		return normalized;
	}

	private void getConstructorTokens(ClassToken classToken,
			ParserContext parserContext)
	{
		// CommentToken comment = getCommentToken(parserContext);
		StringBuilder content = parserContext.getContent();
		String searchName = classToken.getClassName() + "("; // include open
																// paren
		List<ConstructorToken> constructors = new ArrayList<ConstructorToken>();
		int curPos = parserContext.getCurPos();
		int endIndex = curPos;
		boolean fDone = false;
		while(!fDone)
		{

			int startIndex = content.indexOf(searchName, endIndex);
			if(startIndex == -1)
			{
				fDone = true;
			}
			else
			{
				CommentToken comment = getCommentToken(parserContext);
				endIndex = parserContext.getCurPos();
				endIndex = content.indexOf(CRLF,
						startIndex + searchName.length() - 1);
				String constructorLine = content.substring(startIndex, endIndex)
						.trim();
				StringBuilder prefix = new StringBuilder();
				for(int i = startIndex; i > 0; i--)
				{
					// get previous character
					String prevChar = content.substring(i - 1, i);

					if(prevChar.equals(LF))
					{
						break;
					}
					else
					{
						prefix.insert(0, prevChar);
					}
				}

				constructorLine = (prefix.toString() + constructorLine).trim();
				int begin = constructorLine.indexOf(" ");
				String visibility = constructorLine.substring(0, begin).trim();
				int end = constructorLine.indexOf("(");
				String constructorName = constructorLine.substring(begin, end)
						.trim();
				ConstructorToken constructor = new ConstructorToken(
						constructorName, comment, visibility);
				constructors.add(constructor);
				parserContext.setCurPosition(endIndex);
			}
		}

		classToken.setConstructors(constructors);
		parserContext.setCurPosition(endIndex);
	}

	private OverrideToken getOverrideToken(ParserContext parserContext,
			int maxEndIndex)
	{
		StringBuilder content = parserContext.getContent();
		OverrideToken overrideToken = new OverrideToken("");
		String OVERRIDE = "@Override";
		int start = parserContext.getCurPos();
		int end = maxEndIndex;
		String fragment = content.substring(start, end).trim();
		int startIndex = fragment.indexOf(OVERRIDE);
		if(startIndex > -1)
		{
			String text = fragment.substring(startIndex,
					startIndex + OVERRIDE.length());
			// String text = content.substring(startIndex, endIndex).trim();
			overrideToken = new OverrideToken(text);

			parserContext.setCurPosition(maxEndIndex);
		}

		return overrideToken;
	}

	private CommentToken getCommentToken(ParserContext parserContext)
	{
		String JAVADOC_COMMENT_START = "/**";
		String JAVADOC_COMMENT_END = "*/";
		CommentToken comment = null;

		StringBuilder content = parserContext.getContent();
		int startIndex = content.indexOf(JAVADOC_COMMENT_START,
				parserContext.getCurPos());
		if(startIndex > -1)
		{
			int endIndex = content.indexOf(JAVADOC_COMMENT_END,
					startIndex + JAVADOC_COMMENT_START.length());
			String javadoc = content.substring(startIndex,
					endIndex + JAVADOC_COMMENT_END.length()).trim();
			comment = new CommentToken(javadoc);
			parserContext
					.setCurPosition(endIndex + JAVADOC_COMMENT_END.length());
		}

		return comment;
	}

	private FileToken getFileToken(ParserContext parserContext)
	{
		FileToken fileToken = null;
		String fileName = parserContext.getFileName();
		StringBuilder content = parserContext.getContent();

		int index = content.indexOf("package");
		if(index >= 0)
		{
			int eolIndex = content.indexOf(CRLF, index);
			String packageLine = content.substring(index, eolIndex-1).trim();
			String packageName = packageLine.split(" ")[1];
			fileToken = new FileToken(packageName, fileName);

			// update the parsing context
			parserContext.setCurPosition(eolIndex);
		}

		return fileToken;
	}

	/**
	 * Trace the msg to a PrintStream Provides the method for tests to report
	 * status
	 * 
	 * @param msg
	 */
	protected void trace(String msg)
	{
		m_stream.println("    " + msg);
	}

	private JavadocTestResults m_testResult;

	// List of fully qualified file names to process
	// private List<String> m_sourceFilesList;
	private List<FileTestData> m_testFiles;

	// Required to build the fully qualified path the requested files
	private String m_testeName;

	protected PrintStream m_stream;

	// parses a method declaration into visibility, return type, method name,
	// and a single parameter
//	private static String ZEROPARAM = "(\\w*) ([a-zA-Z]\\w*)(<?\\w*>?) (\\w*)(\\(\\))";// "\\w*
																						// \\w*
																						// \\w*\\(\\)";
//	private static String ONEPARAM = "(\\w*) ([a-zA-Z]\\w*)(<?\\w*>?) (\\w*)(\\(\\w* \\w*\\))";// "\\w*
																								// \\w*
																								// \\w*\\(\\w*
																								// \\w*\\)";
//	private static String TWOPARAM = "(\\w*) ([a-zA-Z]\\w*)(<?\\w*>?) (\\w*)(\\(([a-zA-Z]\\w*)(<?\\w*>?) \\w*, ([a-zA-Z]\\w*)(<?\\w*>?) \\w*\\))";// "\\w*
																																					// \\w*
																																					// \\w*\\((\\w*
																																					// \\w*)[,]?
																																					// (\\w*
																																					// \\w*)\\)";
//	private static String THREEPARAM = "(\\w*) ([a-zA-Z]\\w*)(<?\\w*>?) (\\w*)(\\(([a-zA-Z]\\w*)(<?\\w*>?) \\w*, ([a-zA-Z]\\w*)(<?\\w*>?) \\w*, ([a-zA-Z]\\w*)(<?\\w*>?) \\w*\\))";
	private static String CRLF = System.getProperty("line.separator");
	private static String LF = "\n";
//	private static String MISSING_AUTHOR_TAG = " ** Code missing @author tag";
//	private static String INVALID_MAIN = " ** Code has unexpected void main method";
//	private static String INVALID_PKG = " ** Invalid package - expected %s, actual: %s";
}

class ParserContext
{
	ParserContext(String fileName, int curPos, StringBuilder content)
	{
		m_fileName = fileName;
		m_curPos = curPos;
		m_content = content;
	}

	public String getFileName()
	{
		return m_fileName;
	}

	public int getCurPos()
	{
		return m_curPos;
	}

	/**
	 * Update the current position as we move along
	 * 
	 * @param newPos
	 *            new current position
	 */
	public void setCurPosition(int newPos)
	{
		m_curPos = newPos;
	}

	public StringBuilder getContent()
	{
		return m_content;
	}

	String m_fileName;
	StringBuilder m_content;
	int m_curPos = 0; // analysis character position
}

class MethodToken
{
	MethodToken(String name, OverrideToken override, CommentToken comment,
			String visiblity, String returnType,
			List<ParameterToken> parameters)
	{
		m_name = name;
		m_comment = comment;
		m_override = override;
		m_visibiity = visiblity;
		m_returnType = returnType;
		m_parameters = parameters;
	}

	public String getName()
	{
		return m_name;
	}

	public String getVisibility()
	{
		return m_visibiity;
	}

	public String getReturnType()
	{
		return m_returnType;
	}

	public CommentToken getComment()
	{
		return m_comment;
	}

	public List<ParameterToken> getParameters()
	{
		return m_parameters;
	}

	public int getParameterCount()
	{
		return m_parameters.size();
	}

	public OverrideToken geOverride()
	{
		return m_override;
	}

	private String m_name;
	private CommentToken m_comment;
	private OverrideToken m_override;
	private String m_visibiity;
	private String m_returnType;
	private List<ParameterToken> m_parameters;
}

class ParameterToken
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

	private String m_type;
	private String m_name;
}

class ConstructorToken
{
	ConstructorToken(String name, CommentToken comment, String visibility)
	{
		m_name = name;
		m_comment = comment;
		m_visibiity = visibility;
	}

	public String getName()
	{
		return m_name;
	}

	public CommentToken getComment()
	{
		return m_comment;
	}

	public String getVisibility()
	{
		return m_visibiity;
	}

	private String m_name;
	private CommentToken m_comment;
	private String m_visibiity;
}

class ClassToken
{
	ClassToken(String className, CommentToken comment)
	{
		m_comment = comment;
		m_className = className;
	}

	public String getClassName()
	{
		return m_className;
	}

	public CommentToken getComment()
	{
		return m_comment;
	}

	public List<MethodToken> getMethodTokens()
	{
		return m_methods;
	}

	public List<ConstructorToken> getConstructorTokens()
	{
		return m_constructors;
	}

	public void setConstructors(List<ConstructorToken> constructors)
	{
		m_constructors = constructors;
	}

	public void setMethods(List<MethodToken> methods)
	{
		m_methods = methods;
	}

	private String m_className;
	private CommentToken m_comment;
	// private OverrideToken m_override;
	private List<ConstructorToken> m_constructors;
	private List<MethodToken> m_methods;
}

class OverrideToken
{
	OverrideToken(String override)
	{
		m_override = override;
	}

	public String geOverride()
	{
		return m_override;
	}

	private String m_override;
}

class CommentToken
{
	CommentToken(String javadoc)
	{
		m_javadoc = javadoc;
	}

	public String getJavadoc()
	{
		return m_javadoc;
	}

	private String m_javadoc;
}

class FileToken
{
	FileToken(String packageName, String fileName)
	{
		m_packageName = packageName;
		m_fileeName = fileName;
	}

	public String getPackageName()
	{
		return m_packageName;
	}

	public ClassToken getClassToken()
	{
		return m_classToken;
	}

	public String getFileName()
	{
		return m_fileeName;
	}

	public void setClassToken(ClassToken classToken)
	{
		m_classToken = classToken;
	}

	String m_packageName;
	String m_fileeName;
	ClassToken m_classToken;
}
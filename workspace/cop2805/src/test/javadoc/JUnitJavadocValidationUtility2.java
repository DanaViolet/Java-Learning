package test.javadoc;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import test.TestResult;
import test.parser.JavaParser;
import test.parser.tokens.ClassToken;
import test.parser.tokens.CommentToken;
import test.parser.tokens.CommentToken.ParameterInfo;
import test.parser.tokens.ConstructorToken;
import test.parser.tokens.FileToken;
import test.parser.tokens.FunctionToken;
import test.parser.tokens.MethodToken;
import test.parser.tokens.ParameterToken;

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
public class JUnitJavadocValidationUtility2
{

	public JUnitJavadocValidationUtility2(String testeNam,
			List<FileTestData2> testFiles)
	{
		m_testeName = testeNam;
		m_testFiles = testFiles;
		m_stream = System.out; // Standard out
		m_testResult = new JavadocTestResults();
	}

	/**
	 * Name provided for this test
	 * 
	 * @return the name of the test
	 */
	public String getTestName()
	{
		return m_testeName;
	}

	/**
	 * Sets flag for allowing a main method in the file to true
	 */
	public void allowMainMethod()
	{
		m_allowMain = true;
	}

	/**
	 * True disables parser traces, false displays parser tracing
	 * 
	 * @param suppress
	 *            true to suppress, otherwise false;
	 */
	public void suppressParserTrace(boolean suppress)
	{
		m_suppressParserTrace = suppress;
	}
	
	/**
	 * True disables validation traces, false displays validation tracing
	 * 
	 * @param suppress true to suppress, otherwise false
	 */
	public void suppressValidationTrace(boolean suppress)
	{
		m_suppressValidationTrace = suppress;
	}

	/**
	 * Execute the test
	 * 
	 * @return The TestResult object
	 */
	public TestResult runTest()
	{
		trace("Running " + getTestName());
		m_testResult.setPassed();
		boolean result = true;
		for(FileTestData2 testFile : m_testFiles)
		{
			trace(" -- Running test: " + testFile.getFileName());
			try
			{
				if(!validateJavadoc(testFile))
				{
					result = false;// && validateJavadoc(testFile);
				}
			}
			catch(Exception ex)
			{
				trace("******** Error processing " + testFile.getFileName()
						+ " ********");
				trace(ex.getMessage());
				trace("*****************************************************");
				result = false;
			}
		}

		m_testResult.setResult(result);
		trace("Completed running " + getTestName());
		return m_testResult;
	}

	/**
	 * Set the list of expected method results.
	 * 
	 * @param list
	 *            List of files to evaluate
	 */
	public void setTestFiles(List<FileTestData2> list)
	{
		m_testFiles = list;
	}

	/**
	 * Validates @author has been inserted into code and no void main is
	 * contained in the file
	 * 
	 * @return true if code is properly implemented
	 */
	private boolean validateJavadoc(FileTestData2 testFile)
	{
		// trace(" -- validateCode --");
		boolean result = true;

		String codeFilePath = "";
		try
		{
			codeFilePath = initFilePath(testFile);
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

					trace("parsing ... ");
					JavaParser parser = new JavaParser();
					parser.suppressTrace(m_suppressParserTrace);
					FileToken fileToken = parser.parse(srcFile);

					trace("** evaluating ... ");
					try
					{
						result = evaluateFileToken(fileToken, testFile);
					}
					catch(Exception ex)
					{
						trace(" -- error evaluating: " + ex.getMessage());
						result = false;
					}
					trace("** evaluating complete ");
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
	private String initFilePath(FileTestData2 testFile) throws IOException
	{
		String filePath = "";
		String fileName = testFile.getFileName();
		String packageName = testFile.getPackageName();
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

			String packagePath = buildPathFromPackage(packageName);
			filePath = srcFolder.getCanonicalPath() + File.separator
					+ packagePath + fileName;// File.separator + fileName;
		}

		return filePath;
	}

	/**
	 * Packages names that are sub-packages (e.g. week01.app) need to be parsed
	 * and changed into folder paths to create a valid file path. If the package
	 * name has no sub-packages, it is returned unmodified. Used by @see
	 * initFilePath
	 * 
	 * @param packageName
	 *            The package name to change into a path.
	 * @return
	 */
	private String buildPathFromPackage(String packageName)
	{
		String pathSeparator = File.separator;
		String normalizedPath = packageName + File.separator; // assume no
																// subpackages
		String[] packages = packageName.split("\\.");
		if(packages.length > 1)
		{
			StringBuilder builder = new StringBuilder();
			// build the path
			for(String subpackage : packages)
			{
				builder.append(subpackage);
				builder.append(pathSeparator);
			}

			normalizedPath = builder.toString();
		}

		return normalizedPath;

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

	private boolean evaluateFileToken(FileToken fileToken,
			FileTestData2 testFile)
	{
		boolean fileNameMatch = verifyFileNameMatch(fileToken, testFile);
		boolean fileNameClassNameMatch = verifyFileNameClassNameMatch(
				fileToken);
		boolean hasClassComment = verifyClassComment(
				fileToken.getClassToken().getComment(), fileToken);
		boolean correctPackage = verifyPackage(fileToken, testFile);
		boolean validAuthorTag = verifyAuthorTag(fileToken);
		boolean fileNoMain = verifyMain(fileToken);
		boolean methodsMatch = verifyMethods(fileToken, testFile);
		;
		boolean validConstructors = verifyConstructors(fileToken, testFile);

		return fileNameMatch && fileNoMain && fileNameClassNameMatch
				&& hasClassComment && methodsMatch && correctPackage
				&& validConstructors && validAuthorTag;
	}

	private boolean verifyFileNameMatch(FileToken fileToken,
			FileTestData2 testFile)
	{
		trace("  -- filename verification");
		String expectedFileName = testFile.getFileName();
		String actualFileName = fileToken.getFileName();

		boolean fileNameMatch = expectedFileName.equals(actualFileName);
		if(!fileNameMatch)
		{
			// no name match
			String msg = String.format(
					"    -- File name mismatch - expected: %s, actual: %s",
					expectedFileName, actualFileName);
			updateResultStatus("file name validation", msg);
		}

		return fileNameMatch;
	}

	private boolean verifyMain(FileToken fileToken)
	{
		trace("  -- void main verification");
		boolean fileNoMain = true;
		if(fileToken.hasMain() && !m_allowMain)
		{
			String msg = "    -- " + fileToken.getFileName() + ": "
					+ INVALID_MAIN + CRLF;
			updateResultStatus("file validation", msg);
			fileNoMain = false;
		}
		return fileNoMain;
	}

	private boolean verifyAuthorTag(FileToken fileToken)
	{
		trace("  -- @author verification");
		boolean hasAuthor = true;
		if(!fileToken.hasAuthor())
		{
			String msg = "    -- " + fileToken.getClassToken().getClassName()
					+ " comment " + MISSING_AUTHOR_TAG + CRLF;
			updateResultStatus("class comment validation", msg);
			hasAuthor = false;
		}

		return hasAuthor;
	}

	private boolean verifyPackage(FileToken fileToken, FileTestData2 testFile)
	{
		trace("  -- package verification");
		String expectedPackageName = testFile.getPackageName();
		String actualPackageName = trimSemiColon(fileToken.getPackageName());
		boolean correctPackage = expectedPackageName.equals(actualPackageName);
		if(!correctPackage)
		{
			String msg = String.format(
					"    -- " + "class "
							+ fileToken.getClassToken().getClassName()
							+ " Package mismatch - expected: %s, actual: %s",
					expectedPackageName, actualPackageName) + CRLF;
			updateResultStatus("package validation", msg);
		}

		return correctPackage;
	}

	/**
	 * Trim the closing semi-colon on the package name
	 * 
	 * @param packageName
	 *            package name to remove semi-colon from
	 * @return package name without the ending semi-colon
	 */
	private String trimSemiColon(String packageName)
	{
		if(packageName.endsWith(";"))
		{
			packageName = packageName.substring(0, packageName.length() - 1);
		}

		return packageName;
	}

	private boolean verifyMethods(FileToken fileToken, FileTestData2 testFile)
	{
		boolean methodsMatch = true;
		trace("  -- method verification");

		ClassToken classToken = fileToken.getClassToken();
		List<MethodTestData2> expectedMethodsList = testFile
				.getExpectedMethods();

		// verify the parsed methods match the expected methods
		for(MethodTestData2 expectedMethod : expectedMethodsList)
		{
			// find the corresponding method
			MethodToken methodToken = classToken.findMethodTokenByName(
					expectedMethod.getMethodName(),
					expectedMethod.getParameterTypes());

			if(methodToken != null)
			{
				// String methodName = methodToken.getName();
				boolean nameMatch = true;
				boolean visbilityMatch = methodToken.getVisibility()
						.equals(expectedMethod.getVisibility());
				boolean returnTypeMatch = methodToken.getReturnType()
						.equals(expectedMethod.getReturnType());
				boolean validComment = verifyMethodComment(classToken,
						methodToken);
				String expectedTypes = expectedMethod.getParameterTypes();
				String actualTypes = methodToken.getParameterTypes();
				boolean parameterCountMatch = actualTypes.equals(expectedTypes);

				boolean currentMethod = nameMatch && visbilityMatch
						&& returnTypeMatch && validComment
						&& parameterCountMatch;
				methodsMatch = methodsMatch && currentMethod;
				if(!currentMethod)
				{
					StringBuilder msg = new StringBuilder();
					if(!visbilityMatch)
					{
						msg.append(String.format(
								"    -- %s: Visibility mismatch - expected: %s, actual: %s",
								expectedMethod.getMethodName(),
								expectedMethod.getVisibility(),
								methodToken.getVisibility()));
						msg.append(CRLF);

						methodsMatch = false;
					}

					if(!returnTypeMatch)
					{
						msg.append(String.format(
								"    -- %s: Return type mismatch - expected: %s, actual: %s",
								expectedMethod.getMethodName(),
								expectedMethod.getReturnType(),
								methodToken.getReturnType()));
						msg.append(CRLF);

						methodsMatch = false;
					}

					if(!parameterCountMatch)
					{
						msg.append(String.format(
								"    -- %s: Parameter count mismatch - expected: %s, actual: %s",
								expectedMethod.getMethodName(),
								expectedMethod.getParameterTypes(),
								methodToken.getParameterTypes()));
						msg.append(CRLF);

						methodsMatch = false;
					}

					updateResultStatus("method validation", msg.toString());
				}
			}
			else
			{
				// no name match
				String msg = String
						.format("    -- ** %s: Missing method - %s **",
								fileToken.getClassToken().getClassName(),
								expectedMethod.getMethodName() + "("
										+ expectedMethod.getParameterTypes()
										+ ")");
				updateResultStatus("method validation", msg);
				methodsMatch = false;
			}
		}

		return methodsMatch;
	}

	private boolean verifyConstructors(FileToken fileToken,
			FileTestData2 testFile)
	{
		boolean isValid = true;
		StringBuilder msg = new StringBuilder();
		try
		{
			trace("  -- constructor verification");
			List<ConstructorTestData> expectedConstructorsList = testFile
					.getExpectedConstructors();
			ClassToken classToken = fileToken.getClassToken();

			for(ConstructorTestData expectedConstructor : expectedConstructorsList)
			{
				String name = expectedConstructor.getName();
				int paramCount = expectedConstructor.getParameterCount();

				ConstructorToken constructorToken = classToken
						.findConstructorTokenByName(name, paramCount);

				if(constructorToken != null)
				{
					// String methodName = methodToken.getName();
					boolean nameMatch = true;

					isValid = isValid && nameMatch;

					boolean visbilityMatch = constructorToken.getVisibility()
							.equals(expectedConstructor.getVisibility());

					if(!visbilityMatch)
					{
						msg.append(String.format(
								"    -- %s - Visibility mismatch - expected: %s, actual: %s\n",
								expectedConstructor.getName(),
								expectedConstructor.getVisibility(),
								constructorToken.getVisibility()));
						updateResultStatus("constructor validation",
								msg.toString());
					}

					isValid = isValid && visbilityMatch;

					// TODOD
					boolean validComment = verifyMethodComment(
							fileToken.getClassToken(), constructorToken);

					isValid = isValid && validComment;
				}
				else
				{
					// expected constructor not found
					msg.append(String.format(
							"    -- expected constructor - name: %s, param count: %d\n",
							name, paramCount));
					updateResultStatus("constructor validation",
							msg.toString());
					isValid = false;
				}
			}
		}
		catch(Exception ex)
		{

		}

		return isValid;
	}

	private void updateResultStatus(String description, String msg)
	{

		JavadocTestResultDetail detail = new JavadocTestResultDetail(
				description);
		detail.addResultDetail(msg);
		// trace(" -- updateResultStatus " + msg);
		m_testResult.addTestResultDetail(detail);
	}

	private boolean verifyFileNameClassNameMatch(FileToken fileToken)
	{
		trace("  -- Classname verification");
		String className = fileToken.getClassToken().getClassName();
		String fileName = fileToken.getFileName();
		int index = fileName.indexOf(".");
		String fileNameOnly = fileName.substring(0, index);
		boolean match = className.equals(fileNameOnly);
		if(!match)
		{
			String msg = String.format(
					"    -- Class name / File name mismatch - expected %s",
					className);
			updateResultStatus("class name / file name validation", msg);
		}

		return match;
	}

	/**
	 * Verifying the parameters of the function match the parameters of the
	 * comment
	 * 
	 * @param classToken
	 * @param token
	 * @param parameterList
	 * @return
	 */
	private boolean validateParamerters(ClassToken classToken,
			FunctionToken token)// ,
	// List<ParameterToken> parameterList)
	{
		boolean validParams = true;
		boolean foundParam = true;
		StringBuilder msg = new StringBuilder();

		CommentToken comment = token.getComment();
		String javadoc = comment.getJavadoc();
		// method parameters
		List<ParameterToken> parameterList = token.getParameters();
		int expectedCount = parameterList.size();
		int actualCount = token.getParameterCount();
		boolean parameterCountMatch = expectedCount == actualCount;

		String name = token.getName();
		if(!parameterCountMatch)
		{
			msg.append(String.format(
					"    -- %s: s %s Parameter count mismatch - expected: %d, actual: %d",
					classToken.getClassName(), name, expectedCount,
					actualCount));
			msg.append(CRLF);
		}

		for(ParameterToken param : parameterList)
		{
			// see if the method parameter is in the comment parameters
			// must be an exact match
			foundParam = findParameterMatchInComment(param.getName(), comment);
			if(!foundParam)
			{
				String errMsg = String.format(
						"  -- %s: Expected @param missing from %s: param %s",
						classToken.getClassName(), token.getName(),
						param.getName());
				msg.append(errMsg);
				updateResultStatus("parameter validation", errMsg);

			}
			validParams = validParams && foundParam;
		}

		return validParams;
	}

	private boolean findParameterMatchInComment(String methodParameterName,
			CommentToken commentToken)
	{
		boolean result = false;

		for(ParameterInfo commentParameterInfo : commentToken.getParameters())
		{
			if(methodParameterName.equals(commentParameterInfo.m_name))
			{
				result = true;
			}
		}

		return result;
	}

	private boolean validateReturnType(ClassToken classToken, MethodToken token)
	{
		boolean validReturn = true;

		String returnType = token.getReturnType();
		CommentToken comment = token.getComment();
		String javadoc = comment.getJavadoc();

		if(!returnType.equals("void"))
		{
			String lookFor = "@return";
			boolean foundReturn = javadoc.contains(lookFor);
			if(!foundReturn)
			{
				String msg = String.format(
						"   -- ** %s: Expected @return missing from method %s **",
						classToken.getClassName(), token.getName());
				updateResultStatus("method param validation", msg);
			}
			validReturn = validReturn && foundReturn;
		}

		return validReturn;
	}

	private boolean verifyMethodComment(ClassToken classToken,
			FunctionToken token)
	{
		boolean hasCommentTest = false;
		boolean validParams = false;
		boolean validReturn = true;
		boolean validComment = false;

		// verify a comment with description exists and any parameters
		// have a @param and @return as appropriate
		CommentToken comment = token.getComment();
		if(comment != null)
		{
			validComment = validateComment(classToken, token.getName(),
					comment);
			String javadoc = comment.getJavadoc();
			List<ParameterToken> parameterList = token.getParameters();

			hasCommentTest = javadoc.length() > 0;
			validParams = validateParamerters(classToken, token);// ,
																	// parameterList);

			if(token instanceof MethodToken)
			{
				MethodToken mToken = (MethodToken)token;
				validReturn = validateReturnType(classToken, mToken);
			}
		}
		else
		{
			String msg = String.format(
					"   -- %s: missing method comment for %s",
					classToken.getClassName(), token.getName());
			updateResultStatus("comment validation", msg);
		}

		return hasCommentTest && validParams && validReturn && validComment;
	}

	private boolean validateComment(ClassToken classToken, String parentName,
			CommentToken comment)
	{
		boolean hasDescription = comment.getDescription().length() > 0 ? true
				: false;
		boolean validReturn = true;
		boolean validAuthor = true;
		boolean parameterDescriptions = true;

		// Check that the identified parameters have descriptions
		for(CommentToken.ParameterInfo info : comment.getParameters())
		{
			if(info.m_description.length() == 0)
			{
				String msg = String.format(
						"   -- ** %s: %s  parameter '%s' has no description **",
						classToken.getClassName(), parentName, info.m_name);
				// trace(msg);
				updateResultStatus("comment validation", msg);
				parameterDescriptions = false;
			}
		}

		if(!hasDescription)
		{
			// String msg = " -- ** " + parentName + ": method/constructor has
			// no description **";
			String msg = String.format(
					"   -- ** %s: %s  method/constructor has no description **",
					classToken.getClassName(), parentName);
			updateResultStatus("comment validation", msg);
		}

		if(comment.hasReturn())
		{
			if(comment.getReturn().length() == 0)
			{
				// String msg = " -- ** " + parentName + ": @return has no
				// description **";
				String msg = String.format(
						"   -- ** %s: %s  @return has no description **",
						classToken.getClassName(), parentName);
				updateResultStatus("comment validation", msg);
				validReturn = false;
			}
		}

		if(comment.hasAuthor())
		{
			if(comment.getAuthor().length() == 0)
			{
				// String msg = " -- ** " + parentName + ": @author has no
				// description **";
				String msg = String.format(
						"   -- ** %s: %s  @author has no description **",
						classToken.getClassName(), parentName);
				updateResultStatus("comment validation", msg);
				validAuthor = false;
			}
		}

		boolean result = hasDescription && validReturn && validAuthor
				&& parameterDescriptions;

		return result;
	}

	private boolean verifyClassComment(CommentToken comment,
			FileToken fileToken)
	{
		boolean hasClassComment = comment.getJavadoc().length() > 0;
		String javadoc = comment.getJavadoc();
		boolean hasAuthor = javadoc.indexOf("@author") > 0 ? true : false;
		fileToken.setAuthor(hasAuthor);
		return hasClassComment && hasAuthor;
	}

	/**
	 * Trace the msg to a PrintStream Provides the method for tests to report
	 * status
	 * 
	 * @param msg message to trace
	 */
	protected void trace(String msg)
	{
		if(!m_suppressValidationTrace)
		{
			m_stream.println("    " + msg);
		}
	}

	private JavadocTestResults m_testResult;

	// List of fully qualified file names to process
	private List<FileTestData2> m_testFiles;

	// Required to build the fully qualified path the requested files
	private String m_testeName;

	protected PrintStream m_stream;
	private boolean m_allowMain = false;
	private boolean m_suppressParserTrace = false;
	private boolean m_suppressValidationTrace = false;
	private static String CRLF = System.getProperty("line.separator");
	private static String MISSING_AUTHOR_TAG = " ** Code missing @author tag";
	private static String INVALID_MAIN = " ** Code has unexpected void main method";

}

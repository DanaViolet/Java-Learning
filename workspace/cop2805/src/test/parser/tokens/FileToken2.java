package test.parser.tokens;

/**
 * Encapsulates a source file
 * 
 * @author Scott LaChance
 */
public class FileToken2
{
	/**
	 * Represents the source file attributes.
	 * 
	 * @param fileName Name of the source file
	 */
	public FileToken2(String fileName)
	{
		// use pessimistic defaults
		this("", fileName, false, true);
	}

	/**
	 * Represents the source file attributes.
	 * 
	 * @param packageName The package name where the file resides
	 * @param fileName Name of the source file
	 */
	public FileToken2(String packageName, String fileName)
	{
		// use pessimistic defaults
		this(packageName, fileName, false, true);
	}

	/**
	 * Represents the source file attributes.
	 * 
	 * @param packageName The package name where the file resides
	 * @param fileName Name of the source file
	 * @param hasAuthor true if the @author attribute is present
	 * @param hasMain true if the source has a main method
	 */
	public FileToken2(String packageName, String fileName, boolean hasAuthor,
			boolean hasMain)
	{
		m_packageName = packageName;
		m_fileeName = fileName;
		m_hasAuthor = hasAuthor;
		m_hasMain = hasMain;
	}

	public String getPackageName()
	{
		return m_packageToken.getPackageName();
	}

	public ClassToken2 getClassToken()
	{
		return m_classToken;
	}

	public PackageToken getPackageToken()
	{
		return m_packageToken;
	}

	public String getFileName()
	{
		return m_fileeName;
	}

	public void setClassToken(ClassToken2 classToken)
	{
		m_classToken = classToken;
	}

	public void setPackageToken(PackageToken packageToken)
	{
		m_packageToken = packageToken;
	}

	/**
	 * Sets whether the file has an @author tag
	 * 
	 * @param hasAuthor
	 *            true if @author exists, otherwise false
	 */
	public void setAuthor(boolean hasAuthor)
	{
		m_hasAuthor = hasAuthor;
	}

	public boolean hasAuthor()
	{
		return m_hasAuthor;
	}

	public boolean hasMain()
	{
		MethodToken mainMethod = m_classToken.findMethodTokenByName("main", 1);

		return mainMethod != null;
	}

	@Override
	public String toString()
	{
		String fmt = String.format("%s\nMain: %s\nPackage: %s\nClass: %s",
				m_fileeName, m_hasMain, m_packageToken.toString(),
				m_classToken.toString());

		return fmt;
	}

	String m_packageName;
	String m_fileeName;
	boolean m_hasAuthor;
	boolean m_hasMain;
	ClassToken2 m_classToken;
	PackageToken m_packageToken;
}

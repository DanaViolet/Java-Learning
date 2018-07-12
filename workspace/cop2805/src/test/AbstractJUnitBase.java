package test;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import test.javadoc.JUnitJavadocValidationUtility2;

public class AbstractJUnitBase
{
	
	protected File getBinFolder()
	{
		File curDir = new File(".");
		File curDir1 = new File(curDir.getAbsolutePath());
		
		trace("Init file path " + curDir1.getAbsolutePath());
		trace(" - cur dir: " + curDir1.getName());
		trace(" - parent dir: " + curDir1.getParent());
		
		String bin = curDir1.getParentFile().getName();
		boolean b = bin.equals("bin");
		boolean exists = curDir1.exists();
		boolean isDir = curDir1.isDirectory();
		trace("exists - " + exists + ", isDir - " + isDir + ", " + bin + ", "
				+ b);
		
		return curDir1;
	}
	
	/**
	 * Setup the test file path. Dev environment and runtime environment have
	 * different current directory behavior. For runtime testing using this test
	 * framework, it is the bin folder.
	 * For development in Eclipse, it is the current project folder. The test will
	 * create a new File instance for the current directory. If the directory is
	 * bin, the we use the simple file name for the test If it isn't we add the 
	 * the pathAdjustment to the file path. That way this will work in
	 * development and testing.
	 * 
	 * @param fileName - the file name with the extension
	 * @param pathAdjustment - the project folder to inject into the path. Used when testing from cmd line
	 * @throws IOException on error
	 */
	protected String initFilePath(String fileName, String pathAdjustment) throws IOException
	{
		String filePath = "";
		String targetFile = "\\" + fileName;
		String altTargetFile = "\\" + pathAdjustment + targetFile;
//		File curDir = new File(".");
		File curDir1 = getBinFolder();//new File(curDir.getAbsolutePath());
		trace("Init file path " + curDir1.getAbsolutePath());
		trace(" - cur dir: " + curDir1.getName());
		trace(" - parent dir: " + curDir1.getParent());
		trace(" - curDir.getParentFile() " + (null == curDir1.getParentFile()
				? "null" : curDir1.getParentFile().getName()));
		boolean exists = curDir1.exists();
		boolean isDir = curDir1.isDirectory();
		String bin = curDir1.getParentFile().getName();
		boolean b = bin.equals("bin");
		trace("exists - " + exists + ", isDir - " + isDir + ", " + bin + ", "
				+ b);
		File srcFolder = null;
		if(exists && isDir && b)
		{
			// get the src folder
			File binParent = curDir1.getParentFile().getParentFile();
			File[] fileList = binParent.listFiles();
			srcFolder = findSrcFolder(fileList);
			filePath = srcFolder.getCanonicalPath() + targetFile;
		}
		else
		{
			File[] fileList = curDir1.listFiles();
			srcFolder = findSrcFolder(fileList);
			
			filePath = srcFolder.getCanonicalPath() + altTargetFile;//"\\week08\\FermiGame.java";
		}

		return filePath;
	}
	
	/**
	 * This method looks for the src folder in a file hierarchy
	 * 
	 * @param fileList file path (folders)
	 * @return The File representing the src folder
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
	
    /**
     * Sets the suppression flag
     *
     * @param suppress
     *            true to suppress traces, otherwise false
     */
    protected void setSuppress(boolean suppress)
    {
        m_suprress = suppress;
    }

    /**
     * Trace the msg to a PrintStream Provides the method for tests to report
     * status
     *
     * @param msg
     */
    protected void trace(String msg)
    {
        if(!m_suprress)
        {
            interalTrace(msg);
        }
    }

    protected void traceError(String msg)
    {
        interalTrace(msg);
    }

    protected void traceResult(String msg)
    {
        interalTrace(msg);
    }

    private void interalTrace(String msg)
    {
        if(m_stream == null)
        {
            System.out.println(msg);
        }
        else
        {
            m_stream.println(msg);
        }
    }

    protected boolean m_suprress = false; // default to no suppression
    protected PrintStream m_stream;
    protected JUnitJavadocValidationUtility2 m_validator;
    protected static String CRLF = System.getProperty("line.separator");
}

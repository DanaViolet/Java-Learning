package week01;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import test.TestResult;
import test.TestResultDetail;
import test.javadoc.FileTestData2;
import test.javadoc.JUnitJavadocValidationUtility2;
import test.javadoc.MethodTestData2;

/**
 * Tests the Javadoc in the source file.
 *
 * @author Scott
 *
 */
public class JUnitJavadocValidation
{
    public JUnitJavadocValidation()
    {
        m_stream = System.out; // Standard out
        List<FileTestData2> testFiles = new ArrayList<FileTestData2>();
        List<MethodTestData2> methods = new ArrayList<MethodTestData2>();

        methods.add(new MethodTestData2("getSize", "", "int","public"));
        methods.add(new MethodTestData2("isEmpty", "", "boolean","public"));
        methods.add(new MethodTestData2("peek", "", "T","public"));
        methods.add(new MethodTestData2("pop", "", "T","public"));
        methods.add(new MethodTestData2("push", "T", "void","public"));

        testFiles.add(new FileTestData2("week01", "GenericStack.java", methods));

        methods.add(new MethodTestData2("getSize", "", "int","public"));
        methods.add(new MethodTestData2("isEmpty", "", "boolean","public"));
        methods.add(new MethodTestData2("peek", "", "T","public"));
        methods.add(new MethodTestData2("pop", "", "T","public"));
        methods.add(new MethodTestData2("push", "T", "void","public"));

        testFiles.add(new FileTestData2("week01", "GenericStack2.java", methods));

        m_validator = new JUnitJavadocValidationUtility2("Week01 Javadoc Test",
                testFiles);


        m_validator.suppressParserTrace(true);
    }

    @Test
    public void testJavadoc()
    {
        trace("** Validating Javadoc **");

        // Update these values for each assignment
        // m_packageName = "week02";
        TestResult result = m_validator.runTest();
        StringBuilder details = new StringBuilder();
        if(!result.passed())
        {
            List<TestResultDetail> detailList = result.getTestResultDetails();
            for(TestResultDetail detail : detailList)
            {
                trace(detail.testDetails());
                details.append(detail.testDetails());
                details.append(CRLF);
            }
        }
        trace(String.format("** Test result: %s **", result.passed() ? "Passed" : "Failed"));
        assertTrue(details.toString(), result.passed());
    }

    /**
     * Trace the msg to a PrintStream Provides the method for tests to report
     * status
     *
     * @param msg
     */
    private void trace(String msg)
    {
        m_stream.println(msg);
    }

    private JUnitJavadocValidationUtility2 m_validator;
    protected PrintStream m_stream;
    private static String CRLF = System.getProperty("line.separator");
}

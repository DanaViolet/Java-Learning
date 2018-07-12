package week04;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import test.javadoc.JUnitJavadocValidationUtility2;
import test.javadoc.MethodTestData2;
import test.AbstractJUnitBase;
import test.TestResult;
import test.TestResultDetail;
import test.javadoc.FileTestData2;

/**
 * Tests the Javadoc in the source file.
 * 
 * @author Scott
 *
 */
public class JUnitJavadocValidation extends AbstractJUnitBase
{
	public JUnitJavadocValidation()
	{

		m_stream = System.out; // Standard out

		List<FileTestData2> testFiles = new ArrayList<FileTestData2>();

		List<MethodTestData2> methods = new ArrayList<MethodTestData2>();

		// Employee.java
		methods.add(new MethodTestData2("countWordOccurrences", "BufferedReader",				                        
				"List<WordCountResult>", "public"));
		testFiles.add(new FileTestData2("week04", "CountUtility.java", methods));

		// DataLayer.java
		List<MethodTestData2> dlMethods = new ArrayList<MethodTestData2>();
		dlMethods
				.add(new MethodTestData2("incrementCount", "", "void", "public"));
		dlMethods.add(new MethodTestData2("getCount", "", "int", "public"));
		dlMethods.add(new MethodTestData2("getWord", "", "String", "public"));
		dlMethods.add(new MethodTestData2("setCount", "int", "void", "public"));
		dlMethods.add(new MethodTestData2("setWord", "String", "void", "public"));
		dlMethods.add(new MethodTestData2("toString", "", "String", "public"));
		testFiles.add(
				new FileTestData2("week04", "WordCountResult.java", dlMethods));

		m_validator = new JUnitJavadocValidationUtility2("Week04 Javadoc Test",
				testFiles);
		m_validator.suppressParserTrace(true);
	}

	@Test
	public void testJavadoc()
	{
		trace("** Validating Javadoc **");

		// Update these values for each assignment
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
		trace(String.format("** Test result: %s **",
				result.passed() ? "Passed" : "Failed"));
		assertTrue(details.toString(), result.passed());
	}	
}

package test;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import test.javadoc.JUnitJavadocValidationUtility2;
import test.javadoc.MethodTestData2;
import test.TestResult;
import test.TestResultDetail;
import test.javadoc.ConstructorTestData;
import test.javadoc.FileTestData;
import test.javadoc.FileTestData2;

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

		trace("########### Initializing JUnitJavadocValidation for week03 ###########");
		List<FileTestData2> testFiles = new ArrayList<FileTestData2>();

		// Fibonacci.java
//		List<MethodTestData> fibMethods = new ArrayList<MethodTestData>();
//		fibMethods.add(new MethodTestData("fib", 1, "long", "public"));
//		List<ConstructorTestData> fibCconstructors = new ArrayList<ConstructorTestData>();
//		List<MethodTestData> methods = new ArrayList<MethodTestData>();	
//		testFiles.add(new FileTestData("test", "Fibonacci.java", fibMethods,
//				fibCconstructors));
		
		// EuclidGcd.java
		List<MethodTestData2> myList = new ArrayList<MethodTestData2>();
		myList.add(new MethodTestData2("add", "E", "void", "public"));
		myList.add(new MethodTestData2("add", "intE", "void", "public"));
		myList.add(new MethodTestData2("clear", "", "void", "public"));
		myList.add(new MethodTestData2("contains", "E", "boolean", "public"));
		myList.add(new MethodTestData2("get", "int", "E", "public"));
		myList.add(new MethodTestData2("indexOf", "E", "int", "public"));
		myList.add(new MethodTestData2("isEmpty", "", "boolean", "public"));
		myList.add(new MethodTestData2("remove", "E", "boolean", "public"));
		myList.add(new MethodTestData2("remove", "int", "E", "public"));
		myList.add(new MethodTestData2("set", "intE", "E", "public"));
		myList.add(new MethodTestData2("size", "", "int", "public"));
		
		List<ConstructorTestData> myListCconstructors = new ArrayList<ConstructorTestData>();
		//myListCconstructors.add(new ConstructorTestData("EuclidGcd", 0, "public"));
		testFiles.add(new FileTestData2("test", "MyList.java", myList,
				myListCconstructors));
		
		List<MethodTestData2> myArrayList = new ArrayList<MethodTestData2>();
		myArrayList.add(new MethodTestData2("add", "intE", "void", "public"));
		myArrayList.add(new MethodTestData2("clear", "", "void", "public"));
		myArrayList.add(new MethodTestData2("contains", "E", "boolean", "public"));
		myArrayList.add(new MethodTestData2("get", "int", "E", "public"));
		myArrayList.add(new MethodTestData2("indexOf", "E", "int", "public"));
		myArrayList.add(new MethodTestData2("iterator", "", "Iterator<E>", "public"));
		myArrayList.add(new MethodTestData2("remove", "int", "E", "public"));
		myArrayList.add(new MethodTestData2("set", "intE", "E", "public"));
		myArrayList.add(new MethodTestData2("toString", "", "String", "public"));
		myArrayList.add(new MethodTestData2("trimToSize", "", "void", "public"));
		
		List<ConstructorTestData> myArrayListCconstructors = new ArrayList<ConstructorTestData>();
		myArrayListCconstructors.add(new ConstructorTestData("MyArrayList", 0, "public"));
		myArrayListCconstructors.add(new ConstructorTestData("MyArrayList", 1, "public"));
		testFiles.add(new FileTestData2("test", "MyArrayList.java", myArrayList,
				myArrayListCconstructors));
		
		List<MethodTestData2> myAbstractArrayList = new ArrayList<MethodTestData2>();
		myAbstractArrayList.add(new MethodTestData2("add", "E", "void", "public"));
		myAbstractArrayList.add(new MethodTestData2("isEmpty", "", "boolean", "public"));
		myAbstractArrayList.add(new MethodTestData2("remove", "E", "boolean", "public"));
		myAbstractArrayList.add(new MethodTestData2("set", "intE", "E", "public"));
		myAbstractArrayList.add(new MethodTestData2("size", "", "int", "public"));
		
		List<ConstructorTestData> myAbstractArrayListCconstructors = new ArrayList<ConstructorTestData>();
		myAbstractArrayListCconstructors.add(new ConstructorTestData("MyAbstractList", 0, "protected"));
		myAbstractArrayListCconstructors.add(new ConstructorTestData("MyAbstractList", 1, "protected"));
		testFiles.add(new FileTestData2("test", "MyAbstractList.java", myAbstractArrayList,
				myAbstractArrayListCconstructors));


//		// Employee.java
//		methods.add(new MethodTestData("equals", 1, "boolean", "public"));
//		methods.add(
//				new MethodTestData("getDisplayName", 0, "String", "public"));
//		methods.add(new MethodTestData("getFirstName", 0, "String", "public"));
//		methods.add(new MethodTestData("getFormattedSalary", 0, "String",
//				"public"));
//		methods.add(new MethodTestData("getLastName", 0, "String", "public"));
//		methods.add(new MethodTestData("getSalary", 0, "double", "public"));
//		methods.add(new MethodTestData("setFirstName", 1, "void", "public"));
//		methods.add(new MethodTestData("setLastName", 1, "void", "public"));
//		methods.add(new MethodTestData("setSalary", 1, "void", "public"));
//		methods.add(new MethodTestData("toString", 0, "String", "public"));
//
//		List<ConstructorTestData> constructors = new ArrayList<ConstructorTestData>();
//		constructors.add(new ConstructorTestData("Employee", 0, "public"));
//		constructors.add(new ConstructorTestData("Employee", 2, "public"));
//		constructors.add(new ConstructorTestData("Employee", 3, "public"));
//
//		testFiles.add(new FileTestData("test", "Employee.java", methods,
//				constructors));

//		methods = new ArrayList<MethodTestData>();

		// DataLayer.java
//		List<MethodTestData> dlMethods = new ArrayList<MethodTestData>();
//		dlMethods.add(new MethodTestData("addEmployee", 1, "void", "public"));
//		dlMethods.add(
//				new MethodTestData("deleteEmployee", 1, "boolean", "public"));
//		dlMethods.add(new MethodTestData("getEmployeeByName", 1, "Employee",
//				"public"));
//		dlMethods.add(new MethodTestData("getEmployees", 0, "List<Employee>",
//				"public"));
//		dlMethods.add(new MethodTestData("getSize", 0, "int", "public"));
//
//		List<ConstructorTestData> dlConstructors = new ArrayList<ConstructorTestData>();
//		dlConstructors.add(new ConstructorTestData("DataLayer", 1, "public"));
//		testFiles.add(new FileTestData("test", "DataLayer.java", dlMethods,
//				dlConstructors));

		// CollectionBusinessLayer.java
//		List<MethodTestData> blMethods = new ArrayList<MethodTestData>();
//		blMethods.add(
//				new MethodTestData("addEmployee", 3, "Employee", "public"));
//		blMethods.add(
//				new MethodTestData("deleteEmployee", 1, "Employee", "public"));
//		blMethods.add(new MethodTestData("changeEmployeeSalary", 2, "boolean",
//				"public"));
//		blMethods.add(new MethodTestData("listAllEmployees", 0,
//				"List<Employee>", "public"));
//		blMethods.add(
//				new MethodTestData("listEmployee", 1, "Employee", "public"));

//		List<ConstructorTestData> blConstructors = new ArrayList<ConstructorTestData>();
//		blConstructors.add(new ConstructorTestData("CollectionBusinessLayer", 0,
//				"public"));
//		blConstructors.add(new ConstructorTestData("CollectionBusinessLayer", 1,
//				"public"));
//		testFiles.add(new FileTestData("test", "CollectionBusinessLayer.java",
//				blMethods, blConstructors));

//		for(FileTestData file : testFiles)
//		{
//			trace(" - testing " + file.getFileName());
//		}
		m_validator = new JUnitJavadocValidationUtility2("Test Javadoc Test",
				testFiles);
		m_validator.suppressParserTrace(true);

		trace("########### Initialization Complete for JUnitJavadocValidation ###########");
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

	/**
	 * Trace the msg to a PrintStream Provides the method for tests to report
	 * status
	 * 
	 * @param msg
	 */
	protected void trace(String msg)
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

	private JUnitJavadocValidationUtility2 m_validator;

	protected PrintStream m_stream;
	// private String m_packageName;
	// private ArrayList<MethodData> m_methods;
	// private static String ONEPARAM = "\\w* \\w*\\(\\w* \\w*\\)";
	private static String CRLF = System.getProperty("line.separator");
}

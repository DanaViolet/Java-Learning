package test.parser;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import test.parser.tokens.FileToken;

public class JUnitJavaParserTest
{

	//@Test
	public void test()
	{
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/src/week07/MyAbstractList.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}

	//@Test
	public void test2()
	{
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/src/week04/CountUtility.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}

	//@Test
	public void test3()
	{
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/src/week03/CollectionBusinessLayer.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}

	//@Test
	public void test4()
	{
		trace("Testing student formatted file");
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/Employee.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}
	
	//@Test
	public void test5()
	{
		trace("Testing student formatted file");
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/Kopkin/Employee.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}
	
	//@Test
	public void test6()
	{
		trace("Testing student formatted file");
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/ramirez/Employee.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}
		
	//@Test
	public void test7()
	{
		trace("Testing student formatted file");
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/herlihy/CollectionBusinessLayer.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
		}
	}
	
	@Test
	public void test8()
	{
		trace("Testing student formatted file test 8");
		//String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/coggins/CollectionBusinessLayer.java";
		//String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/hurst/CollectionBusinessLayer.java";
		String testFilePath = "C:/projects/school/seminolestate/COP2805-2017_05/cop2085/test-data/hurst/Employee.java";
		try
		{
			File testFile = new File(testFilePath);
			JavaParser parser = new JavaParser();

			FileToken fileToken = parser.parse(testFile);

			trace(fileToken.toString());
			// trace(fileToken.getClassToken().getComment().toString());
		}
		catch(Exception ex)
		{
			trace("Unexpected error during test: " + ex.getMessage());
			fail("Unexpected error during test: " + ex.getCause());
		}
	}

	private void trace(String msg)
	{
		System.out.println(msg);
	}

}

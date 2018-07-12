package week01;

import static org.junit.Assert.*;

import org.junit.Test;

public class JUnitGenericStackTest
{

	@Test
	public void testGenericStack()
	{
		trace(" testGenericStack");
		GenericStack<Integer> stack = new GenericStack<Integer>();
		assertTrue(stack.isEmpty());
		
		int size = 0;
		for(Integer i : INT_TESTDATA)
		{
			stack.push(i);
			size++;
			String msg = String.format("expected %d, actual %d", size, stack.getSize());
			trace(msg);
			assertEquals("Stack size doesn't match " + msg, size, stack.getSize());			
		}
	}
	
	@Test
	public void testGenericStack2()
	{
		trace(" testGenericStack2");
		GenericStack2<Integer> stack = new GenericStack2<Integer>();
		assertTrue(stack.isEmpty());
		
		int size = 0;
		for(Integer i : INT_TESTDATA)
		{
			stack.push(i);
			size++;
			String msg = String.format("expected %d, actual %d", size, stack.getSize());
			trace(msg);
			assertEquals("Stack size doesn't match " + msg, size, stack.getSize());
		}
		
		int expected = INT_TESTDATA[INT_TESTDATA.length - 1];
		int actual = stack.pop();
		String msg = String.format("Pop - expected: %d, actual: %d", expected, actual);
		trace(msg);
		assertEquals(msg, expected, actual);
		
		expected = INT_TESTDATA[INT_TESTDATA.length - 2];
		actual = stack.peek();
		msg = String.format("Peek - expected: %d, actual: %d", expected, actual);
		trace(msg);
		assertEquals(msg, expected, actual);
	}



	/**
	 * Trace the msg to a PrintStream Provides the method for tests to report
	 * status
	 * 
	 * @param msg
	 */
	protected void trace(String msg)
	{
		System.out.println("    " + msg);
	}
	
	private Integer[] INT_TESTDATA = new Integer[]{200,100,300,-14,35,0,75,68,102,303};
}

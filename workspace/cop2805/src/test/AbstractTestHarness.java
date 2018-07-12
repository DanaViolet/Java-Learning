package test;

import java.util.List;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

/**
 * Provides common services to the TestHarness instances
 * @author Scott LaChance
 *
 */
public abstract class AbstractTestHarness
{
	/**
	 * Must be implemented by the subclass
	 */
	protected abstract void runTests();
	
	protected boolean executeTest(@SuppressWarnings("rawtypes") Class c)
	{
		//trace("");
		trace("===============================================");
		trace(" -- executing " + c.getName());
		trace("===============================================");
		trace("");
		boolean success = true;
		Result result = org.junit.runner.JUnitCore
				.runClasses(c);
		int failCount = result.getFailureCount();
		if(failCount > 0)
		{
			
			List<Failure> failures = result.getFailures();
			trace("FAILED");
			for(Failure fail : failures)
			{
				trace(fail.getTestHeader());
				trace(" - message: " + fail.getMessage());
				trace(" - description: " + fail.getDescription());
				trace(" - exception: " + fail.getException());
				success = false;
			}
		}
		
		trace("-----------------------------------------------");
		trace(" -- " + (success ? "Success" : "Failed"));
		trace("===============================================");
		trace("");
		return success;			
	}
	
	
	static protected void trace(String msg)
	{
		System.out.println(msg);
	}
}

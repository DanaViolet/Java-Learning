package week04;


import test.AbstractTestHarness;

/**
 * Test Harness
 *
 * @author Scott LaChance
 */
public class TestHarness extends AbstractTestHarness
{
    public static void main(String[] args)
    {
        new TestHarness().runTests();
    }

    protected void runTests()
    {
        try
        {
            boolean countTest = executeTest(JUnitWordOccurrenceTest.class);
            boolean javadocTest = executeTest(JUnitJavadocValidation.class);

            boolean result = countTest && javadocTest;

            trace(result ? "Tests Passed" : "Tests Failed");
        }
        catch(Exception ex)
        {
            trace(ex.getMessage());
        }
    }
}

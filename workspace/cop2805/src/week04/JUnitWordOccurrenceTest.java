package week04;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class JUnitWordOccurrenceTest
{

	@Test
	public void testWordOccuranceCount()
	{
		int wordCount = 0;
		try
		{
			CountUtility counter = new CountUtility();
			
			StringReader sReader = new StringReader(TEST_DATA);
			BufferedReader reader = new BufferedReader(sReader);
			List<WordCountResult> list = counter.countWordOccurrences(reader);
			
			for(WordCountResult result : list)
			{
				wordCount += result.getCount();
				//trace(result.toString());
			}
//			
//			buildTestData(list);
			
			validateList(list);
			
		}
		catch(Exception ex)
		{
			fail("Test failed: " + ex.getMessage());
		}
		
		trace(String.format("Total word count: %d", wordCount));
	}
	
	private void buildTestData(List<WordCountResult> list)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Arrays.asList(");
		builder.append(System.lineSeparator());
		int count = 0;
		for(WordCountResult result : list)
		{
			if(count > 0)
			{
				builder.append(",");
			}
			
			String codeFragment = String.format("new WordCountResult(%d,\"%s\")", result.getCount(), result.getWord());
			builder.append(codeFragment);
			builder.append(System.lineSeparator());	
			count++;
		}
		
		builder.append("));");
		trace(builder.toString());
	}
	
	private void validateList(List<WordCountResult> list)
	{
		boolean isValid = true;
		
		boolean countMatch = expectedList.size() == list.size();
		if( !countMatch )
		{
			trace("Word count mismatch: expected " + expectedList.size() + ". actual: " + list.size());
			isValid = false;
		}
		
		for(WordCountResult result : list)
		{
			if(!isMatch(result))
			{
				trace(String.format("%s did not match expected result", result.getWord()));
				isValid = false;
			}
		}
		
		if(!isValid)
		{
			fail("List did not match expected results");
		}
		else
		{
			trace("Successful list validation - " + list.size());
		}
	}

	private boolean isMatch(WordCountResult result)
	{
		boolean valid = false;
		
		for(WordCountResult expected : expectedList)
		{
			if(result.getWord().equals(expected.getWord()))
			{
				if(result.getCount() == expected.getCount())
				{
					valid = true;
					break;
				}
				else
				{
					trace(String.format("%s expected count: %d, actual %d", 
							result.getWord(), expected.getCount(), result.getCount()));
				}
			}
		}
		
		return valid;
	}

	private void trace(String msg)
	{
		System.out.println(msg);
	}
	
	private static String TEST_DATA = 
	"Some where over the rainbow, skies are blue. Skies are blue\n"
	+ "over the rainbow, where fairy tales come true\n"
	+ "Some day I'll wish upon a start and wake up where the stars are far behind me.\n"
	+ "Way up on the chimney tops that's where you'll always find me.\n"
	+ "Some where over the rainbow way up high. There's a land that I heard of once in a lullaby.\n"
	+ "There's a lady whose sure, all that glitters is gold and she's buying a stairway to heaven.\n"
	+ "When she gets there she knows, if the stores are all closed, with a word she can get what she came for.\n"
	+ "There's a sign on the wall\n"
	+ "But she wants to be sure\n"
	+ "'Cause you know sometimes words have two meanings.\n"
	+ "In a tree by the brook\n"
	+ "There's a songbird that sings,\n"
	+ "Sometimes all of our thoughts are misgiven\n"
	+ "And she's buying a stairway to heaven\n"
	+ "There's a feeling I get\n"
	+ "When I look to the west,\n"
	+ "And my spirit is crying for leaving.\n"
	+ "In my thoughts I have seen\n"
	+ "Rings of smoke through the trees,\n"
	+ "When the voices of those who standing looking\n"
	+ "And she's buying a stairway to heaven\n"
	+ "And it's whispered that soon\n"
	+ "If we all call the tune\n"
	+ "Well Then the piper will lead us to reason.\n"
	+ "And a new day will dawn\n"
	+ "For those who stand long\n"
	+ "And the forests will echo with laughter\n";
	
	List<WordCountResult> expectedList = new ArrayList<WordCountResult>(
			Arrays.asList(
					new WordCountResult(1,"reason")
					,new WordCountResult(1,"echo")
					,new WordCountResult(1,"trees")
					,new WordCountResult(1,"soon")
					,new WordCountResult(1,"far")
					,new WordCountResult(1,"Then")
					,new WordCountResult(1,"tales")
					,new WordCountResult(1,"if")
					,new WordCountResult(1,"fairy")
					,new WordCountResult(1,"you")
					,new WordCountResult(2,"sure")
					,new WordCountResult(1,"in")
					,new WordCountResult(1,"voices")
					,new WordCountResult(1,"come")
					,new WordCountResult(2,"is")
					,new WordCountResult(1,"whose")
					,new WordCountResult(3,"heaven")
					,new WordCountResult(3,"Some")
					,new WordCountResult(1,"looking")
					,new WordCountResult(1,"dawn")
					,new WordCountResult(1,"glitters")
					,new WordCountResult(1,"word")
					,new WordCountResult(1,"If")
					,new WordCountResult(1,"sings")
					,new WordCountResult(1,"be")
					,new WordCountResult(2,"In")
					,new WordCountResult(4,"I")
					,new WordCountResult(1,"two")
					,new WordCountResult(1,"our")
					,new WordCountResult(1,"seen")
					,new WordCountResult(1,"long")
					,new WordCountResult(1,"gold")
					,new WordCountResult(5,"are")
					,new WordCountResult(2,"get")
					,new WordCountResult(1,"by")
					,new WordCountResult(1,"came")
					,new WordCountResult(2,"have")
					,new WordCountResult(3,"she's")
					,new WordCountResult(5,"where")
					,new WordCountResult(1,"stand")
					,new WordCountResult(1,"knows")
					,new WordCountResult(1,"But")
					,new WordCountResult(13,"a")
					,new WordCountResult(1,"stores")
					,new WordCountResult(1,"words")
					,new WordCountResult(1,"spirit")
					,new WordCountResult(1,"meanings")
					,new WordCountResult(1,"forests")
					,new WordCountResult(1,"lead")
					,new WordCountResult(14,"the")
					,new WordCountResult(1,"call")
					,new WordCountResult(1,"standing")
					,new WordCountResult(3,"When")
					,new WordCountResult(2,"blue")
					,new WordCountResult(1,"Sometimes")
					,new WordCountResult(3,"buying")
					,new WordCountResult(1,"closed")
					,new WordCountResult(6,"to")
					,new WordCountResult(1,"through")
					,new WordCountResult(1,"sometimes")
					,new WordCountResult(1,"tops")
					,new WordCountResult(1,"sign")
					,new WordCountResult(1,"upon")
					,new WordCountResult(1,"wish")
					,new WordCountResult(4,"that")
					,new WordCountResult(1,"high")
					,new WordCountResult(1,"wake")
					,new WordCountResult(1,"find")
					,new WordCountResult(1,"Rings")
					,new WordCountResult(2,"me")
					,new WordCountResult(1,"west")
					,new WordCountResult(3,"up")
					,new WordCountResult(1,"Skies")
					,new WordCountResult(2,"day")
					,new WordCountResult(2,"those")
					,new WordCountResult(1,"us")
					,new WordCountResult(1,"always")
					,new WordCountResult(4,"all")
					,new WordCountResult(1,"brook")
					,new WordCountResult(1,"new")
					,new WordCountResult(1,"you'll")
					,new WordCountResult(1,"it's")
					,new WordCountResult(1,"tree")
					,new WordCountResult(1,"For")
					,new WordCountResult(1,"feeling")
					,new WordCountResult(2,"my")
					,new WordCountResult(1,"look")
					,new WordCountResult(3,"rainbow")
					,new WordCountResult(5,"she")
					,new WordCountResult(1,"once")
					,new WordCountResult(6,"And")
					,new WordCountResult(1,"true")
					,new WordCountResult(5,"There's")
					,new WordCountResult(1,"know")
					,new WordCountResult(1,"leaving")
					,new WordCountResult(1,"laughter")
					,new WordCountResult(2,"who")
					,new WordCountResult(1,"behind")
					,new WordCountResult(1,"chimney")
					,new WordCountResult(1,"songbird")
					,new WordCountResult(1,"Well")
					,new WordCountResult(2,"for")
					,new WordCountResult(1,"that's")
					,new WordCountResult(1,"Way")
					,new WordCountResult(1,"we")
					,new WordCountResult(1,"tune")
					,new WordCountResult(1,"can")
					,new WordCountResult(2,"and")
					,new WordCountResult(1,"crying")
					,new WordCountResult(4,"of")
					,new WordCountResult(1,"land")
					,new WordCountResult(1,"wants")
					,new WordCountResult(1,"heard")
					,new WordCountResult(1,"I'll")
					,new WordCountResult(2,"on")
					,new WordCountResult(1,"piper")
					,new WordCountResult(3,"over")
					,new WordCountResult(2,"thoughts")
					,new WordCountResult(1,"lullaby")
					,new WordCountResult(3,"will")
					,new WordCountResult(1,"start")
					,new WordCountResult(1,"smoke")
					,new WordCountResult(1,"'Cause")
					,new WordCountResult(1,"stars")
					,new WordCountResult(1,"gets")
					,new WordCountResult(1,"way")
					,new WordCountResult(1,"misgiven")
					,new WordCountResult(1,"whispered")
					,new WordCountResult(2,"with")
					,new WordCountResult(1,"what")
					,new WordCountResult(1,"there")
					,new WordCountResult(1,"lady")
					,new WordCountResult(1,"skies")
					,new WordCountResult(3,"stairway")
					,new WordCountResult(1,"wall")
					));
}

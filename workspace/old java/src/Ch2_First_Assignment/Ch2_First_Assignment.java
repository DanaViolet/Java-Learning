package Ch2_First_Assignment;

public class Ch2_First_Assignment {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int x = 10;
		int y = 21;
		
		int result = x + y;
		int nextResult = y % x;
		
		System.out.println("Result is: " + result + "Next Result is " + nextResult);
		
		//declare variables
		int gradeOne = 95;
		int gradeTwo = 85;
		int gradeThree = 75;
		//calculate average
		int finalGrade = (gradeOne + gradeTwo + gradeThree)/3;
		
		//create a blank line using \n
		System.out.println("\n");
		System.out.println("Student's final grade is " + finalGrade + " percent");
	}

}

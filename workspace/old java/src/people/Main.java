package people;
//import scanner
import java.util.Scanner;
public class Main {
	public static Student[] ary = new Student[10];

	public static void main(String[] args) {
		//initialize scanner
		Scanner input = new Scanner(System.in);
		//initialize array of student objects.
		
		ary[0] = new Student("FN1", "LN1", 1, 2000, 1.0);
		ary[1] = new Student("FN2", "LN2", 2, 2001, 1.1);
		ary[2] = new Student("FN3", "LN3", 3, 2002, 1.2);
		ary[3] = new Student("FN4", "LN4", 4, 2003, 1.3);
		ary[4] = new Student("FN5", "LN5", 5, 2004, 1.4);
		ary[5] = new Student("FN6", "LN6", 6, 2005, 1.5);
		ary[6] = new Student("FN7", "LN7", 7, 2006, 1.6);
		ary[7] = new Student("FN8", "LN8", 8, 2007, 1.7);
		ary[8] = new Student("FN9", "LN9", 9, 2008, 1.8);
		ary[9] = new Student("FN10", "LN10", 10, 2009, 1.9);
		// For loop asking for student ID
		System.out.print("Enter student ID:");
		
		int search = input.nextInt();
		//call process student with ID as argument.
		ProcessStudent.ProcessStudent(search);
		
		//output student
		//System.out.println("index = " + index);

	}

}

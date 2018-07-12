package Ct1;
//COP2800, George Harrington
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Data d = new Data(40);
		
		d.InitEven();
		d.Puts();
		Scanner input = new Scanner(System.in);

		System.out.print("Enter an integer that you would like to find in the array: ");
		int search = input.nextInt();
		int index = d.Search(search);
		System.out.println("index = " + index);

		Data f = new Data(15);
		f.InitFact();
		f.Puts();
	}

}

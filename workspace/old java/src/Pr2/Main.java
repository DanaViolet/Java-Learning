package Pr2;

public class Main {

	public static void main(String[] args) {
		//define points for line and triangle
		Point pt1 = new Point(1,1);
		Point pt2 = new Point(1,2);
		Point pt3 = new Point(2,1);
		//Point pt4 = new Point(2,2);
		
		//"this will create a square" -- teacher dude
		//Square sq = new Square(pt1, pt2, pt3, pt4);
		//sq.Display();
		
		//display length of line
		Line line = new Line(pt1, pt2);
		line.Display();
		
		//add a space in the output
		System.out.println("");
		
		//display triangle side lengths.
		Triangle triangle = new Triangle(pt1, pt2, pt3);
		triangle.Display();
	}

}

package project2;

public class Main {

	public static void main(String[] args) {
		//something clever
		int x = 2;
		int y = 2;
		Point pt1 = new Point(x,y);

		x = 4;
		y = 4;
		Point pt2 = new Point(x,y);

		Trig pTrig = new Trig(pt1, pt2);


		Midpoint mp = new Midpoint(pt1, pt2);

		Point mid = new Point(mp.x, mp.y);

		System.out.println("The points are" + " (" + pt1.x + "," + pt1.y + ") and (" + pt2.x + "," + pt2.y + ")");

		System.out.println("The midpoint between them is: (" + mid.x + "," + mid.y + ")");
		System.out.println("The distance between the two is: " + pTrig.EuDistance);

	}

}

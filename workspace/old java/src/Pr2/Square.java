package Pr2;

public class Square extends Calc{
	public Point pt1;
	public Point pt2;
	public Point pt3;
	public Point pt4;
	
	public double side1;
	public double side2;
	public double side3;
	public double side4;
	
	public Square(){
		
	}
	
	public Square(Point _pt1, Point _pt2, Point _pt3, Point _pt4){
		pt1=_pt1;
		pt2=_pt2;
		pt3=_pt3;
		pt4=_pt4;
		
		
		//two of these are calculating the hypotenuse. fix it later.
		side1 = EuDist(pt1, pt2);
		side2 = EuDist(pt2, pt3);
		side3 = EuDist(pt3, pt4);
		side4 = EuDist(pt4, pt1);
	}
	
	public void Display(){
		System.out.println("Side1 = " + side1);
		System.out.println("Side2 = " + side2);
		System.out.println("Side3 = " + side3);
		System.out.println("Side4 = " + side4);
	}
	
	
}

package people;

public class Student {
/*FirstName
LastName
ID
DOB (date of birth --- the year only.) 
Grades
GPA
*/
	public String FN;
	public String LN;
	public int ID;
	public int DOB;
	public double GPA;
	
	public Student(String _FN, String _LN, int _ID, int _DOB, double _GPA){
		FN 	= _FN;
		LN 	= _LN;
		ID 	= _ID;
		DOB = _DOB;
		GPA = _GPA;
		
	}
	
	public void Puts(){
		for (int i=0; i<10; i++){
			System.out.println(FN);
			System.out.println(LN);
			System.out.println(ID);
		}
	}
	
}

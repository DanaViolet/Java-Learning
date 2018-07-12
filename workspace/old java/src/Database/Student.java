package Database;
public class Student {
	
	public Student(){
		//default constructor
	}
	
	public Student(String _First, String _Last, int _DOB, int _ID, String _Email, double _GPA){
		/* First name
		 * Last name
		 * DOB
		 * ID
		 * email
		 * gpa
		 */
		String First = _First;
		String Last = _Last;
		String Email = _Email;
		int DOB = _DOB;
		int ID = _ID;
		double GPA = _GPA;
		
	}
	
	public void InitStudent(Student ary[]){
		
		for (int i = 0; i < ary.length; i++){
			//create each student using array index
		}
	}
	
	public void Puts(){
		//display single line
	}
	
	public void SearchDB(){
		//search each student entry by ID number.
		//return 
	}

}

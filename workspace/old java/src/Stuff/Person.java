package Stuff;
import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Person {
	static String FirstName;
	static String LastName;
	static String ID;
	
	public Person(){
		
	}
	
	public Person(String FN, String LN, String _ID){
		FirstName = FN;
		LastName = LN;
		ID = _ID;
	}
	
	public static int SearchById(ArrayList<Person> ary, String Id){
		for (int i  =  0;   i  <  ary.size(); i++)
		{ary.get(i);
		if   (Id == Person.ID)
		{return i;}
		}return -1;}
	
	public static void Display(Person person){
		try{
		String buffer = ID   +  "  " +  FirstName +  " " +  LastName;
		System.out.println(buffer);
		}
		catch (Exception e){
			Alert exception = new Alert(AlertType.INFORMATION);
			exception.setTitle("OOPS!");
			exception.setHeaderText(null);
			exception.setContentText("Something went horribly wrong.");

			exception.showAndWait();
		}
		}

}

package Database;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

//import scanner
//George Harrington

public class Main extends Application{
	static ArrayList<Person> list = new ArrayList<Person>(10);
	Student data[] = new Student[10];
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void start(Stage stage){
		
		TextField tbID = new TextField("ID");
		tbID.setLayoutX(0);
		tbID.setLayoutY(60);
		
		
		//Buttons go here
		Button sum = new Button("Sum");
		sum.setLayoutX(100);
		sum.setLayoutY(90);
		sum.setOnAction((ActionEvent e ) -> {
			//call sum method
			int total = 0;
			total = Person.Sum();
			//display alert box showing the entry.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Here is the total:");
			alert.setHeaderText(null);
			alert.setContentText(String.valueOf(total));

			alert.showAndWait();
		});
	
		
		Button add = new Button("Add");
		add.setLayoutX(0);
		add.setLayoutY(90);
		add.setOnAction((ActionEvent e ) -> {
			list.add(new Person(Integer.parseInt(tbID.getText())));
			//display alert box showing the entry.
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Successful Entry");
			alert.setHeaderText(null);
			list.get(0);
			int i = Person.n;
			alert.setContentText(String.valueOf(i));

			alert.showAndWait();
		});
		
		Pane pane = new Pane();
		pane.getChildren().addAll(sum, add, tbID);
		Scene scene = new Scene(pane, 175, 120);
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
		stage.setTitle("DB");
	}


}

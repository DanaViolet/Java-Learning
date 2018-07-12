package Geo;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

//import com.sun.javafx.geom.Rectangle;
//import com.sun.prism.Image;
import javafx.scene.image.*;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
		public void start(Stage stage){
			/*
			//Text fields go here
			TextField tbFN = new TextField("Name");
			tbFN.setLayoutX(0);
			tbFN.setLayoutY(0);
			
			
			//Buttons go here
			Button search = new Button("OK");
			search.setLayoutX(100);
			search.setLayoutY(90);
			search.setOnAction((ActionEvent e ) -> {});

			*/
			Line line = new Line(30,50,60,200);
			line.setStrokeWidth(3);
			
			
			
			
			
			Image img = new Image("https://cdn.pixabay.com/photo/2012/05/02/22/40/food-46573_960_720.png");
			Image img2 = new Image("http://i0.kym-cdn.com/entries/icons/original/000/021/971/saltybae.PNG");
			ImageView imgview = new ImageView(img2);
			imgview.setFitWidth(img.getWidth()/3);
			imgview.setFitHeight(img.getHeight()/3);
			
			Rectangle rect = new Rectangle(30,30, 25,40);
			rect.setFill(Color.DARKORCHID);
			rect.setFill(new ImagePattern(img));
			
			PathTransition pt = new PathTransition();
			pt.setDuration(Duration.millis(2000));
			pt.setPath(line);
			pt.setNode(rect);
			pt.setCycleCount(Timeline.INDEFINITE);
			pt.setAutoReverse(true);
			pt.play();
			
			line.setOnMousePressed(e -> pt.pause());
			line.setOnMouseReleased(e -> pt.play());
			
			Circle circle = new Circle (60, 20, 20);
			circle.setStroke(Color.rgb(00, 80, 00));
			circle.setStrokeWidth(4);
			circle.setFill(Color.LIMEGREEN);
			
			Pane pane = new Pane();
			pane.getChildren().addAll(imgview, line, circle, rect);
			//pane.getChildren().add(line);
			//pane.getChildren().add(circle);
			//pane.getChildren().add(rect);
			
			Scene scene = new Scene(pane, 175, 120);
			
			//stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Geo");
		}
}

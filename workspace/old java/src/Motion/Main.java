package Motion;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}
	
		public void start(Stage stage){
			
			Circle c1 = new Circle (60, 80, 20);
			c1.setStroke(Color.rgb(00, 80, 00));
			c1.setStrokeWidth(1);
			c1.setFill(Color.LIMEGREEN);
			
			Circle c2 = new Circle (230, 80, 20);
			c2.setStrokeWidth(1);
			c2.setFill(Color.RED);
			
			Arc a2 = new Arc(150, 150, 100, 100, 45, 90);
			a2.setType(ArcType.CHORD);
			a2.setStroke(Color.BLACK);
			a2.setFill(null);
			a2.setStrokeWidth(1);
			
			PathTransition pt = new PathTransition();
			pt.setDuration(Duration.millis(2000));
			pt.setPath(a2);
			pt.setNode(c2);
			pt.setCycleCount(5);
			pt.setAutoReverse(false);
			pt.play();
			
			Pane pane = new Pane();
			pane.getChildren().addAll(c1, c2, a2);
			
			Scene scene = new Scene(pane, 300, 200);
			
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
			stage.setTitle("Motion");
		}
}

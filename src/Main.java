import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import GUI.UIController;

public class Main extends Application {
		
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("HeyBuddy! [v0.2]");
			Parent root = FXMLLoader.load(getClass().getResource("MyView.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			//primaryStage.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

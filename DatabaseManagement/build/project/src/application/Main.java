package application;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("WelcomeView.fxml")); ;
		    Scene scene = new Scene(root);

	        stage.setTitle("Welcome");
	        stage.setScene(scene);
	        stage.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    	        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

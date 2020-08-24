package mg.jurisprudence;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		Parent parent = null;
		
		try {
			parent = FXMLLoader.load(getClass().getResource("/mg/jurisprudence/view/StarterView.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		Scene scene = new Scene(parent);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Jurisprudence 1976 - 2003");
		primaryStage.show();
	}
}

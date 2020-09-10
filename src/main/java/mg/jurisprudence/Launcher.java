package mg.jurisprudence;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mg.jurisprudence.app.controller.StarterController;

public class Launcher extends Application {
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	@Override
	public void start(Stage primaryStage){
		AnchorPane parent = null;
		FXMLLoader loader = new FXMLLoader();
		try {
			loader.setLocation(Launcher.class.getResource("/mg/jurisprudence/app/view/StarterView.fxml"));
			parent = loader.load();
			Scene scene = new Scene(parent);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Jurisprudence 1976 - 2003");
			primaryStage.show();
			((StarterController)loader.getController()).setStage(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

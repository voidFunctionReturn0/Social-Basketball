package server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerAppMain extends Application {
	
	public static void main(String[] args) {
		Application.launch(args); // JavaFX Application Thread ���� & start�޼ҵ� ����
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("server.fxml"));
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("~.css").toString());
		primaryStage.setScene(scene);
		primaryStage.setTitle("���ҷ�?");
		//primaryStage.setOnCloseRequest(??);
		primaryStage.show();
	}
}
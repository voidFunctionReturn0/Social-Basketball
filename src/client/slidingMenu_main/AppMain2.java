package client.slidingMenu_main;

import java.net.InetSocketAddress;

import client.AppMain;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class AppMain2 extends Application {
	public static final InetSocketAddress serverAddress = new InetSocketAddress("192.168.0.4", 50001);
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		AppMain.primaryStage = primaryStage;
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("slidingMenuBoards.fxml"));
		Scene scene = new Scene(root);
	
		primaryStage.setTitle("³ó±¸ÇÒ·¡?");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

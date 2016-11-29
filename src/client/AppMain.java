package client;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import client.login.LoginController;
import client.model.Board;
import client.model.Comment;
import client.model.Member;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application {
	public static Stage primaryStage;

	public static final InetSocketAddress serverAddress = new InetSocketAddress("192.168.0.4", 50001);

	public static Member member;
	
	// 디폴트 이미지 경로를 알아내기 위한 필드
	public static File defaultImage;
	
	public static int pageNo=1;
	public static int totalPage=0;

	// 서버와 바이트배열로 송수신 하기 위해 String 타입으로 선언
	
	public static String search_Area=null;
	public static String bno;
	public static String cno;
	
	public static List<Board> boardList = new ArrayList<Board>();
	public static List<Comment> commentList = new ArrayList<Comment>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// 첫 화면(로그인) 보여주기
		AppMain.primaryStage = primaryStage;
		
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("../client/login/login(first).fxml"));
		LoginController.setPrimaryStage(primaryStage);
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("농구할래?");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// defaultImage 초기화
		try {
			defaultImage = new File(AppMain.class.getResource("images/profile.png").toURI());
		} catch(Exception e) { e.printStackTrace(); }
		
		launch(args);
	}
}

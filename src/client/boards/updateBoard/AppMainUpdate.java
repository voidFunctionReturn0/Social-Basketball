package client.boards.updateBoard;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import client.login.LoginController;
import client.model.Board;
import client.model.Member;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMainUpdate extends Application {
	public static Stage primaryStage;

	public static final InetSocketAddress serverAddress = new InetSocketAddress("192.168.0.07", 50001);

	public static Member member = new Member("test1", "123123", "150cm����", "����", "F", "����� ���α�", "�ʺ�", "image1.jpg");
	
	// ����Ʈ �̹��� ��θ� �˾Ƴ��� ���� �ʵ�
	public static File defaultImage;
	
	public static int pageNo=1;
	public static int totalPage=0;

	// ������ ����Ʈ�迭�� �ۼ��� �ϱ� ���� String Ÿ������ ����
	public static String bno = "303";
	
	public static List<Board> boardList = new ArrayList<Board>();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// ù ȭ��(�α���) �����ֱ�
		AppMainUpdate.primaryStage = primaryStage;
		
		Parent root = (Parent)FXMLLoader.load(getClass().getResource("updateBoard.fxml"));
		LoginController.setPrimaryStage(primaryStage);
		Scene scene = new Scene(root);
		
		primaryStage.setTitle("���ҷ�?");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		// defaultImage �ʱ�ȭ
		try {
			defaultImage = new File(AppMainUpdate.class.getResource("images/profile.png").toURI());
		} catch(Exception e) { e.printStackTrace(); }
		
		launch(args);
	}
}

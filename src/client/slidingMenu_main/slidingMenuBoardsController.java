package client.slidingMenu_main;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import client.AppMain;
import client.boards.Transition;
import client.boards.boardsRoomController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class slidingMenuBoardsController implements Initializable {
	@FXML private Button xBtn;
	@FXML private Label idLbl;
	@FXML private Label levelLbl;
	@FXML private Button adjustProfileBtn;
	@FXML private Button logoutBtn;
	@FXML private Button withdrawBtn;
	@FXML private Button friendsList;
	@FXML private Button exitBtn;
	@FXML private CheckBox guardChckBox;
	@FXML private CheckBox forwardChckBox;
	@FXML private CheckBox centerChckBox;
	@FXML private ComboBox<String> cityCmbBox;
	@FXML private ComboBox<String> boroughCmbBox;
	@FXML private ImageView imageView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// AppMain.member 정보대로 회원정보를 세팅
		if(AppMain.member!=null) {
			// id 세팅
			idLbl.setText(AppMain.member.getMid());
			// 실력 세팅
			levelLbl.setText(AppMain.member.getMlevel());
			
			// 포지션 세팅 후 수정불가하게 만듦
			setPosition();
			guardChckBox.setDisable(true);
			forwardChckBox.setDisable(true);
			centerChckBox.setDisable(true);
			
			// 지역 세팅 후 수정불가하게 만듦
			String[] area = AppMain.member.getMarea().split(" ");
			cityCmbBox.setValue(area[0]);
			boroughCmbBox.setValue(area[1]);
			cityCmbBox.setDisable(true);
			boroughCmbBox.setDisable(true);
		}
		
		// 저장한 사진이 있으면 서버에서 AppMain.member의 mimage에 해당하는 사진 받아와서 세팅
		if(AppMain.member.getMimage()!=null) {
			try {
				Socket sock = new Socket();
				sock.connect(AppMain.serverAddress);
				
				OutputStream os = sock.getOutputStream();
				os.write("getPicture".getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				os.write(AppMain.member.getMimage().getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				InputStream is = sock.getInputStream();
				Image profile = new Image(is);
				imageView.setImage(profile);
				
				is.close();
				os.close();
				sock.close();
				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	// 정보수정버튼을 누르면 '정보수정' 다이얼로그이 나오도록 버튼이벤트 처리
	public void handleAdjustProfileBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(adjustProfileBtn.getScene().getWindow());
			dialog.setTitle("정보수정");
			Parent parent = FXMLLoader.load(getClass().getResource("adjustProfile/adjustProfile.fxml"));
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// 정보수정화면을 로드하지 못할 때 예외
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// 로그아웃버튼을 누르면 로그아웃처리 후 성공하면 '로그아웃되었습니다'다이얼로그가 나오도록 버튼이벤트처리
	public void handleLogoutBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(logoutBtn.getScene().getWindow());
			dialog.setTitle("로그아웃");
			Parent parent = FXMLLoader.load(getClass().getResource("logout/you_logouted.fxml"));
			Button logOutbtn = (Button) parent.lookup("#logOutBtn");
			
			logOutbtn.setOnAction((e)-> {
				try {
					dialog.close();
					Parent root = (Parent)FXMLLoader.load(getClass().getResource("../login/login(first).fxml"));
					boardsRoomController.instance.getChildren().add(root);
					boardsRoomController.instance.getChildren().remove(1);
					boardsRoomController.instance.getChildren().get(0).setVisible(false);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			});
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// 로그아웃결과화면을 로드하지 못할 때 예외
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	
	
	
	// 친구관리버튼을 누르면 친구관리 다이얼로그 나오도록 버튼이벤트처리
	public void handleFriendsListBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(friendsList.getScene().getWindow());
			dialog.setTitle("친구관리");
			Parent parent = FXMLLoader.load(getClass().getResource("friendsList/friendsList.fxml"));
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// 친구관리 화면을 로드하지 못할 때 예외
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// 종료버튼을 누르면 종료하시겠습니까 다이얼로그 나오도록 버튼이벤트처리
	public void handleExitBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(exitBtn.getScene().getWindow());
			dialog.setTitle("앱 종료");
			Parent parent = FXMLLoader.load(getClass().getResource("exit/doYouExit.fxml"));
			Button exitOkBtn = (Button) parent.lookup("#exitOkBtn");
			Button exitCancelBtn = (Button) parent.lookup("#exitCancelBtn");
			
			exitOkBtn.setOnAction((e)->Platform.runLater(()-> Platform.exit()));
			exitCancelBtn.setOnAction((e)->dialog.close());
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// 종료하시겠습니까 화면을 로드하지 못할 때 예외
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// AppMain.member의 포지션 정보대로 체크박스를 세팅
	private void setPosition() {
		if(AppMain.member.getMposition()!=null) {
			if(AppMain.member.getMposition().contains("G")) {
				guardChckBox.setSelected(true);
			}
			if(AppMain.member.getMposition().contains("F")) {
				forwardChckBox.setSelected(true);
			}
			if(AppMain.member.getMposition().contains("C")) {
				centerChckBox.setSelected(true);
			}
		}
	}
	
	public void handleXBtn(ActionEvent e) {
		Transition.slide(boardsRoomController.menuParent, -110, -280, new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		boardsRoomController.instance.getChildren().remove(boardsRoomController.menuParent);
        	}
        });
	}
}

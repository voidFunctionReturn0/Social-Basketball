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
		// AppMain.member ������� ȸ�������� ����
		if(AppMain.member!=null) {
			// id ����
			idLbl.setText(AppMain.member.getMid());
			// �Ƿ� ����
			levelLbl.setText(AppMain.member.getMlevel());
			
			// ������ ���� �� �����Ұ��ϰ� ����
			setPosition();
			guardChckBox.setDisable(true);
			forwardChckBox.setDisable(true);
			centerChckBox.setDisable(true);
			
			// ���� ���� �� �����Ұ��ϰ� ����
			String[] area = AppMain.member.getMarea().split(" ");
			cityCmbBox.setValue(area[0]);
			boroughCmbBox.setValue(area[1]);
			cityCmbBox.setDisable(true);
			boroughCmbBox.setDisable(true);
		}
		
		// ������ ������ ������ �������� AppMain.member�� mimage�� �ش��ϴ� ���� �޾ƿͼ� ����
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
	
	// ����������ư�� ������ '��������' ���̾�α��� �������� ��ư�̺�Ʈ ó��
	public void handleAdjustProfileBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(adjustProfileBtn.getScene().getWindow());
			dialog.setTitle("��������");
			Parent parent = FXMLLoader.load(getClass().getResource("adjustProfile/adjustProfile.fxml"));
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// ��������ȭ���� �ε����� ���� �� ����
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// �α׾ƿ���ư�� ������ �α׾ƿ�ó�� �� �����ϸ� '�α׾ƿ��Ǿ����ϴ�'���̾�αװ� �������� ��ư�̺�Ʈó��
	public void handleLogoutBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(logoutBtn.getScene().getWindow());
			dialog.setTitle("�α׾ƿ�");
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
		
		// �α׾ƿ����ȭ���� �ε����� ���� �� ����
		} catch(IOException e) {
			e.printStackTrace();
		}
	}


	
	
	
	// ģ��������ư�� ������ ģ������ ���̾�α� �������� ��ư�̺�Ʈó��
	public void handleFriendsListBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(friendsList.getScene().getWindow());
			dialog.setTitle("ģ������");
			Parent parent = FXMLLoader.load(getClass().getResource("friendsList/friendsList.fxml"));
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// ģ������ ȭ���� �ε����� ���� �� ����
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// �����ư�� ������ �����Ͻðڽ��ϱ� ���̾�α� �������� ��ư�̺�Ʈó��
	public void handleExitBtn(Event event) {
		try {
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(exitBtn.getScene().getWindow());
			dialog.setTitle("�� ����");
			Parent parent = FXMLLoader.load(getClass().getResource("exit/doYouExit.fxml"));
			Button exitOkBtn = (Button) parent.lookup("#exitOkBtn");
			Button exitCancelBtn = (Button) parent.lookup("#exitCancelBtn");
			
			exitOkBtn.setOnAction((e)->Platform.runLater(()-> Platform.exit()));
			exitCancelBtn.setOnAction((e)->dialog.close());
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		
		// �����Ͻðڽ��ϱ� ȭ���� �ε����� ���� �� ����
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	// AppMain.member�� ������ ������� üũ�ڽ��� ����
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

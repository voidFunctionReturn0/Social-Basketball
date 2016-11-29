package client.waitingRoom;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.chattingRoom.chatRootController;
import client.login.LoginController;
import client.model.Member;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class waitingRoomController implements Initializable{
	@FXML private Button btnSlidingMenu;
	@FXML private TableView<Member> tableView;

	private ObservableList<Member> list;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Member member = new Member("ghostinthejava","12345","190", "남", "포워드", "서울시 송파구", "중수", "profile.png");
		
		list = FXCollections.observableArrayList(member);
		//list.add(member);
		
		TableColumn tc = tableView.getColumns().get(0);
		tc.setCellValueFactory(new PropertyValueFactory("mheight"));
		tc.setStyle("-fx-alignment: CENTER;");
		
		tc = tableView.getColumns().get(1);
		tc.setCellValueFactory(new PropertyValueFactory("mimage"));
		tc.setStyle("-fx-alignment: CENTER;");
		
		tc = tableView.getColumns().get(2);
		tc.setCellValueFactory(new PropertyValueFactory("mid"));
		tc.setStyle("-fx-alignment: CENTER;");
		
		tc = tableView.getColumns().get(3);
		tc.setCellValueFactory(new PropertyValueFactory("marea"));
		tc.setStyle("-fx-alignment: CENTER;");
		
		tc = tableView.getColumns().get(4);
		tc.setCellValueFactory(new PropertyValueFactory("mlevel"));
		tc.setStyle("-fx-alignment: CENTER;");
		
		tableView.setItems(list);
		tableView.setOnMouseClicked(event->handleTableViewMouseClicked(event));
		
	
		
	}
	private void handleTableViewMouseClicked(MouseEvent event) {
		if (event.getClickCount() != 2) return;
		
		
		try {		
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnSlidingMenu.getScene().getWindow());
			dialog.setTitle("프로필 상세화면");
		
			
			
			Parent detailView = (Parent)FXMLLoader.load(getClass().getResource("../detailView/detailView.fxml"));
			
			System.out.println("핸들정상작동");
			
			TextArea height = (TextArea) detailView.lookup("#detail_Height");
			TextArea sex = (TextArea) detailView.lookup("#detail_Sex");
			TextArea id = (TextArea) detailView.lookup("#detail_Id");
			TextArea position = (TextArea) detailView.lookup("#detail_Position");
			TextArea area = (TextArea) detailView.lookup("#detail_Area");
			
			
			Member member = tableView.getSelectionModel().getSelectedItem();//member객체가 제대로 받아와 질까?
			
			height.setText(member.getMheight());
			sex.setText(member.getMsex());
			id.setText(member.getMid());
			position.setText(member.getMposition());
			area.setText(member.getMarea());
			
			
			
			
			//Button btnClose = (Button) parent.lookup("#btnClose");
			//btnClose.setOnAction(e->dialog.close());
			
			Scene scene = new Scene(detailView);
			dialog.setScene(scene);
			dialog.show();	
		} catch (IOException e) {}
		
		
		
		
	}
	public void makeRoomBtn() throws Exception{
		Stage dialog = new Stage(StageStyle.DECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(LoginController.primaryStage);
		Parent makeRoom =(Parent)FXMLLoader.load(getClass().getResource("../makeRoom/makeRoom.fxml"));		
		Scene sc = new Scene(makeRoom);
		dialog.setScene(sc);
		dialog.setResizable(false);
		dialog.setTitle("방 만들기");
		dialog.show();
	}
	
	
	public void goBoard() throws Exception {
		Parent root =(Parent)FXMLLoader.load(getClass().getResource("../board/boardAllMenu.fxml"));
		Scene sc = new Scene(root);
		LoginController.primaryStage.setScene(sc);
	}
	

	public void handleEnterBtn(ActionEvent event) throws Exception {
		try {
			Parent parent = FXMLLoader.load(chatRootController.class.getResource("chat.fxml"));
			Scene sc = new Scene(parent);
			LoginController.primaryStage.setScene(sc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void handleSlidingMenu(ActionEvent event) {
		try {
			Parent login= FXMLLoader.load(getClass().getResource("../slidingMenu_main/slidingMenu_waitingRoom.fxml"));
			StackPane root = (StackPane) btnSlidingMenu.getScene().getRoot();
			root.getChildren().add(login);
			
			login.setTranslateX(-200);

			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(login.translateXProperty(), -80);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(100), keyValue);
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}

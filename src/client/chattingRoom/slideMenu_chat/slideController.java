package client.chattingRoom.slideMenu_chat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class slideController implements Initializable {
	@FXML private StackPane slide;
	@FXML private Button cahtBtn;
	@FXML private Button inviteBtn;
	@FXML private Button btnOk;
	@FXML private Button btnExit;
	@FXML private Button btnOutExit;
	@FXML private Button btnOut;
	@FXML private Button getOutBtn;
	@FXML private Button roomOutBtn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cahtBtn.setOnAction(e->handleCahtBtn(e));
		inviteBtn.setOnAction(e->handleInviteBtn(e));
		getOutBtn.setOnAction(e->handleGetOutBtn(e));
		roomOutBtn.setOnAction(e->handleRoomOutBtn(e));
	}
	private void handleRoomOutBtn(ActionEvent event) {
		try{
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(slide.getScene().getWindow());
			dialog.setTitle("방나가기");
			Parent parent = (Parent)FXMLLoader.load(getClass().getResource("roomOut.fxml"));
			
			Button roomOutYesBtn = (Button)parent.lookup("#roomOutYesBtn");
			roomOutYesBtn.setOnAction(e->Platform.exit());
			Button roomOutNoBtn = (Button)parent.lookup("#roomOutNoBtn");
			roomOutNoBtn.setOnAction(e->dialog.close());
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
			
			}catch(Exception e){}
			}
	private void handleGetOutBtn(ActionEvent event) {
		try{
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(slide.getScene().getWindow());
			dialog.setTitle("내보내기");
			Parent parent = (Parent)FXMLLoader.load(getClass().getResource("getOut/getOut.fxml"));
			
			Button btnOk = (Button)parent.lookup("#btnOutExit");
			btnOk.setOnAction(e->dialog.close());
			Button btnExit = (Button)parent.lookup("#btnOut");
			btnExit.setOnAction(e->dialog.close());
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
			}catch(Exception e){}
			}
		
	private void handleInviteBtn(ActionEvent event) {
		try{
		Stage dialog = new Stage(StageStyle.UTILITY);
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(slide.getScene().getWindow());
		dialog.setTitle("초대하기");
		Parent parent = (Parent)FXMLLoader.load(getClass().getResource("chatRoomInvite/chatRoomInvite.fxml"));
		
		Button btnOk = (Button)parent.lookup("#btnOk");
		btnOk.setOnAction(e->dialog.close());
		Button btnExit = (Button)parent.lookup("#btnExit");
		btnExit.setOnAction(e->dialog.close());
		
		Scene scene = new Scene(parent);
		dialog.setScene(scene);
		dialog.setResizable(false);
		dialog.show();
		}catch(Exception e){}
		}
	
	public static void setPrimaryStage(Stage primaryStage) {
	}

	
	public void handleCahtBtn(ActionEvent event) {
		try {
			StackPane root = (StackPane) cahtBtn.getScene().getRoot();
			
			slide.setTranslateX(-100);
			
			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(slide.translateXProperty(), -350);
			KeyFrame keyFrame = new KeyFrame(
	    		Duration.millis(1000), 
	    		new EventHandler<ActionEvent>() {
		        	@Override
		        	public void handle(ActionEvent event) {
		        		root.getChildren().remove(slide);
		        	}
		        },
		        keyValue
	        );
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
package client.makeBoard;

import java.net.URL;
import java.util.ResourceBundle;

import client.login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MakeBoardController implements Initializable{
	@FXML Button cancelMakingRoomBtn;
	@FXML Button makingRoom;
	@FXML TextField roomTitle;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	public void makeCancel() throws Exception{
		Stage dialog = new Stage(StageStyle.DECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(LoginController.primaryStage);
		Parent makeCancel =(Parent)FXMLLoader.load(getClass().getResource("../makeBoard/MakeCancel.fxml"));
		Button CancelOkBtn = (Button) makeCancel.lookup("#CancelOk");
		CancelOkBtn.setOnAction((e)->{
			dialog.close();
			Stage parentDialog = (Stage)cancelMakingRoomBtn.getScene().getWindow();
			parentDialog.close();
		});
		Scene sc = new Scene(makeCancel);
		dialog.setScene(sc);
		dialog.setResizable(false);
		dialog.setTitle("취소");
		dialog.show();		
	}
	
	public void handleMakingRoom(ActionEvent event) {
		int roomNum; //데이터베이스에서 가져온 방 갯수+1
		/*Sting title = roomTitle.getText();
		Room room = new Room( );*/
	}
}

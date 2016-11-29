package client.detailView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class detailViewController implements Initializable{
	@FXML private Button btnFriendAdd;
	@FXML private Button friend_add_btnYes;
	@FXML private Button friend_add_btnNo;
	
	
	private static Stage primaryStage;
	public static void setPrimaryStage(Stage primaryStage) {
		detailViewController.primaryStage=primaryStage;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
		
	}


	public void handleBtnDetailView_FriendAdd(ActionEvent event) {

		try {		
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnFriendAdd.getScene().getWindow());
			dialog.setTitle("친구추가");
		
			Parent parent = FXMLLoader.load(getClass().getResource("friend_add.fxml"));
			
			Button btnFriendAddYes = (Button) parent.lookup("#friend_add_btnYes");
			btnFriendAddYes.setOnAction(e->{
				dialog.close();
			});
			
			Button btnFriendAddNo = (Button) parent.lookup("#friend_add_btnNo");
			btnFriendAddNo.setOnAction(e->dialog.close());
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		} catch (IOException e) {}
		
		
	}

}

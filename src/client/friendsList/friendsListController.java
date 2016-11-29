package client.friendsList;

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

public class friendsListController implements Initializable{
	@FXML private Button btnFriendSearch;
	@FXML private Button btnFriendDelete;
	@FXML private Button friend_delete_btnYes;
	@FXML private Button friend_delete_btnNo;

	
	
	private static Stage primaryStage;
	public static void setPrimaryStage(Stage primaryStage) {
		friendsListController.primaryStage=primaryStage;
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
		
	}


	public void handleFriendSearch(ActionEvent event) {
		
		try {		
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnFriendSearch.getScene().getWindow());
			dialog.setTitle("模备八祸");
		
			Parent parent = FXMLLoader.load(getClass().getResource("Search.fxml"));
			
			/*Button btnSearch = (Button) parent.lookup("#btnFriendSearch");
			btnSearch.setOnAction(e->{
				dialog.close();
			});*/
			
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		} catch (Exception e) {}
		
		
	}
	
	
	public void handleFriendDelete(ActionEvent event) {
		
		try {		
			Stage dialog = new Stage(StageStyle.UTILITY);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(btnFriendDelete.getScene().getWindow());
			dialog.setTitle("模备昏力");
			
		
			Parent parent = FXMLLoader.load(getClass().getResource("friend_delete.fxml"));
			
			Button btnYes = (Button) parent.lookup("#friend_delete_btnYes");
			btnYes.setOnAction(e->{dialog.close();});
			
			Button btnNo = (Button) parent.lookup("#friend_delete_btnNo");
			btnNo.setOnAction(e->dialog.close());
			
			Scene scene = new Scene(parent);
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();	
		} catch (Exception e) {}
		
		
	}

}

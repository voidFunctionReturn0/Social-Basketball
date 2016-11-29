package client.boards.comment;

import java.net.URL;
import java.util.ResourceBundle;

import client.AppMain;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;



public class Comment_ItemController implements Initializable {
	public static Comment_ItemController instance;
	
	@FXML Button comment_Rewrite;
	@FXML Button comment_Delete;
	@FXML HBox cItemHbox;

	

	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Label cno = (Label) cItemHbox.lookup("#comment_No");
		AppMain.cno = String.valueOf(cno.getText());
		System.out.println("삭제할 댓글번호" + cno);
		
		comment_Delete.setOnAction(event->handleCommentDelete(event));
		
		
		
		
		
		
	}	
	
	
	public void handleCommentDelete(ActionEvent event) {
		
		
	}
}


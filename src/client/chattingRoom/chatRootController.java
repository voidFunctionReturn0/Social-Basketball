package client.chattingRoom;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class chatRootController implements Initializable {
	@FXML private Button chatMenuBtn;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//chatMenuBtn.setOnAction(e->handlechatMenuBtn(e));
	
	}
	
	public void  handlechatMenuBtn(ActionEvent event) {
		try {
			Parent login= FXMLLoader.load(getClass().getResource("slide.fxml"));
			StackPane root = (StackPane) chatMenuBtn.getScene().getRoot();
			root.getChildren().add(login);
			
			login.setTranslateX(-250);

			Timeline timeline = new Timeline();
			KeyValue keyValue = new KeyValue(login.translateXProperty(), -75);
			KeyFrame keyFrame = new KeyFrame(Duration.millis(1000), keyValue);
			timeline.getKeyFrames().add(keyFrame);
			timeline.play();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

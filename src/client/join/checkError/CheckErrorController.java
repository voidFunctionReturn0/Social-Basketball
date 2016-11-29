package client.join.checkError;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CheckErrorController implements Initializable {
	@FXML private Button okBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		okBtn.setOnAction((e)->{
			Stage dialog = (Stage) okBtn.getScene().getWindow();
			dialog.close();
		});
	}

}

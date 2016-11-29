package client.login;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import client.AppMain;
import client.model.Member;
import javafx.application.Platform;
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

public class LoginController implements Initializable{
	@FXML private Button loginBtn;
	@FXML private Button joinBtn;
	@FXML private Button exitBtn;
	@FXML private TextField idTxtFld;
	@FXML private TextField pwTxtFld;

	public static Stage primaryStage;
	public static void setPrimaryStage(Stage primaryStage) {
		LoginController.primaryStage=primaryStage;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// id,pw�� �Է����� ���� ��츦 �˾Ƴ��� ���� �ʱ�ȭ
		idTxtFld.setText("");
		pwTxtFld.setText("");
	}
	
	public void handleLoginBtn() throws Exception{
		String mid = idTxtFld.getText();
		String mpassword = pwTxtFld.getText();
		
		// ���̵� ��й�ȣ�� �Է����� ���� ���
		if(mid==null || mpassword==null) {
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(LoginController.primaryStage);
			Parent errorDialog =(Parent)FXMLLoader.load(getClass().getResource("idPwIsNull.fxml"));
			Button okBtn = (Button) errorDialog.lookup("#okBtn");
			okBtn.setOnAction((e)->dialog.close());
			
			Scene sc = new Scene(errorDialog);
			dialog.setScene(sc);
			dialog.setResizable(false);
			dialog.setTitle("Ȯ��");
			dialog.show();		
		} else {
			rqstJoinToServ(mid, mpassword);
		}
	}

	public void handleJoinBtn() throws Exception{
		Parent join =(Parent)FXMLLoader.load(getClass().getResource("../join/join.fxml"));
		changeScene(join);
	}

	public void handleExitBtn() throws Exception{
		Stage dialog = new Stage(StageStyle.DECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(LoginController.primaryStage);
		Parent youExit =(Parent)FXMLLoader.load(getClass().getResource("doYouExit.fxml"));
		Button exitOkBtn = (Button) youExit.lookup("#exitOk");
		Button exitCancelBtn =  (Button) youExit.lookup("#exitCancel");
		exitOkBtn.setOnAction((e)->Platform.exit());
		exitCancelBtn.setOnAction((e)->dialog.close());
		
		Scene sc = new Scene(youExit);
		dialog.setScene(sc);
		dialog.setResizable(false);
		dialog.setTitle("Ȯ��");
		dialog.show();		
	}
	
	private void changeScene(Parent root) {
		Scene sc = new Scene(root);
		Stage stage = (Stage) loginBtn.getScene().getWindow();
		stage.setScene(sc);
	}
	
	// ������ �����ϰ� �����ͺ��̽��� �Է¹��� ���̵�/��й�ȣ�� ��ġ�ϴ� �����Ͱ� �ִ��� Ȯ���ϴ� ��û������ ����ޱ�
	private void rqstJoinToServ(String mid, String mpassword) {						
		// ������ �α��ο�û������ ����ޱ�
		Thread thread = new Thread(()->{
			Socket sock = null;
			OutputStream os = null;
			InputStream is = null;
			
			try {
				// ������ ���� ����
				sock = new Socket();
				sock.connect(AppMain.serverAddress);
				
				// ��û command �۽�
				os = sock.getOutputStream();
				os.write("login".getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				// ����ڿ��� �Է¹��� ���̵�/��й�ȣ�� ��û��ü ����� �۽�
				JSONObject loginObj = new JSONObject();
				loginObj.put("mid", mid);
				loginObj.put("mpassword", mpassword);
				os.write(loginObj.toString().getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				// ��ûó���������� ����
				is = sock.getInputStream();
				byte[] datas = new byte[1024];
				int data;
				int index = 0;
				while(true) {
					data = is.read();
					if(data != '\n') {
						datas[index++] = (byte)data;
					} else {
						break;
					}
				}
				String loginResult = new String(datas, 0, index);
				
				// �������ο� ���� ó��(�����̸� ȸ�������� AppMain.member�� �����ϰ� �۸������ ȭ����ȯ, ���и� ���д��̾�α� ����
				if(loginResult.equals("success")) {
					try {
						byte[] memberData = new byte[5000];
						int data2;
						int index2 = 0;
						while(true) {
							data2 = is.read();
							if(data2 != '\n') {
								memberData[index2++] = (byte)data2;
							} else {
								break;
							}
						}
						JSONObject memberJSON = new JSONObject(new String(memberData, 0, index2));
						System.out.println(memberJSON.toString());
						AppMain.member = new Member();
						AppMain.member.setMid(memberJSON.getString("mid"));
						AppMain.member.setMpassword(memberJSON.getString("mpassword"));
						AppMain.member.setMheight(memberJSON.getString("mheight"));
						AppMain.member.setMsex(memberJSON.getString("msex"));
						AppMain.member.setMposition(memberJSON.getString("mposition"));
						AppMain.member.setMarea(memberJSON.getString("marea"));
						AppMain.member.setMlevel(memberJSON.getString("mlevel"));
						System.out.println(memberJSON.getString("mimage")==null);
						if(!memberJSON.getString("mimage").isEmpty()){
							AppMain.member.setMimage(memberJSON.getString("mimage"));
						}
						// �����ȭ������ ȭ����ȯ
						Platform.runLater(()->{
							try {
								Parent waitingRoom = FXMLLoader.load(getClass().getResource("../boards/boardsRoom.fxml"));
								changeScene(waitingRoom);
							} catch(IOException e) {
								e.printStackTrace();
							}
						});
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("fxml������ ã�� �� �����ϴ�.");
					}
				} else if(loginResult.equals("fail")){
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(LoginController.primaryStage);
							Parent errorDialog =(Parent)FXMLLoader.load(getClass().getResource("login_fail.fxml"));
							Button okBtn = (Button) errorDialog.lookup("#okBtn");
							okBtn.setOnAction((e)->dialog.close());
							
							Scene sc = new Scene(errorDialog);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("�α��� ����");
							dialog.show();
						} catch(IOException e) { }
					});
				} else {
					System.out.println("���� ����� �߸��Ǿ����ϴ�.");
				}
			} catch(IOException e) {
				try {
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(LoginController.primaryStage);
							Parent errorDialog =(Parent)FXMLLoader.load(getClass().getResource("conn_fail.fxml"));
							Button okBtn = (Button) errorDialog.lookup("#okBtn");
							okBtn.setOnAction((e2)->dialog.close());
							
							Scene sc = new Scene(errorDialog);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("���� ���� ����");
							dialog.show();
						} catch(IOException e2) {}
					});
					e.printStackTrace();
					os.close();
					is.close();
					sock.close();
				} catch(IOException e2) {
					e2.printStackTrace();
				}
			}
		});
		thread.start();
	}
}

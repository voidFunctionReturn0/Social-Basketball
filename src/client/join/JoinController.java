package client.join;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.regex.*;

import org.json.JSONObject;

import client.AppMain;
import client.login.LoginController;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class JoinController implements Initializable {
	@FXML TextField txtId; //ID �ؽ�Ʈ�ʵ�
	@FXML Button checkId; // �ߺ�üũ ��ư
	@FXML Label idLabel; // id�� ��й�ȣ ���� �˷��ִ� ��
	@FXML Label idLabelOk; //  id�� ��й�ȣ ����� �ۼ� ���� �˷��ִ� ��
	@FXML PasswordField password1; // ��й�ȣ �ʵ�
	@FXML PasswordField password2; // �ι�° ��й�ȣ �ʵ�
	@FXML ImageView imgUpload; // ������ ����
	@FXML ToggleGroup selectSex; // ����, ���� ���Ű
	@FXML ToggleGroup selectLevel; // �ʺ�, �߼�, ��� ���Ű 
	@FXML Button joinBtn; //ȸ������  ��ư
	@FXML CheckBox guardChckBx; // ����
	@FXML CheckBox forwardChckBx; // ������
	@FXML CheckBox centerChckBx; // ����
	@FXML ComboBox<String> cityCheck; // ���� ���� �޺��ڽ�
	@FXML ComboBox<String> boroughCheck; // ���� ���� �޺� �ڽ�
	@FXML ComboBox<String> heightCheck; // Ű ���� �޺� �ڽ�
	@FXML RadioButton maleBtn;
	@FXML RadioButton femaleBtn;
	@FXML RadioButton rookieBtn;
	@FXML RadioButton amateurBtn;
	@FXML RadioButton professBtn;
	
	// �ʼ� ������ ����� �Է��ߴ��� ����
	private boolean isIdOk = false;
	private boolean isPasswordOk = false;
	private boolean isPasswordOk2 = false;
	private boolean isIdOverlapOk = false;
	
	private ObservableList<String> heightList; //
	private ObservableList<String> cityList; //
	private ObservableList<String> boroughList; //
	
	private File selectedFile;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		//���̵�, ��й�ȣ, Ű�� �ؽ�Ʈ�ʵ忡 �Է��� �� ��ȿ�˻��ϱ�
		txtId.setOnKeyReleased((event) ->textIdCheck(event));
		password1.setOnKeyReleased((event) ->passwordCheck(event));
		password2.setOnKeyReleased((event) ->password2Check());
	
		// Ű�޺��ڽ��� �� �ֱ�
		heightList= FXCollections.observableArrayList();
		heightList.add("150cm����");
		heightList.add("160~170cm");
		heightList.add("170~180cm");
		heightList.add("180~190cm");
		heightList.add("190cm�̻�");
		heightList.add("����");
		heightCheck.setItems(heightList);
		
		//����1 �޺��ڽ��� �� �ֱ�
		cityList = FXCollections.observableArrayList();
		cityList.add("�����");
		cityList.add("�λ��");
		cityList.add("��õ��");
		cityCheck.setItems(cityList);
		// ����1�� �����ϸ� ����2�� �ش��ϴ� ������ �� �ֱ�, �� �޾ƿ���
		cityCheck.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("�����")){
					boroughCheck.setValue("");
					addSeoul();
				} else if(newValue.equals("��õ��")){
					boroughCheck.setValue("");
					addIncheon();
				} else{
					boroughCheck.setValue("");
					addBusan();
				}
				boroughCheck.setItems(boroughList);
			}
		});
		
		
		/*// �׽�Ʈ�� ����Ʈ ��
		txtId.setText("test");
		password1.setText("123123");
		password2.setText("123123");
		forwardChckBx.setSelected(true);;
		heightCheck.setValue("150cm����");
		cityCheck.setValue("�����");
		boroughCheck.setValue("���α�");*/
		
	}

	public void addBusan(){
		boroughList.add("�߱�");
		boroughList.add("����");
		boroughList.add("������");
		boroughList.add("�λ�����");
		boroughList.add("������");
		boroughList.add("����");
		boroughList.add("�ϱ�");
		boroughList.add("������");
		boroughList.add("�ؿ�뱸");
		boroughList.add("���屺");
		boroughList.add("���ϱ�");
		boroughList.add("������");
		boroughList.add("������");
		boroughList.add("������");
		boroughList.add("���");
	}
	public void addIncheon(){
		boroughList.add("�߱�");
		boroughList.add("����");
		boroughList.add("������");
		boroughList.add("����");
		boroughList.add("������");
		boroughList.add("������");
		boroughList.add("����");
		boroughList.add("��籸");
		boroughList.add("����");
		boroughList.add("��ȭ��");
	}
	
	public void addSeoul(){
		boroughList.add("������");
		boroughList.add("������");
		boroughList.add("���ϱ�");
		boroughList.add("������");
		boroughList.add("���Ǳ�");
		boroughList.add("������");
		boroughList.add("���α�");
		boroughList.add("�����");
		boroughList.add("������");
		boroughList.add("���빮��");
		boroughList.add("���۱�");
		boroughList.add("������");
		boroughList.add("���빮��");
		boroughList.add("���ʱ�");
		boroughList.add("������");
		boroughList.add("���ϱ�");
		boroughList.add("���ϱ�");
		boroughList.add("���ı�");
		boroughList.add("��������");
		boroughList.add("��걸");
		boroughList.add("����");
		boroughList.add("���α�");
		boroughList.add("�߱�");
		boroughList.add("�߶���");
	}
	
	public void handleBtnSelect(ActionEvent e) { //�̹��� ���ε�
		try { 
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			selectedFile = fileChooser.showOpenDialog(AppMain.primaryStage);
			if (selectedFile == null) return; //������ ���ϰ� �ݾ��� ���
			imgUpload.setImage(new Image(new FileInputStream(selectedFile))); //imageview
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// ���̵� �ҹ���, ���� �ܿ� ���� �־��� ��� && 4�����̻� üũ
	public void textIdCheck(KeyEvent event) { 
		if(txtId.getText().length()<4) {
			idLabelOk.setText("");
			idLabel.setText("���̵� 4���̻� �ۼ����ּ���");
			isIdOk = false;
		} else if(100 < txtId.getText().length()) {
			idLabelOk.setText("");
			idLabel.setText("���̵� 100�� �̳��� �ۼ����ּ���");
			isIdOk = false;
		} else {
			idLabel.setText("");
			char[] ch = event.getText().toCharArray();
			for(int i=0; i<ch.length; i++) {
				int keyCode = ch[i];
				if(!(123>keyCode && 96< keyCode) && !(47<keyCode && 58>keyCode)){
					idLabelOk.setText("");
					idLabel.setText("���̵� �ٽ� �ۼ����ּ���(�ҹ���,���ڸ�)");
					isIdOk = false;
				}
			}
			ch = txtId.getText().toCharArray();
			for(int i=0; i<ch.length; i++) {
				int keyCode = ch[i];
				if(!(123>keyCode && 96< keyCode) && !(47<keyCode && 58>keyCode)){
					idLabelOk.setText("");
					idLabel.setText("���̵� �ٽ� �ۼ����ּ���(�ҹ���,���ڸ�)");
					isIdOk = false;
					break;
				} else {
					idLabel.setText("");
					idLabelOk.setText("���̵�ok");
					isIdOk = true;
				}
			}
		}
	}
	
	public void handleCheckId() throws Exception{ //�ߺ�üũ ��ư
		if(isIdOk == false){
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(LoginController.primaryStage);
			Parent root =(Parent)FXMLLoader.load(getClass().getResource("checkIdError.fxml"));
			Scene sc = new Scene(root);
			dialog.setScene(sc);
			dialog.setResizable(false);
			dialog.setTitle("Ȯ��");
			dialog.show();
		} else {	
			Thread thread = new Thread() {
				@Override
				public void run() {
					Socket sock = null;
					try {
						sock = new Socket();
						sock.connect(AppMain.serverAddress);
						OutputStream os = sock.getOutputStream();
						os.write("idCheck".getBytes());
						os.write("\n".getBytes());
						os.write(txtId.getText().getBytes());
						os.write("\n".getBytes());
						os.flush();
						
						InputStream is = sock.getInputStream();
						byte[] datas = new byte[100];
						byte index = 0;
						while(true) {
							int data = is.read();
							if(data != '\n') {
								datas[index] = (byte)data;
								index++;
							} else {
								break;
							}
						}
						String idCheckResult = new String(datas, 0, index);	
						
						Platform.runLater(()->{
							try {
							if(idCheckResult.equals("success")) {
								Stage dialog = new Stage(StageStyle.DECORATED);
								dialog.initModality(Modality.WINDOW_MODAL);
								dialog.initOwner(LoginController.primaryStage);
								Parent joinCancel =(Parent)FXMLLoader.load(getClass().getResource("checkId.fxml"));
								Button idOkBtn = (Button) joinCancel.lookup("#IdOk");
								idOkBtn.setOnAction((e)->dialog.close());	
								Scene sc = new Scene(joinCancel);
								dialog.setScene(sc);
								dialog.setResizable(false);
								dialog.setTitle("Ȯ��");
								dialog.show();
								 isIdOverlapOk=true;
							} else {
								Stage dialog = new Stage(StageStyle.DECORATED);
								dialog.initModality(Modality.WINDOW_MODAL);
								dialog.initOwner(LoginController.primaryStage);
								Parent joinCancel = (Parent)FXMLLoader.load(getClass().getResource("overlapId.fxml"));
								Button idOverlapBtn = (Button) joinCancel.lookup("#IdCancelOk");
								idOverlapBtn.setOnAction((e)->dialog.close());	
								Scene sc = new Scene(joinCancel);
								dialog.setScene(sc);
								dialog.setResizable(false);
								dialog.setTitle("Ȯ��");
								dialog.show();
								isIdOverlapOk=false;
							}
							}catch (Exception e) {
								
							}
						});
					} catch (IOException e) {
						handleIOException(e);
						return;
					}finally {
						try {
							sock.close();
						} catch (IOException e) {}
					}
				}
			};
			thread.start();
		}
	}	
	
	public void passwordCheck(KeyEvent event) { //��й�ȣ�� ���� �־��� ��� üũ
		if(password1.getText().length()<6) {
			idLabelOk.setText("");
			idLabel.setText("��й�ȣ�� 6���̻� �ۼ����ּ���");
			isPasswordOk = false;
		} else if(100 < password1.getText().length()) {
			idLabelOk.setText("");
			idLabel.setText("��й�ȣ�� 100�� �̳��� �ۼ����ּ���");
			isPasswordOk = false;
		} else {
			idLabel.setText("");
			Pattern p = Pattern.compile("^[a-zA-Z0-9a-zA-Z0-9!,@,#,$,%,^,&,*,?,_,~]*$");
			String pass =event.getText();
			Matcher m = p.matcher(pass);
			if (m.find()){
				idLabel.setText("");
				idLabelOk.setText("ok");
				isPasswordOk = true;
			} else {
				idLabelOk.setText("");
				idLabel.setText("��й�ȣ�� ���ĺ�, ����, Ư�����ڸ� �����մϴ�.");
				isPasswordOk = false;
			}
			//����, ����, Ư�������� �������� Ȯ��
			p = Pattern.compile("^[a-zA-Z0-9a-zA-Z0-9!,@,#,$,%,^,&,*,?,_,~]*$");
			pass =password1.getText();
			m = p.matcher(pass);
			if (m.find()){
				idLabel.setText("");
				idLabelOk.setText("�����ȣok");
				isPasswordOk = true;
			} else {
				idLabelOk.setText("");
				idLabel.setText("��й�ȣ�� ���ĺ�, ����, Ư�����ڸ� �����մϴ�.");
				isPasswordOk = false;
			}
		}
	}

	public void password2Check(){
		if(password2.getText().equals(password1.getText())){
			idLabel.setText("");
			idLabelOk.setText("��й�ȣ�� ��ġ �մϴ�.");
			isPasswordOk2 = true;
		} else{
			idLabelOk.setText("");
			idLabel.setText("��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
			isPasswordOk2 = false;
		}
	}
	

	
	//�����ϱ� ��ư �̺�Ʈ ó��
	public void handleJoinBtn() { 
		if(inputCheck() == false) {
			return;
		}
		
		Thread threadOutput = new Thread(()->{
			try {
				// ������ ����
				Socket sock=new Socket();
				sock.connect(AppMain.serverAddress);
				
				// command ������
				OutputStream os = sock.getOutputStream();
				os.write("join".getBytes());
				os.write("\n".getBytes());
				
				// �Է¹��� ������ Member��ü ���� ������
				JSONObject joinData = mkMemDataByInput();
				os.write(joinData.toString().getBytes());
				os.write("\n".getBytes());
				
				// �������� �ִ� ��� �������ϵ� ������
				if(selectedFile!=null) {
					FileInputStream fis = new FileInputStream(selectedFile);
					byte[] datas = new byte[1024];
					int totalReadNum = 0;
					int readByteNum = -1;
					while(true) {
						readByteNum = fis.read(datas);
						os.write(datas, 0 , readByteNum);
						
						// ������ �������Ϻ����� ����Ʈ ũ�Ⱑ ������ ����Ʈ ũ��� ������ ���Ϻ����� ����
						totalReadNum += readByteNum;
						if(totalReadNum == selectedFile.length()) {
							os.flush();
							break;
						}
					}
					fis.close();
				}				
				os.flush();
				
				// �����ϱ� ��ûó����� �ޱ�
				InputStream is = sock.getInputStream();
				byte[] datas = new byte[1024];
				int index = 0;
				while(true) {
					int data = is.read();
					if(data != '\n') {
						datas[index] = (byte)data;
						index++;
					} else {
						break;
					}
				}
				String joinResult = new String(datas, 0, index);	
				
				// ��ûó������� ���� ó��(�����̸� �������̾�α� ���� �α���ȭ������ �Ѿ��, ���и�
				Platform.runLater(()->{
					if(joinResult.equals("success")) {
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(joinBtn.getScene().getWindow());
							Parent joinOk;
							joinOk = FXMLLoader.load(getClass().getResource("you_joined.fxml"));
							Button joinOkBtn = (Button) joinOk.lookup("#joinOkCheckBtn");
							
							// ���ԿϷ�Ȯ�δ��̾�α׸� ������ �����ϱ�ȭ�鿡�� �α���ȭ������ �Ѿ���� ó��
							joinOkBtn.setOnAction((e)->{
								dialog.close();
								showLogin();		
							});
							Scene sc = new Scene(joinOk);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("Ȯ��");
							dialog.show();
						} catch (Exception e1) {}
					
					} else {
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(joinBtn.getScene().getWindow());
							Parent joinFail;
							joinFail = FXMLLoader.load(getClass().getResource("fail_join.fxml"));
							Button finishBtn = (Button) joinFail.lookup("#finishBtn");
							
							finishBtn.setOnAction((e)->{
								dialog.close();		
							});
							Scene sc = new Scene(joinFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("Ȯ��");
							dialog.show();
						} catch(IOException e) {	}
					}
				});
				
			} catch(IOException e) {
				handleIOException(e);
				return;
			}
		});
		threadOutput.start();
	}

	public void joinCancel() throws Exception { //ȸ�������� ��ҹ�ư
		Stage dialog = new Stage(StageStyle.DECORATED);
		dialog.initModality(Modality.WINDOW_MODAL);
		dialog.initOwner(LoginController.primaryStage);
		Parent joinCancel =(Parent)FXMLLoader.load(getClass().getResource("JoinCancel.fxml"));
		Button cancelOkBtn = (Button) joinCancel.lookup("#JoinCancelOkBtn");
		
		cancelOkBtn.setOnAction((e)->{
			dialog.close();
			showLogin();
		});	
		
		Scene sc = new Scene(joinCancel);
		dialog.setScene(sc);
		dialog.setResizable(false);
		dialog.setTitle("Ȯ��");
		dialog.show();
	}
	
	// UIȭ���� �α���ȭ������ �����ϴ� �޼ҵ�
	public void showLogin() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../login/login(first).fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) joinBtn.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			
		} catch(IOException e) {
			e.printStackTrace();
			System.out.print("fxml������ ã�� �� �����ϴ�.");
		}
	}
	
	
	private void handleIOException(IOException e) {
		e.printStackTrace();
		System.out.println("������ ������ �� �����ϴ�.");
		return;
	}
	
	// ����ڰ� �Է��� ������ JSONObect �����Ͽ� ��ȯ�ϴ� �޼ҵ� (Make Member Data By Input information)
	private JSONObject mkMemDataByInput() {	
		String msex = findSex();
		String mposition = findPosition();
		String mlevel = findLevel();
		String mimage = "";
		if (selectedFile != null) {
			mimage=selectedFile.getName();
		}
		
		
		JSONObject member = new JSONObject();
		member.put("mid", txtId.getText());
		member.put("mpassword", password2.getText());
		String mheight=heightCheck.getSelectionModel().getSelectedItem();
		member.put("mheight", mheight);
		member.put("msex", msex);
		member.put("mposition", mposition);
		String mcity=cityCheck.getSelectionModel().getSelectedItem();
		String mborough=boroughCheck.getSelectionModel().getSelectedItem();
		member.put("marea", mcity+" "+mborough);
		member.put("mlevel", mlevel);
		member.put("mimage", mimage);
		if(!mimage.equals("")) {
			member.put("fileSize", selectedFile.length());
		} else {
			member.put("fileSize", 0);
		}
		return member;
	}
	
	// ���� ������ư �� ������� ���õǾ����� ã��
	private String findSex() {
		if(maleBtn.isSelected()) {
			return maleBtn.getText();
		} else if(femaleBtn.isSelected()) {
			return femaleBtn.getText();
		} else {
			return new String("?");
		}
	}
	
	// ������ üũ�ڽ� �� ������� ���õǾ����� �˾Ƴ���
	private String findPosition() {
		StringBuilder position = new StringBuilder("");
		if(guardChckBx.isSelected()) {
			position.append("G");
		}
		if(forwardChckBx.isSelected()) {
			position.append("F");
		}
		if(centerChckBx.isSelected()) {
			position.append("C");
		}
		return new String(position);
		
	}
	
	// �Ƿ� ���� ��ư �� ��� ���� ���õǾ����� �˾Ƴ���
	private String findLevel() {
		if(rookieBtn.isSelected()) {
			return rookieBtn.getText();
		} else if(amateurBtn.isSelected()) {
			return amateurBtn.getText();
		} else if(professBtn.isSelected()) {
			return professBtn.getText();
		} else {
			return new String("?");
		}
	}
	
	private boolean inputCheck() {
		// ���̵� �߸��Էµ� ��� �����޽��� ���̾�α� ���� ID�ؽ�Ʈ�ʵ� ���� & false ����
		if(isIdOk == false) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkIdError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();
				
				txtId.setText(null);
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			return false;
		}
		
		// ���̵� �ߺ��˻翡 �ɸ� ��� �����޽��� ���̾�α� ���� ID�ؽ�Ʈ�ʵ� ���� & false ����
		if(isIdOverlapOk == false) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkIdOverlapError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();
		
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		// ��й�ȣ�� �߸� �Էµ� ��� �����޽��� ���̾�α� ���� ��й�ȣ �ؽ�Ʈ�ʵ� ���� & false ����
		if(isPasswordOk == false) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkPasswordError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();
				
				password1.setText(null);
				password2.setText(null);
		
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		// ��й�ȣȮ���� �߸� �Էµ� ��� �����޽��� ���̾�α� ���� ��й�ȣ �ؽ�Ʈ�ʵ� ���� & false ����
		if(isPasswordOk2 == false) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkPasswordError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();
				
				password1.setText(null);
				password2.setText(null);
		
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		// ������ üũ�ڽ��� �Էµ��� ���� ��� �����޽��� ���̾�α� ���� false ����
		if(findPosition().equals("")) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkPositionError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();

			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		//Ű(height) �޺��ڽ��� �Էµ��� ���� ��� �����޽��� ���̾�α� ���� false ����
		if(heightCheck.getSelectionModel().getSelectedItem() == null) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkHeightError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();

			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		// ��(city) �޺��ڽ��� �Էµ��� ���� ��� �����޽��� ���̾�α� ���� false ����
		if(cityCheck.getSelectionModel().getSelectedItem() == null) {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkCityError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();
				
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		// ��(borough) �޺��ڽ��� �Էµ��� ���� ��� �����޽��� ���̾�α� ���� false ����
		if(boroughCheck.getSelectionModel().getSelectedItem() == "") {
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(joinBtn.getScene().getWindow());
				Parent joinOk = FXMLLoader.load(getClass().getResource("checkError/checkBoroughError.fxml"));
				Scene sc = new Scene(joinOk);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Error");
				dialog.show();
				
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("fxml������ ã�� �� �����ϴ�.");
			}
			
			return false;
		}
		
		return true;
	}
	
	// �������� ��ư�̺�Ʈ - ����Ʈ�̹����� �̹����信 ���ε��ϰ� selectFile���� null�� ����
	public void handleDeletePictureBtn(ActionEvent e) {
		try {
			imgUpload.setImage(new Image(new FileInputStream(AppMain.defaultImage.getPath())));
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		selectedFile = null;
	}
}

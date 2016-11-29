package client.slidingMenu_main.adjustProfile;

import java.io.*;
import java.net.*;
import java.util.ResourceBundle;
import java.util.regex.*;

import org.json.JSONObject;

import client.AppMain;
import client.model.Member;
import javafx.application.Platform;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.stage.FileChooser.ExtensionFilter;

public class AdjustProfileController implements Initializable {
	@FXML private ImageView imageView;
	@FXML private TextField idTxtFld;
	@FXML private PasswordField pwTxtFld1;
	@FXML private PasswordField pwTxtFld2;
	@FXML private Button changePictureBtn;
	@FXML private ToggleGroup sexTG;
	@FXML private ToggleGroup levelTG;
	@FXML private RadioButton maleBtn;
	@FXML private RadioButton femaleBtn;
	@FXML private CheckBox guardChckBox;
	@FXML private CheckBox forwardChckBox;
	@FXML private CheckBox centerChckBox;
	@FXML private RadioButton rookieBtn;
	@FXML private RadioButton amateurBtn;
	@FXML private RadioButton professBtn;
	@FXML private ComboBox<String> heightComboBox;
	@FXML private ComboBox<String> cityComboBox;
	@FXML private ComboBox<String> boroughComboBox;
	@FXML private Button finishBtn;
	@FXML private Button cancelBtn;
	private ObservableList<String> heightList; 
	private ObservableList<String> cityList; 
	private ObservableList<String> boroughList; 
	private File selectedFile;
	private boolean isPictureDeleted = false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// �Է°˻縦 ���� �ʱ�ȭ
		pwTxtFld1.setText(null);
		pwTxtFld2.setText(null);
		
		// Ű(hight) �޺��ڽ��� �� �ֱ�
		heightList= FXCollections.observableArrayList();
		heightList.add("150cm����");
		heightList.add("160~170cm");
		heightList.add("170~180cm");
		heightList.add("180~190cm");
		heightList.add("190cm�̻�");
		heightList.add("����");
		heightComboBox.setItems(heightList);
		
		//����(��) �޺��ڽ��� �� �ֱ�
		cityList = FXCollections.observableArrayList();
		cityList.add("�����");
		cityList.add("�λ��");
		cityList.add("��õ��");
		cityComboBox.setItems(cityList);
		
		// ����(��)�� �����ϸ� ����(��)�� �ش��ϴ� ������ �� �ֱ�, �� �޾ƿ���
		cityComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("�����")){
					boroughComboBox.setValue("");
					addSeoul();
				} else if(newValue.equals("��õ��")){
					boroughComboBox.setValue("");
					addIncheon();
				} else{
					boroughComboBox.setValue("");
					addBusan();
				}
				boroughComboBox.setItems(boroughList);
			}
		});		
		
		// AppMain.member ������� ȸ�������� ����
		if(AppMain.member!=null) {
			// id ����
			idTxtFld.setText(AppMain.member.getMid());
			idTxtFld.setDisable(true);
			// ���� ����
			setSex();			
			// ������ ����
			setPosition();
			// Ű ����
			heightComboBox.setValue(AppMain.member.getMheight());
			// ���� ����
			String area[] = AppMain.member.getMarea().split(" ");
			cityComboBox.setValue(area[0]);
			boroughComboBox.setValue(area[1]);
			// �Ƿ� ����
			setLevel();
			
		}
		
		// ������ ������ ������ �������� AppMain.member�� mimage�� �ش��ϴ� ���� �޾ƿͼ� ����
		if(AppMain.member.getMimage()!=null) {
			try {
				Socket sock = new Socket();
				sock.connect(AppMain.serverAddress);
				
				OutputStream os = sock.getOutputStream();
				os.write("getPicture".getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				os.write(AppMain.member.getMimage().getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				InputStream is = sock.getInputStream();
				Image profile = new Image(is);
				imageView.setImage(profile);
					
				is.close();
				os.close();
				sock.close();
				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
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
	
	public void handlefinishBtn(ActionEvent e) {
		Thread thread = new Thread(()->{
			Socket sock = null;
			OutputStream os = null;
			try {
				// �ʼ��Է°�(��й�ȣ, ��й�ȣȮ��, ����(��)�޺��ڽ�)�� �ԷµǾ����� Ȯ���ؼ� �Է¾ȵ� ���� ������ ���� ���̾�α� ����
				if(pwTxtFld1.getText()==null || pwTxtFld2.getText()==null || boroughComboBox.getValue()=="") {
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent adjustFail = FXMLLoader.load(getClass().getResource("fail_adjust.fxml"));
							Button okBtn = (Button) adjustFail.lookup("#okBtn");
							okBtn.setOnAction((e2)->{
								dialog.close();		
							});
							Scene sc = new Scene(adjustFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("������������");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				// ��й�ȣ, ��й�ȣȮ���� ���ǰ˻縦 ����ϴ��� Ȯ���ؼ� ��� ���ϸ� ���� ���̾�α�
				String pw = pwTxtFld1.getText();
				// ���̰˻�(4����~100����)
				if(pw.length() < 4 || 100 < pw.length()) {
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent adjustFail = FXMLLoader.load(getClass().getResource("checkPasswordLength.fxml"));
							Button okBtn = (Button) adjustFail.lookup("#okBtn");
							okBtn.setOnAction((e2)->{
								dialog.close();		
							});
							Scene sc = new Scene(adjustFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("������������");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				// ��й�ȣ�� ����,���ĺ�,Ư�����ڷ� �̷�������� �˻�
				Pattern p = Pattern.compile("^[a-zA-Z0-9a-zA-Z0-9!,@,#,$,%,^,&,*,?,_,~]*$");
				Matcher m = p.matcher(pw);
				if(m.find()==false){
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent adjustFail = FXMLLoader.load(getClass().getResource("checkPasswordChar.fxml"));
							Button okBtn = (Button) adjustFail.lookup("#okBtn");
							okBtn.setOnAction((e2)->{
								dialog.close();		
							});
							Scene sc = new Scene(adjustFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("������������");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				// ��й�ȣ-��й�ȣȮ�� ���� ���� ������ �˻�
				if(!pwTxtFld1.getText().equals(pwTxtFld2.getText())){
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent adjustFail = FXMLLoader.load(getClass().getResource("checkPasswordEqual.fxml"));
							Button okBtn = (Button) adjustFail.lookup("#okBtn");
							okBtn.setOnAction((e2)->{
								dialog.close();		
							});
							Scene sc = new Scene(adjustFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("������������");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				
				// ������ ȸ������ ������ ��û
				sock = new Socket();
				sock.connect(AppMain.serverAddress);
				os = sock.getOutputStream();
				os.write("updateProfile".getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				// ������ Member���� ������
				JSONObject memberJSON = inputToJSON();
				os.write(memberJSON.toString().getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				// �̹��������� ����Ǿ����� �̹������ϵ� ������
				if(selectedFile != null) {
					// �̹������� ũ�� ������
					Long fileSize = new Long(selectedFile.length());
					os.write(fileSize.toString().getBytes());
					os.write("\n".getBytes());
					os.flush();
					
//					System.out.println("�̹���ũ�⺸��");
					
					FileInputStream fis = new FileInputStream(selectedFile);
					byte[] datas = new byte[1024];
					int readByteNum = -1;
					int totalByteNum = 0;
					while(true) {
						readByteNum = fis.read(datas);
						os.write(datas, 0, readByteNum);
						totalByteNum += readByteNum;
						
						// ������ ����ũ�⸸ŭ ������������ ��� ��������
						if(totalByteNum == selectedFile.length()) {
							os.flush();
							break;
						}
					}
					fis.close();
					
//					System.out.println("�̹�������");
				}
				
				// ��ûó�� ����� �ޱ�
				InputStream is = sock.getInputStream();
				byte[] resultByte = new byte[1024];
				int idx = 0;
				int data;
				while(true) {
					data = is.read();
					if(data != '\n') {
						resultByte[idx++] = (byte)data;
//						System.out.println(idx);
					} else {
						break;
					}
				}
				String result = new String(resultByte, 0, idx);
				
//				System.out.println("�������");
				
				// ����� ���� ó��
				Platform.runLater(()->{
					// update ����
					if(result.equals("success")) {
						try {
							// AppMain.member ����
							AppMain.member = inputToMember();
							
							// ���������Ϸ� ���̾�α� ����
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent root = FXMLLoader.load(getClass().getResource("you_adjusted.fxml"));
							Button okBtn = (Button) root.lookup("#okBtn");
							
							// �����������̾�α׸� ������ ��������ȭ�鿡�� �����̵�޴�ȭ������ �Ѿ���� ó��
							okBtn.setOnAction((e2)->{
								dialog.close();
								Stage adjustStage = (Stage) finishBtn.getScene().getWindow();
								adjustStage.close();
								
								try {
									Parent refreshSlidingMenu = FXMLLoader.load(getClass().getResource("../../boards/boardsRoom.fxml"));
									Scene scene = new Scene(refreshSlidingMenu);
									AppMain.primaryStage.setScene(scene);
									AppMain.primaryStage.show();
								} catch(IOException e3) {
									e3.printStackTrace();
								}
								
							});
							Scene sc = new Scene(root);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("�������� �Ϸ�");
							dialog.show();
						} catch (IOException e2) {
						}
					
					// update ����
					} else {
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent adjustFail = FXMLLoader.load(getClass().getResource("fail_adjust.fxml"));
							Button okBtn = (Button) adjustFail.lookup("#okBtn");
							okBtn.setOnAction((e2)->{
								dialog.close();		
							});
							Scene sc = new Scene(adjustFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("Ȯ��");
							dialog.show();
							
						} catch(IOException e2) {	}	
					}	
				});
	
			} catch(IOException e2) {
				e2.printStackTrace();
			}
		});
		thread.start();
	}
	
	// �������� ��ư�̺�Ʈ
	public void handleChangePictureBtn(ActionEvent e) {
		try {
			// �������� ����
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			selectedFile = fileChooser.showOpenDialog(changePictureBtn.getScene().getWindow());
			
			// ������ ���� �ʰ� ������� ���
			if(selectedFile == null) {
				return;
			}
			
			imageView.setImage(new Image(new FileInputStream(selectedFile)));
			isPictureDeleted = false;
			
		
		} catch(IOException e2) {
			e2.printStackTrace();
		}
	}
	
	// ��� ��ư�̺�Ʈ
	public void handleCancelBtn(ActionEvent e) {
		try {
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(finishBtn.getScene().getWindow());
			Parent adjustFail = FXMLLoader.load(getClass().getResource("doYouCancel.fxml"));
			Button yesBtn = (Button) adjustFail.lookup("#yesBtn");
			Button noBtn = (Button) adjustFail.lookup("#noBtn");
			yesBtn.setOnAction((e2)->{
				dialog.close();
				((Stage)cancelBtn.getScene().getWindow()).close();
				
			});
			noBtn.setOnAction((e2)->{
				dialog.close();		
			});
			Scene sc = new Scene(adjustFail);
			dialog.setScene(sc);
			dialog.setResizable(false);
			dialog.setTitle("Ȯ��");
			dialog.show();
		} catch(IOException e2) {
			e2.printStackTrace();
		}
	}
	
	// AppMain.member�� ���� ������� ������ư�� ����
	private void setSex() {
		if(AppMain.member.getMsex().equals("����")) {
			maleBtn.setSelected(true);
		} else {
			femaleBtn.setSelected(true);
		}
	}
	
	// AppMain.member�� ������ ������� üũ�ڽ��� ����
	private void setPosition() {
		if(AppMain.member.getMposition()!=null) {
			if(AppMain.member.getMposition().contains("G")) {
				guardChckBox.setSelected(true);
			}
			if(AppMain.member.getMposition().contains("F")) {
				forwardChckBox.setSelected(true);
			}
			if(AppMain.member.getMposition().contains("C")) {
				centerChckBox.setSelected(true);
			}
		}
	}
	
	// AppMain.member�� �Ƿ� ������� üũ�ڽ��� ����
	private void setLevel() {
		if(AppMain.member.getMlevel().equals("�ʺ�")) {
			rookieBtn.setSelected(true);
		} else if(AppMain.member.getMlevel().equals("�߼�")) {
			amateurBtn.setSelected(true);
		} else {
			professBtn.setSelected(true);
		}
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
		if(guardChckBox.isSelected()) {
			position.append("G");
		}
		if(forwardChckBox.isSelected()) {
			position.append("F");
		}
		if(centerChckBox.isSelected()) {
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
	
	// ����ڿ��� �Է¹��� ������ JSON��ü �����
	private JSONObject inputToJSON() {
		JSONObject memberJSON = new JSONObject();
		memberJSON.put("mid", idTxtFld.getText());
		memberJSON.put("mpassword", pwTxtFld1.getText());
		memberJSON.put("mheight", heightComboBox.getSelectionModel().getSelectedItem());
		memberJSON.put("msex", findSex());
		memberJSON.put("mposition", findPosition());
		memberJSON.put("marea", cityComboBox.getSelectionModel().getSelectedItem() + " " + boroughComboBox.getSelectionModel().getSelectedItem());
		memberJSON.put("mlevel", findLevel());
		
		// �̹��������� ���濩�ο� ���� ó��(�����Ǿ����� null ����, ������� �ʾ����� ������ �̹������ϸ����� ����)
		if(isPictureDeleted == true) {
			memberJSON.put("mimage", "");
		}
		else if(selectedFile == null) {
			memberJSON.put("mimage", AppMain.member.getMimage());
		} else {
			memberJSON.put("mimage", selectedFile.getName());
		}
		
		return memberJSON;
	}
	
	// ����ڿ��� �Է¹��� ������ Member��ü �����
	private Member inputToMember() {
		Member member = new Member();
		member.setMid(idTxtFld.getText());
		member.setMpassword(pwTxtFld1.getText());
		member.setMheight(heightComboBox.getSelectionModel().getSelectedItem());
		member.setMsex(findSex());
		member.setMposition(findPosition());
		member.setMarea(cityComboBox.getSelectionModel().getSelectedItem() + " " + boroughComboBox.getSelectionModel().getSelectedItem());
		member.setMlevel(findLevel());
		
		// �̹��������� ���濩�ο� ���� ó��(�����Ǿ����� null ����, ������� �ʾ����� ������ �̹������ϸ����� ����)
		if(isPictureDeleted == true) {
			member.setMimage("");
		}
		else if(selectedFile == null) {
			member.setMimage(AppMain.member.getMimage());
		} else {
			member.setMimage(selectedFile.getName());
		}
		
		return member;
	}
	
	// �������� ��ư�̺�Ʈ
	public void handleDeletePictureBtn() {
		try {
			imageView.setImage(new Image(new FileInputStream(new File(AdjustProfileController.class.getResource("../../images/profile.png").toURI()))));
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		selectedFile = null;
		isPictureDeleted = true;
	}
}

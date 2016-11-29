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
		// 입력검사를 위한 초기화
		pwTxtFld1.setText(null);
		pwTxtFld2.setText(null);
		
		// 키(hight) 콤보박스에 글 넣기
		heightList= FXCollections.observableArrayList();
		heightList.add("150cm이하");
		heightList.add("160~170cm");
		heightList.add("170~180cm");
		heightList.add("180~190cm");
		heightList.add("190cm이상");
		heightList.add("비설정");
		heightComboBox.setItems(heightList);
		
		//지역(시) 콤보박스에 글 넣기
		cityList = FXCollections.observableArrayList();
		cityList.add("서울시");
		cityList.add("부산시");
		cityList.add("인천시");
		cityComboBox.setItems(cityList);
		
		// 지역(시)을 선택하면 지역(구)에 해당하는 지역의 글 넣기, 값 받아오기
		cityComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("서울시")){
					boroughComboBox.setValue("");
					addSeoul();
				} else if(newValue.equals("인천시")){
					boroughComboBox.setValue("");
					addIncheon();
				} else{
					boroughComboBox.setValue("");
					addBusan();
				}
				boroughComboBox.setItems(boroughList);
			}
		});		
		
		// AppMain.member 정보대로 회원정보를 세팅
		if(AppMain.member!=null) {
			// id 세팅
			idTxtFld.setText(AppMain.member.getMid());
			idTxtFld.setDisable(true);
			// 성별 세팅
			setSex();			
			// 포지션 세팅
			setPosition();
			// 키 세팅
			heightComboBox.setValue(AppMain.member.getMheight());
			// 지역 세팅
			String area[] = AppMain.member.getMarea().split(" ");
			cityComboBox.setValue(area[0]);
			boroughComboBox.setValue(area[1]);
			// 실력 세팅
			setLevel();
			
		}
		
		// 저장한 사진이 있으면 서버에서 AppMain.member의 mimage에 해당하는 사진 받아와서 세팅
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
		boroughList.add("중구");
		boroughList.add("동구");
		boroughList.add("영도구");
		boroughList.add("부산진구");
		boroughList.add("동래구");
		boroughList.add("남구");
		boroughList.add("북구");
		boroughList.add("강서구");
		boroughList.add("해운대구");
		boroughList.add("기장군");
		boroughList.add("사하구");
		boroughList.add("금정구");
		boroughList.add("연제구");
		boroughList.add("수영구");
		boroughList.add("사상구");
	}
	public void addIncheon(){
		boroughList.add("중구");
		boroughList.add("동구");
		boroughList.add("웅진군");
		boroughList.add("남구");
		boroughList.add("연수구");
		boroughList.add("남동구");
		boroughList.add("부평구");
		boroughList.add("계양구");
		boroughList.add("서구");
		boroughList.add("강화군");
	}
	
	public void addSeoul(){
		boroughList.add("강남구");
		boroughList.add("강동구");
		boroughList.add("강북구");
		boroughList.add("강서구");
		boroughList.add("관악구");
		boroughList.add("광진구");
		boroughList.add("구로구");
		boroughList.add("노원구");
		boroughList.add("도봉구");
		boroughList.add("동대문구");
		boroughList.add("동작구");
		boroughList.add("마포구");
		boroughList.add("서대문구");
		boroughList.add("서초구");
		boroughList.add("성동구");
		boroughList.add("강북구");
		boroughList.add("성북구");
		boroughList.add("송파구");
		boroughList.add("영등포구");
		boroughList.add("용산구");
		boroughList.add("은평구");
		boroughList.add("종로구");
		boroughList.add("중구");
		boroughList.add("중랑구");
	}
	
	public void handlefinishBtn(ActionEvent e) {
		Thread thread = new Thread(()->{
			Socket sock = null;
			OutputStream os = null;
			try {
				// 필수입력값(비밀번호, 비밀번호확인, 지역(구)콤보박스)이 입력되었는지 확인해서 입력안된 것이 있으면 오류 다이얼로그 띄우기
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
							dialog.setTitle("정보수정오류");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				// 비밀번호, 비밀번호확인이 조건검사를 통과하는지 확인해서 통과 못하면 오류 다이얼로그
				String pw = pwTxtFld1.getText();
				// 길이검사(4글자~100글자)
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
							dialog.setTitle("정보수정오류");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				// 비밀번호가 숫자,알파벳,특수문자로 이루어졌는지 검사
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
							dialog.setTitle("정보수정오류");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				// 비밀번호-비밀번호확인 쌍이 같은 값인지 검사
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
							dialog.setTitle("정보수정오류");
							dialog.show();
						} catch(IOException e2) { }
					});
					return;
				}
				
				
				// 서버에 회원정보 수정을 요청
				sock = new Socket();
				sock.connect(AppMain.serverAddress);
				os = sock.getOutputStream();
				os.write("updateProfile".getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				// 수정할 Member정보 보내기
				JSONObject memberJSON = inputToJSON();
				os.write(memberJSON.toString().getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				// 이미지파일이 변경되었으면 이미지파일도 보내기
				if(selectedFile != null) {
					// 이미지파일 크기 보내기
					Long fileSize = new Long(selectedFile.length());
					os.write(fileSize.toString().getBytes());
					os.write("\n".getBytes());
					os.flush();
					
//					System.out.println("이미지크기보냄");
					
					FileInputStream fis = new FileInputStream(selectedFile);
					byte[] datas = new byte[1024];
					int readByteNum = -1;
					int totalByteNum = 0;
					while(true) {
						readByteNum = fis.read(datas);
						os.write(datas, 0, readByteNum);
						totalByteNum += readByteNum;
						
						// 보내는 파일크기만큼 데이터전송한 경우 전송중지
						if(totalByteNum == selectedFile.length()) {
							os.flush();
							break;
						}
					}
					fis.close();
					
//					System.out.println("이미지보냄");
				}
				
				// 요청처리 결과를 받기
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
				
//				System.out.println("결과받음");
				
				// 결과에 따른 처리
				Platform.runLater(()->{
					// update 성공
					if(result.equals("success")) {
						try {
							// AppMain.member 갱신
							AppMain.member = inputToMember();
							
							// 정보수정완료 다이얼로그 띄우기
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(finishBtn.getScene().getWindow());
							Parent root = FXMLLoader.load(getClass().getResource("you_adjusted.fxml"));
							Button okBtn = (Button) root.lookup("#okBtn");
							
							// 정보수정다이얼로그를 닫으면 정보수정화면에서 슬라이드메뉴화면으로 넘어가도록 처리
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
							dialog.setTitle("정보수정 완료");
							dialog.show();
						} catch (IOException e2) {
						}
					
					// update 실패
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
							dialog.setTitle("확인");
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
	
	// 사진변경 버튼이벤트
	public void handleChangePictureBtn(ActionEvent e) {
		try {
			// 사진파일 고르기
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			selectedFile = fileChooser.showOpenDialog(changePictureBtn.getScene().getWindow());
			
			// 파일을 고르지 않고 취소했을 경우
			if(selectedFile == null) {
				return;
			}
			
			imageView.setImage(new Image(new FileInputStream(selectedFile)));
			isPictureDeleted = false;
			
		
		} catch(IOException e2) {
			e2.printStackTrace();
		}
	}
	
	// 취소 버튼이벤트
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
			dialog.setTitle("확인");
			dialog.show();
		} catch(IOException e2) {
			e2.printStackTrace();
		}
	}
	
	// AppMain.member의 성별 정보대로 라디오버튼을 세팅
	private void setSex() {
		if(AppMain.member.getMsex().equals("남성")) {
			maleBtn.setSelected(true);
		} else {
			femaleBtn.setSelected(true);
		}
	}
	
	// AppMain.member의 포지션 정보대로 체크박스를 세팅
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
	
	// AppMain.member의 실력 정보대로 체크박스를 세팅
	private void setLevel() {
		if(AppMain.member.getMlevel().equals("초보")) {
			rookieBtn.setSelected(true);
		} else if(AppMain.member.getMlevel().equals("중수")) {
			amateurBtn.setSelected(true);
		} else {
			professBtn.setSelected(true);
		}
	}
	
	// 성별 라디오버튼 중 어느것이 선택되었는지 찾기
	private String findSex() {
		if(maleBtn.isSelected()) {
			return maleBtn.getText();
		} else if(femaleBtn.isSelected()) {
			return femaleBtn.getText();
		} else {
			return new String("?");
		}
	}
	
	// 포지션 체크박스 중 어느것이 선택되었는지 알아내기
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
	
	// 실력 라디오 버튼 중 어느 것이 선택되었는지 알아내기
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
	
	// 사용자에게 입력받은 정보로 JSON객체 만들기
	private JSONObject inputToJSON() {
		JSONObject memberJSON = new JSONObject();
		memberJSON.put("mid", idTxtFld.getText());
		memberJSON.put("mpassword", pwTxtFld1.getText());
		memberJSON.put("mheight", heightComboBox.getSelectionModel().getSelectedItem());
		memberJSON.put("msex", findSex());
		memberJSON.put("mposition", findPosition());
		memberJSON.put("marea", cityComboBox.getSelectionModel().getSelectedItem() + " " + boroughComboBox.getSelectionModel().getSelectedItem());
		memberJSON.put("mlevel", findLevel());
		
		// 이미지파일의 변경여부에 따른 처리(삭제되었으면 null 보냄, 변경되지 않았으면 이전의 이미지파일명으로 저장)
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
	
	// 사용자에게 입력받은 정보로 Member객체 만들기
	private Member inputToMember() {
		Member member = new Member();
		member.setMid(idTxtFld.getText());
		member.setMpassword(pwTxtFld1.getText());
		member.setMheight(heightComboBox.getSelectionModel().getSelectedItem());
		member.setMsex(findSex());
		member.setMposition(findPosition());
		member.setMarea(cityComboBox.getSelectionModel().getSelectedItem() + " " + boroughComboBox.getSelectionModel().getSelectedItem());
		member.setMlevel(findLevel());
		
		// 이미지파일의 변경여부에 따른 처리(삭제되었으면 null 보냄, 변경되지 않았으면 이전의 이미지파일명으로 저장)
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
	
	// 사진삭제 버튼이벤트
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

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
	@FXML TextField txtId; //ID 텍스트필드
	@FXML Button checkId; // 중복체크 버튼
	@FXML Label idLabel; // id나 비밀번호 오류 알려주는 라벨
	@FXML Label idLabelOk; //  id나 비밀번호 제대로 작성 됨을 알려주는 라벨
	@FXML PasswordField password1; // 비밀번호 필드
	@FXML PasswordField password2; // 두번째 비밀번호 필드
	@FXML ImageView imgUpload; // 프로필 사진
	@FXML ToggleGroup selectSex; // 남성, 여성 토글키
	@FXML ToggleGroup selectLevel; // 초보, 중수, 고수 토글키 
	@FXML Button joinBtn; //회원가입  버튼
	@FXML CheckBox guardChckBx; // 가드
	@FXML CheckBox forwardChckBx; // 포워드
	@FXML CheckBox centerChckBx; // 센터
	@FXML ComboBox<String> cityCheck; // 도시 선택 콤보박스
	@FXML ComboBox<String> boroughCheck; // 지역 선택 콤보 박스
	@FXML ComboBox<String> heightCheck; // 키 선택 콤보 박스
	@FXML RadioButton maleBtn;
	@FXML RadioButton femaleBtn;
	@FXML RadioButton rookieBtn;
	@FXML RadioButton amateurBtn;
	@FXML RadioButton professBtn;
	
	// 필수 정보를 제대로 입력했는지 여부
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
		
		//아이디, 비밀번호, 키의 텍스트필드에 입력할 때 유효검사하기
		txtId.setOnKeyReleased((event) ->textIdCheck(event));
		password1.setOnKeyReleased((event) ->passwordCheck(event));
		password2.setOnKeyReleased((event) ->password2Check());
	
		// 키콤보박스에 글 넣기
		heightList= FXCollections.observableArrayList();
		heightList.add("150cm이하");
		heightList.add("160~170cm");
		heightList.add("170~180cm");
		heightList.add("180~190cm");
		heightList.add("190cm이상");
		heightList.add("비설정");
		heightCheck.setItems(heightList);
		
		//지역1 콤보박스에 글 넣기
		cityList = FXCollections.observableArrayList();
		cityList.add("서울시");
		cityList.add("부산시");
		cityList.add("인천시");
		cityCheck.setItems(cityList);
		// 지역1을 선택하면 지역2에 해당하는 지역의 글 넣기, 값 받아오기
		cityCheck.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("서울시")){
					boroughCheck.setValue("");
					addSeoul();
				} else if(newValue.equals("인천시")){
					boroughCheck.setValue("");
					addIncheon();
				} else{
					boroughCheck.setValue("");
					addBusan();
				}
				boroughCheck.setItems(boroughList);
			}
		});
		
		
		/*// 테스트용 디폴트 값
		txtId.setText("test");
		password1.setText("123123");
		password2.setText("123123");
		forwardChckBx.setSelected(true);;
		heightCheck.setValue("150cm이하");
		cityCheck.setValue("서울시");
		boroughCheck.setValue("종로구");*/
		
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
	
	public void handleBtnSelect(ActionEvent e) { //이미지 업로드
		try { 
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
			selectedFile = fileChooser.showOpenDialog(AppMain.primaryStage);
			if (selectedFile == null) return; //선택을 안하고 닫았을 경우
			imgUpload.setImage(new Image(new FileInputStream(selectedFile))); //imageview
		} catch(Exception e1) {
			e1.printStackTrace();
		}
	}
	
	// 아이디 소문자, 숫자 외에 값을 넣었을 경우 && 4글자이상 체크
	public void textIdCheck(KeyEvent event) { 
		if(txtId.getText().length()<4) {
			idLabelOk.setText("");
			idLabel.setText("아이디를 4자이상 작성해주세요");
			isIdOk = false;
		} else if(100 < txtId.getText().length()) {
			idLabelOk.setText("");
			idLabel.setText("아이디를 100자 이내로 작성해주세요");
			isIdOk = false;
		} else {
			idLabel.setText("");
			char[] ch = event.getText().toCharArray();
			for(int i=0; i<ch.length; i++) {
				int keyCode = ch[i];
				if(!(123>keyCode && 96< keyCode) && !(47<keyCode && 58>keyCode)){
					idLabelOk.setText("");
					idLabel.setText("아이디를 다시 작성해주세요(소문자,숫자만)");
					isIdOk = false;
				}
			}
			ch = txtId.getText().toCharArray();
			for(int i=0; i<ch.length; i++) {
				int keyCode = ch[i];
				if(!(123>keyCode && 96< keyCode) && !(47<keyCode && 58>keyCode)){
					idLabelOk.setText("");
					idLabel.setText("아이디를 다시 작성해주세요(소문자,숫자만)");
					isIdOk = false;
					break;
				} else {
					idLabel.setText("");
					idLabelOk.setText("아이디ok");
					isIdOk = true;
				}
			}
		}
	}
	
	public void handleCheckId() throws Exception{ //중복체크 버튼
		if(isIdOk == false){
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(LoginController.primaryStage);
			Parent root =(Parent)FXMLLoader.load(getClass().getResource("checkIdError.fxml"));
			Scene sc = new Scene(root);
			dialog.setScene(sc);
			dialog.setResizable(false);
			dialog.setTitle("확인");
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
								dialog.setTitle("확인");
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
								dialog.setTitle("확인");
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
	
	public void passwordCheck(KeyEvent event) { //비밀번호에 값을 넣었을 경우 체크
		if(password1.getText().length()<6) {
			idLabelOk.setText("");
			idLabel.setText("비밀번호를 6자이상 작성해주세요");
			isPasswordOk = false;
		} else if(100 < password1.getText().length()) {
			idLabelOk.setText("");
			idLabel.setText("비밀번호를 100자 이내로 작성해주세요");
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
				idLabel.setText("비밀번호는 알파벳, 숫자, 특수문자만 가능합니다.");
				isPasswordOk = false;
			}
			//문자, 숫자, 특수문자의 조합인지 확인
			p = Pattern.compile("^[a-zA-Z0-9a-zA-Z0-9!,@,#,$,%,^,&,*,?,_,~]*$");
			pass =password1.getText();
			m = p.matcher(pass);
			if (m.find()){
				idLabel.setText("");
				idLabelOk.setText("비빌번호ok");
				isPasswordOk = true;
			} else {
				idLabelOk.setText("");
				idLabel.setText("비밀번호는 알파벳, 숫자, 특수문자만 가능합니다.");
				isPasswordOk = false;
			}
		}
	}

	public void password2Check(){
		if(password2.getText().equals(password1.getText())){
			idLabel.setText("");
			idLabelOk.setText("비밀번호가 일치 합니다.");
			isPasswordOk2 = true;
		} else{
			idLabelOk.setText("");
			idLabel.setText("비밀번호가 일치하지 않습니다.");
			isPasswordOk2 = false;
		}
	}
	

	
	//가입하기 버튼 이벤트 처리
	public void handleJoinBtn() { 
		if(inputCheck() == false) {
			return;
		}
		
		Thread threadOutput = new Thread(()->{
			try {
				// 서버와 연결
				Socket sock=new Socket();
				sock.connect(AppMain.serverAddress);
				
				// command 보내기
				OutputStream os = sock.getOutputStream();
				os.write("join".getBytes());
				os.write("\n".getBytes());
				
				// 입력받은 값으로 Member객체 만들어서 보내기
				JSONObject joinData = mkMemDataByInput();
				os.write(joinData.toString().getBytes());
				os.write("\n".getBytes());
				
				// 사진파일 있는 경우 사진파일도 보내기
				if(selectedFile!=null) {
					FileInputStream fis = new FileInputStream(selectedFile);
					byte[] datas = new byte[1024];
					int totalReadNum = 0;
					int readByteNum = -1;
					while(true) {
						readByteNum = fis.read(datas);
						os.write(datas, 0 , readByteNum);
						
						// 서버로 사진파일보내는 바이트 크기가 사진의 바이트 크기와 같으면 파일보내기 중지
						totalReadNum += readByteNum;
						if(totalReadNum == selectedFile.length()) {
							os.flush();
							break;
						}
					}
					fis.close();
				}				
				os.flush();
				
				// 가입하기 요청처리결과 받기
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
				
				// 요청처리결과에 따른 처리(성공이면 성공다이얼로그 띄우고 로그인화면으로 넘어가기, 실패면
				Platform.runLater(()->{
					if(joinResult.equals("success")) {
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(joinBtn.getScene().getWindow());
							Parent joinOk;
							joinOk = FXMLLoader.load(getClass().getResource("you_joined.fxml"));
							Button joinOkBtn = (Button) joinOk.lookup("#joinOkCheckBtn");
							
							// 가입완료확인다이얼로그를 닫으면 가입하기화면에서 로그인화면으로 넘어가도록 처리
							joinOkBtn.setOnAction((e)->{
								dialog.close();
								showLogin();		
							});
							Scene sc = new Scene(joinOk);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("확인");
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
							dialog.setTitle("확인");
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

	public void joinCancel() throws Exception { //회원가입의 취소버튼
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
		dialog.setTitle("확인");
		dialog.show();
	}
	
	// UI화면을 로그인화면으로 변경하는 메소드
	public void showLogin() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../login/login(first).fxml"));
			Scene scene = new Scene(root);
			Stage stage = (Stage) joinBtn.getScene().getWindow();
			stage.setScene(scene);
			stage.show();
			
		} catch(IOException e) {
			e.printStackTrace();
			System.out.print("fxml파일을 찾을 수 없습니다.");
		}
	}
	
	
	private void handleIOException(IOException e) {
		e.printStackTrace();
		System.out.println("서버와 연결할 수 없습니다.");
		return;
	}
	
	// 사용자가 입력한 값으로 JSONObect 생성하여 반환하는 메소드 (Make Member Data By Input information)
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
	
	private boolean inputCheck() {
		// 아이디가 잘못입력된 경우 에러메시지 다이얼로그 띄우고 ID텍스트필드 비우기 & false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			return false;
		}
		
		// 아이디가 중복검사에 걸린 경우 에러메시지 다이얼로그 띄우고 ID텍스트필드 비우기 & false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		// 비밀번호가 잘못 입력된 경우 에러메시지 다이얼로그 띄우고 비밀번호 텍스트필드 비우기 & false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		// 비밀번호확인이 잘못 입력된 경우 에러메시지 다이얼로그 띄우고 비밀번호 텍스트필드 비우기 & false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		// 포지션 체크박스가 입력되지 않은 경우 에러메시지 다이얼로그 띄우고 false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		//키(height) 콤보박스가 입력되지 않은 경우 에러메시지 다이얼로그 띄우고 false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		// 시(city) 콤보박스가 입력되지 않은 경우 에러메시지 다이얼로그 띄우고 false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		// 구(borough) 콤보박스가 입력되지 않은 경우 에러메시지 다이얼로그 띄우고 false 리턴
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
				System.out.println("fxml파일을 찾을 수 없습니다.");
			}
			
			return false;
		}
		
		return true;
	}
	
	// 사진삭제 버튼이벤트 - 디폴트이미지를 이미지뷰에 업로드하고 selectFile값을 null로 변경
	public void handleDeletePictureBtn(ActionEvent e) {
		try {
			imgUpload.setImage(new Image(new FileInputStream(AppMain.defaultImage.getPath())));
		} catch(Exception e2) {
			e2.printStackTrace();
		}
		selectedFile = null;
	}
}

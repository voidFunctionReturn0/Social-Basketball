package client.boards.updateBoard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.json.JSONObject;

import client.AppMain;
import client.boards.boardsRoomController;
import client.model.Board;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UpdateBoardController implements Initializable {
	@FXML private Label bnoLbl;
	@FXML private TextField titleTxtFld;
	@FXML private Label idLbl;
	@FXML private DatePicker datePicker;
	@FXML private ComboBox<String> timeCombo;
	@FXML private ComboBox<String> cityCombo;
	@FXML private ComboBox<String> boroughCombo;
	@FXML private TextArea contentTxtArea;
	@FXML private Button updateBtn;
	private ObservableList<String> timeList; 
	private ObservableList<String> cityList; 
	private ObservableList<String> boroughList;
	@FXML private Button cancelBtn;
	
	boolean isTitleCheck=false; //빈칸체크랑 빈공간만 들어가는 것을 방지
	boolean isContentCheck=false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TextArea 세로 스크롤 만들기
		
		
		// 약속시간 콤보박스 초기화
		timeList= FXCollections.observableArrayList();
		for(int i=0; i<25; i++){
			if(i<10) {
				timeList.add("0"+i+"시");
			} else {
				timeList.add(i+"시");
			}
		}
		timeCombo.setItems(timeList);
		
		//지역1 콤보박스에 글 넣기
		cityList = FXCollections.observableArrayList();
		cityList.add("서울시");
		cityList.add("부산시");
		cityList.add("인천시");
		cityCombo.setItems(cityList);
		// 지역1을 선택하면 지역2에 해당하는 지역의 글 넣기, 값 받아오기
		cityCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("서울시")){
					boroughCombo.setValue("");
					addSeoul();
				} else if(newValue.equals("인천시")){
					boroughCombo.setValue("");
					addIncheon();
				} else{
					boroughCombo.setValue("");
					addBusan();
				}
				boroughCombo.setItems(boroughList);
			}
		});
		
		// 썼던 글을 불러오기
		initialzeBoard();
		
		//11자 넘어가면 글자가 잘려서 텍스트필드 11자 제한
		titleTxtFld.textProperty().addListener(new ChangeListener<String>() { 
			@Override
	        public void changed( ObservableValue<? extends String> ov, String oldValue, String newValue) {
	            if (titleTxtFld.getText().length() > 11) {
	                String s = titleTxtFld.getText().substring(0, 11);
	                titleTxtFld.setText(s);
	            }
	        }
	    });
		
		handleTitleCheck();
		titleTxtFld.focusedProperty().addListener((e)->{ //포커스가 바뀌면 발동함
			handleTitleCheck();
		});
		
		
		handleContentCheck();
		contentTxtArea.focusedProperty().addListener((e)->{ //포커스가 바뀌면 발동함
			handleContentCheck();
		});
	

	}
	
	public void handleContentCheck(){ //내용에 빈칸 방지 (스페이스는 허용했음)
		if(!contentTxtArea.getText().equals("")){
			isContentCheck=true;
		}else{
			isContentCheck=false;
		}
	}
	
	public void handleTitleCheck(){ // 제목에 빈칸, 스페이스 방지
		String textCheck =titleTxtFld.getText();
		String[] array;
		array=textCheck.split(" ");
		if (!(array.length==0) && !(textCheck.equals(""))){ 
			isTitleCheck=true;
		}else{
			isTitleCheck=false;
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
	
	public void enterDetailBoard(){
		try {
			StackPane root = boardsRoomController.instance;
			StackPane sp =(StackPane)FXMLLoader.load(getClass().getResource("../boardsRoom.fxml"));
			root.getChildren().add(sp);
			root.getChildren().remove(2);
			root.getChildren().remove(1);
			root.getChildren().get(0).setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleCancelBtn(ActionEvent e) { //뒤로가기 버튼 눌렀을 때 작동함
		try {
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
			Parent dia = (Parent)FXMLLoader.load(getClass().getResource("../rewriteCancelCheck.fxml"));
	
			Button rewriteCheckOkBtn = (Button) dia.lookup("#rewriteCheckOk");
			Button rewriteCheckCancelBtn =  (Button) dia.lookup("#rewriteCheckCancel");
			
			rewriteCheckCancelBtn.setOnAction((event)->{
				dialog.close();
			});
			rewriteCheckOkBtn.setOnAction((event)->{
				dialog.close();
				enterDetailBoard();
			});
			
			Scene sc = new Scene(dia);
			dialog.setScene(sc);
			dialog.setResizable(false);
			dialog.setTitle("확인");
			dialog.show();	
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	public void handleUpdateBtn(ActionEvent e) {  //수정 버튼 눌렀을 때
		System.out.println(isTitleCheck + "-" + isContentCheck);
		if(!isTitleCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("../check/titleCheck.fxml"));
			
				Button titleCheckOkBtn = (Button) dia.lookup("#titleCheckOk");
				titleCheckOkBtn.setOnAction((event)->{
					dialog.close();
					((Stage)(updateBtn.getScene().getWindow())).close();
				});
				
				Scene sc = new Scene(dia);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("확인");
				dialog.show();	
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(!isContentCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("../check/contentCheck.fxml"));
				
				Button contentCheckOkBtn = (Button) dia.lookup("#contentCheckOk");
				
				contentCheckOkBtn.setOnAction((event)->dialog.close());
				
				Scene sc = new Scene(dia);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("확인");
				dialog.show();	
			
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(isContentCheck && isTitleCheck){
			Board board = new Board();
			board.setBno(Integer.valueOf(bnoLbl.getText()));
			board.setBwriter(idLbl.getText());
			board.setBtitle(titleTxtFld.getText());
			board.setBappointday(datePicker.getValue().toString());
			board.setBappointtime(timeCombo.getValue());
			board.setBarea(cityCombo.getValue() + " " + boroughCombo.getValue());
			board.setBcontent(contentTxtArea.getText());
			
			Thread thread = new Thread(()-> {
				
				Socket sock = null;
				try {
					// 서버와 연결
					sock = new Socket();
					sock.connect(AppMain.serverAddress);
					
					// command 보내기
					OutputStream os = sock.getOutputStream();
					os.write("updateBoard".getBytes());
					os.write("\n".getBytes());
					
					
					JSONObject sendData = new JSONObject();
					sendData.put("bno", Integer.toString(board.getBno()));
					sendData.put("bwriter", board.getBwriter());
					sendData.put("btitle", board.getBtitle());
					sendData.put("bappointday", board.getBappointday());
					sendData.put("bappointtime", board.getBappointtime());
					sendData.put("barea", board.getBarea());
					sendData.put("bcontent", board.getBcontent());
					os.write(sendData.toString().getBytes());
					os.write("\n".getBytes());
					os.flush();
					
					
					// 요청처리결과 받기
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
					String updateBoardResult = new String(datas, 0, index);
	
	
					if(updateBoardResult.equals("success")) {
						Platform.runLater(()->{
							handleEnterDetailBoard(board.getBno());
						});
					} else {
						//데이터 받기 실패일때 출력
						System.out.println("데이터 저장 실패");
					}
				}catch(Exception e1) {
					
				} finally {
					try {
						sock.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			thread.start();
		}
	}
	// 서버에서 썼던 글을 가져오기
	private void initialzeBoard() {
		Socket sock = null;
		try {
			// 서버와 TCP소켓연결
			sock = new Socket();
			sock.connect(AppMain.serverAddress);
			
			// command : "getBoard" 보내기
			OutputStream os = sock.getOutputStream();
			os.write("getBoard".getBytes());
			os.write("\n".getBytes());
			
			// data : board의 PK인 "bno" 보내기
			os.write(AppMain.bno.getBytes());
			os.write("\n".getBytes());
			os.flush();
			// 요청처리결과 받기
			InputStream is = sock.getInputStream();
			byte[] datas = new byte[10];
			int data;
			int idx = 0;
			while( (data=is.read()) != '\n' ) {
				datas[idx++] = (byte)data;
			}
			String result = new String(datas, 0, idx);
			
			// 결과에 따른 처리
			if(result.equals("success")) {
				// 성공시 Board객체를 JSON객체로 받아서 파싱 후 fxml에 세팅
				byte[] boardDatas = new byte[10000];
				int boardData;
				int boardIdx = 0;
				while( (boardData=is.read()) != '\n' ) {
					boardDatas[boardIdx++] = (byte)boardData;
				}
				JSONObject boardJSON = new JSONObject(new String(boardDatas, 0, boardIdx));
				JSONToContorols(boardJSON);
				
				// 실패시 실패다이얼로그 띄우기
			} else {
				
			} 
			
		} catch(IOException e) {
			
		} finally {
			try {
				sock.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void handleEnterDetailBoard(int bno){
		
		Thread thread = new Thread(()->{
		try {
			Socket socket = new Socket();
			socket.connect(AppMain.serverAddress);
			
			
			OutputStream os = socket.getOutputStream(); //보낸다.
			os.write("getBoard".getBytes());
			os.write("\n".getBytes());
			os.write(AppMain.bno.getBytes());
			os.write("\n".getBytes());
			os.flush();
			
			InputStream is = socket.getInputStream(); //받는다.
			byte[] datas = new byte[100];
			byte index =0;
			while(true){
				int data= is.read();
				if(data != '\n'){
					datas[index] = (byte)data;
					index++;
				} else {
					break;
				}
			}
			String enterBoardResult = new String(datas,0,index);

			if(enterBoardResult.equals("success")){
				byte[] boardDatas = new byte[10000];
				int boardData;
				int boardIdx = 0;
				while( (boardData=is.read()) != '\n' ) {
					boardDatas[boardIdx++] = (byte)boardData;
				}
				JSONObject boardJSON = new JSONObject(new String(boardDatas, 0, boardIdx));
				
				Platform.runLater(()->{
					
					try {
						BorderPane root = FXMLLoader.load(getClass().getResource("../detailboard/detailBoard.fxml"));
						Label detailTitle = (Label) root.lookup("#detailBoardTitle"); 
						Label detailWriter = (Label) root.lookup("#detailBoardWriter"); 
						Label detailTime = (Label) root.lookup("#detailBoardTime"); 
						TextArea detailContent = (TextArea) root.lookup("#detailBoardContent"); 
						//Label detailCommentCount = (Label) root.lookup("#detailBoardCommentCount"); 
						Label detailArea = (Label) root.lookup("#detailBoardArea");
						
							// 제목
							detailTitle.setText(boardJSON.getString("btitle"));
							// 글쓴이
							detailWriter.setText(boardJSON.getString("bwriter"));
							// 약속 시간
							detailTime.setText(boardJSON.getString("bappointtime"));
							// 내용
							detailContent.setText(boardJSON.getString("bcontent"));		
							
							detailArea.setText(boardJSON.getString("barea"));
							
							//detailCommentCount.setText(Integer.toString(boardJSON.getInt("bcommentcnt")));
							
							StackPane sp = boardsRoomController.instance;
							sp.getChildren().remove(2);
							sp.getChildren().remove(1);
							sp.getChildren().add(root);
							sp.getChildren().get(0).setVisible(false);
							
						
					} catch (IOException e) {
						e.printStackTrace();
					}

				});
			} else {
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		});
		thread.start();
		
	}
	
	// JSON객체를 fx컨트롤들에 세팅
	private void JSONToContorols(JSONObject boardJSON) {
		// 게시물번호
		bnoLbl.setText(String.valueOf(boardJSON.getInt("bno")));
		
		// 제목
		titleTxtFld.setText(boardJSON.getString("btitle"));
		
		// 글쓴이
		idLbl.setText(boardJSON.getString("bwriter"));
		
		// 약속 날짜 (문자열을 '-' 구분자로 분리하여 datePicker에 세팅)
		String[] dayStrArr = boardJSON.getString("bappointday").split("-");
		int[] dayArr = new int[3];
		for(int i=0; i<3; i++) {
			dayArr[i] = Integer.parseInt(dayStrArr[i]);
		}
		datePicker.setValue(LocalDate.of(dayArr[0], dayArr[1], dayArr[2]));
		
		// 약속 시간
		timeCombo.setValue(boardJSON.getString("bappointtime"));
		
		// 지역 (시, 구로 나누어 세팅)
		String areaArr[] = boardJSON.getString("barea").split(" ");
		cityCombo.setValue(areaArr[0]);
		boroughCombo.setValue(areaArr[1]);
		
		// 내용
		contentTxtArea.setText(boardJSON.getString("bcontent"));		
	}
	
}

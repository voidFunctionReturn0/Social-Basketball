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
	
	boolean isTitleCheck=false; //��ĭüũ�� ������� ���� ���� ����
	boolean isContentCheck=false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TextArea ���� ��ũ�� �����
		
		
		// ��ӽð� �޺��ڽ� �ʱ�ȭ
		timeList= FXCollections.observableArrayList();
		for(int i=0; i<25; i++){
			if(i<10) {
				timeList.add("0"+i+"��");
			} else {
				timeList.add(i+"��");
			}
		}
		timeCombo.setItems(timeList);
		
		//����1 �޺��ڽ��� �� �ֱ�
		cityList = FXCollections.observableArrayList();
		cityList.add("�����");
		cityList.add("�λ��");
		cityList.add("��õ��");
		cityCombo.setItems(cityList);
		// ����1�� �����ϸ� ����2�� �ش��ϴ� ������ �� �ֱ�, �� �޾ƿ���
		cityCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				boroughList = FXCollections.observableArrayList();
				if(newValue.equals("�����")){
					boroughCombo.setValue("");
					addSeoul();
				} else if(newValue.equals("��õ��")){
					boroughCombo.setValue("");
					addIncheon();
				} else{
					boroughCombo.setValue("");
					addBusan();
				}
				boroughCombo.setItems(boroughList);
			}
		});
		
		// ��� ���� �ҷ�����
		initialzeBoard();
		
		//11�� �Ѿ�� ���ڰ� �߷��� �ؽ�Ʈ�ʵ� 11�� ����
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
		titleTxtFld.focusedProperty().addListener((e)->{ //��Ŀ���� �ٲ�� �ߵ���
			handleTitleCheck();
		});
		
		
		handleContentCheck();
		contentTxtArea.focusedProperty().addListener((e)->{ //��Ŀ���� �ٲ�� �ߵ���
			handleContentCheck();
		});
	

	}
	
	public void handleContentCheck(){ //���뿡 ��ĭ ���� (�����̽��� �������)
		if(!contentTxtArea.getText().equals("")){
			isContentCheck=true;
		}else{
			isContentCheck=false;
		}
	}
	
	public void handleTitleCheck(){ // ���� ��ĭ, �����̽� ����
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
	
	public void handleCancelBtn(ActionEvent e) { //�ڷΰ��� ��ư ������ �� �۵���
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
			dialog.setTitle("Ȯ��");
			dialog.show();	
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	

	public void handleUpdateBtn(ActionEvent e) {  //���� ��ư ������ ��
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
				dialog.setTitle("Ȯ��");
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
				dialog.setTitle("Ȯ��");
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
					// ������ ����
					sock = new Socket();
					sock.connect(AppMain.serverAddress);
					
					// command ������
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
					
					
					// ��ûó����� �ޱ�
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
						//������ �ޱ� �����϶� ���
						System.out.println("������ ���� ����");
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
	// �������� ��� ���� ��������
	private void initialzeBoard() {
		Socket sock = null;
		try {
			// ������ TCP���Ͽ���
			sock = new Socket();
			sock.connect(AppMain.serverAddress);
			
			// command : "getBoard" ������
			OutputStream os = sock.getOutputStream();
			os.write("getBoard".getBytes());
			os.write("\n".getBytes());
			
			// data : board�� PK�� "bno" ������
			os.write(AppMain.bno.getBytes());
			os.write("\n".getBytes());
			os.flush();
			// ��ûó����� �ޱ�
			InputStream is = sock.getInputStream();
			byte[] datas = new byte[10];
			int data;
			int idx = 0;
			while( (data=is.read()) != '\n' ) {
				datas[idx++] = (byte)data;
			}
			String result = new String(datas, 0, idx);
			
			// ����� ���� ó��
			if(result.equals("success")) {
				// ������ Board��ü�� JSON��ü�� �޾Ƽ� �Ľ� �� fxml�� ����
				byte[] boardDatas = new byte[10000];
				int boardData;
				int boardIdx = 0;
				while( (boardData=is.read()) != '\n' ) {
					boardDatas[boardIdx++] = (byte)boardData;
				}
				JSONObject boardJSON = new JSONObject(new String(boardDatas, 0, boardIdx));
				JSONToContorols(boardJSON);
				
				// ���н� ���д��̾�α� ����
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
			
			
			OutputStream os = socket.getOutputStream(); //������.
			os.write("getBoard".getBytes());
			os.write("\n".getBytes());
			os.write(AppMain.bno.getBytes());
			os.write("\n".getBytes());
			os.flush();
			
			InputStream is = socket.getInputStream(); //�޴´�.
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
						
							// ����
							detailTitle.setText(boardJSON.getString("btitle"));
							// �۾���
							detailWriter.setText(boardJSON.getString("bwriter"));
							// ��� �ð�
							detailTime.setText(boardJSON.getString("bappointtime"));
							// ����
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
	
	// JSON��ü�� fx��Ʈ�ѵ鿡 ����
	private void JSONToContorols(JSONObject boardJSON) {
		// �Խù���ȣ
		bnoLbl.setText(String.valueOf(boardJSON.getInt("bno")));
		
		// ����
		titleTxtFld.setText(boardJSON.getString("btitle"));
		
		// �۾���
		idLbl.setText(boardJSON.getString("bwriter"));
		
		// ��� ��¥ (���ڿ��� '-' �����ڷ� �и��Ͽ� datePicker�� ����)
		String[] dayStrArr = boardJSON.getString("bappointday").split("-");
		int[] dayArr = new int[3];
		for(int i=0; i<3; i++) {
			dayArr[i] = Integer.parseInt(dayStrArr[i]);
		}
		datePicker.setValue(LocalDate.of(dayArr[0], dayArr[1], dayArr[2]));
		
		// ��� �ð�
		timeCombo.setValue(boardJSON.getString("bappointtime"));
		
		// ���� (��, ���� ������ ����)
		String areaArr[] = boardJSON.getString("barea").split(" ");
		cityCombo.setValue(areaArr[0]);
		boroughCombo.setValue(areaArr[1]);
		
		// ����
		contentTxtArea.setText(boardJSON.getString("bcontent"));		
	}
	
}

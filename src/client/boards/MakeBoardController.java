package client.boards;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import org.json.JSONObject;

import client.AppMain;
import client.model.Board;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MakeBoardController implements Initializable{ 
	@FXML private Label label_boardMid;
	@FXML private TextField txt_BoardTitle;
	@FXML private DatePicker datepicker_BoardDay;
	
	@FXML private ComboBox<String> timeCombo;
	@FXML private ComboBox<String> cityCombo;
	@FXML private ComboBox<String> boroughCombo;
	
	@FXML private TextArea txt_BoardContent;
	
	private ObservableList<String> timeList;
	private ObservableList<String> cityList;
	private ObservableList<String> boroughList;
	
	boolean isTitleCheck;
	boolean isDatepickerCheck;
	boolean isTimeCheck;
	boolean isCityCheck;
	boolean isBoroughCheck;
	boolean isContentCheck;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		label_boardMid.setText(AppMain.member.getMid());
		
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
	
		txt_BoardTitle.textProperty().addListener(new ChangeListener<String>() { //�ؽ�Ʈ�ʵ� 11�� ����
		        @Override
		        public void changed( ObservableValue<? extends String> ov, String oldValue, String newValue) {
		            if (txt_BoardTitle.getText().length() > 11) {
		                String s = txt_BoardTitle.getText().substring(0, 11);
		                txt_BoardTitle.setText(s);
		            }
		        }
		    });
		
		txt_BoardTitle.focusedProperty().addListener((e)->{ //��Ŀ���� �ٲ�� �ߵ���
			handleTitleCheck();
		});
		

		txt_BoardContent.focusedProperty().addListener((e)->{ //��Ŀ���� �ٲ�� �ߵ���
			handleContentCheck();
		});
	
		datepicker_BoardDay.valueProperty().addListener((e)->{ 
			handleDatePickerCheck();
		});
		
		cityCombo.valueProperty().addListener((e)->{ 
			handlecityComboCheck();
		});
	
		handletimeComboCheck();
		timeCombo.valueProperty().addListener((e)->{ 
			handletimeComboCheck();
		});
		

		boroughCombo.valueProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!newValue.equals("")){
					handleboroughComboCheck();
				}
			}
		});
			
	}
		
	public void handletimeComboCheck(){ 
		if( timeCombo.getValue() != null){
			isTimeCheck=true;
		} else {
			isTimeCheck=false;
		}
	}
	
	public void handlecityComboCheck(){ 
		if(cityCombo.getValue() != null){
			isCityCheck=true;
		} else {
			isCityCheck=false;
		}
	}
	
	public void handleboroughComboCheck(){ 
		if(boroughCombo.getValue() != null){
			isBoroughCheck=true;
		} else {
			isBoroughCheck=false;
		}
	}
	
	public void handleDatePickerCheck(){ 
		LocalDate localDate = datepicker_BoardDay.getValue();
		if(localDate != null){
			isDatepickerCheck=true;
		}
	}
	
	public void handleContentCheck(){ //���뿡 ��ĭ ���� (�����̽��� �������)
		if(!txt_BoardContent.getText().equals("")){
			isContentCheck=true;
		}else{
			isContentCheck=false;
		}
	}
	
	public void handleTitleCheck(){ // ���� ��ĭ, �����̽� ����
		String textCheck =txt_BoardTitle.getText();
		String[] array;
		array=textCheck.split(" ");
		if (!(array.length==0) && !(textCheck.equals(""))){ 
			isTitleCheck=true;
		}else{
			isTitleCheck=false;
		}
	}
	
	public void handleCancelBtn(){
		StackPane root = boardsRoomController.instance;
		root.getChildren().remove(1);
		root.getChildren().get(0).setVisible(true);
	}
	

	public void handleBtnMakeBoard(){ //�� �ø��� ��ư
		System.out.println(isTitleCheck + "-" + isContentCheck + "-" +isDatepickerCheck + "-" +isTimeCheck+ "-" +isCityCheck+ "-" + isBoroughCheck);
		if(!isTitleCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("check/titleCheck.fxml"));
			
				Button titleCheckOkBtn = (Button) dia.lookup("#titleCheckOk");
				titleCheckOkBtn.setOnAction((event)->dialog.close());
				
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
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("check/contentCheck.fxml"));
				
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
		}else if(!isDatepickerCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("check/datepickerCheck.fxml"));
				
				Button datepickerCheckOkBtn = (Button) dia.lookup("#datepickerCheckOk");
				
				datepickerCheckOkBtn.setOnAction((event)->dialog.close());
				
				Scene sc = new Scene(dia);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Ȯ��");
				dialog.show();	
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(!isTimeCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("check/timeCheck.fxml"));
				
				Button timeCheckOkBtn = (Button) dia.lookup("#timeCheckOk");
				
				timeCheckOkBtn.setOnAction((event)->dialog.close());
				
				Scene sc = new Scene(dia);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Ȯ��");
				dialog.show();	
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if(!isCityCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("check/cityCheck.fxml"));
				
				Button cityCheckOkBtn = (Button) dia.lookup("#cityCheckOk");
				
				cityCheckOkBtn.setOnAction((event)->dialog.close());
				
				Scene sc = new Scene(dia);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Ȯ��");
				dialog.show();	
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if(!isBoroughCheck){
			try {
				Stage dialog = new Stage(StageStyle.DECORATED);
				dialog.initModality(Modality.WINDOW_MODAL);
				dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
				Parent dia = (Parent)FXMLLoader.load(getClass().getResource("check/boroughCheck.fxml"));
				
				Button boroughCheckOkBtn = (Button) dia.lookup("#boroughCheckOk");
				
				boroughCheckOkBtn.setOnAction((event)->dialog.close());
				
				Scene sc = new Scene(dia);
				dialog.setScene(sc);
				dialog.setResizable(false);
				dialog.setTitle("Ȯ��");
				dialog.show();	
			
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		} else if(isContentCheck && isTitleCheck && isDatepickerCheck && isTimeCheck && isCityCheck && isBoroughCheck){

		Thread thread = new Thread(()-> {
			Board board = new Board();
			board.setBwriter(label_boardMid.getText());
			board.setBtitle(txt_BoardTitle.getText());
			board.setBappointday(datepicker_BoardDay.getValue().toString());
			board.setBappointtime(timeCombo.getValue());
			board.setBarea(cityCombo.getValue() + " " + boroughCombo.getValue());
			board.setBcontent(txt_BoardContent.getText());
				
			try {
				// ������ ����
				Socket sock=new Socket();
				sock.connect(AppMain.serverAddress);
				
				// command ������
				OutputStream os = sock.getOutputStream();
				os.write("makeBoard".getBytes());
				os.write("\n".getBytes());
				JSONObject sendData = new JSONObject();
				
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
				String makeBoardResult = new String(datas, 0, index);
//				System.out.println(makeBoardResult);
	
				
				if(makeBoardResult.equals("success")) {
					Platform.runLater(()->{
					
						try {
							StackPane root = boardsRoomController.instance;
							StackPane sp =(StackPane)FXMLLoader.load(getClass().getResource("boardsRoom.fxml"));
							root.getChildren().add(sp);
							root.getChildren().remove(1);
						} catch (Exception e) {
					
							e.printStackTrace();
						}
				
					});
				} else {
					//������ �ޱ� �����϶� ���
//					System.out.println("������ ���� ����");
				}
			}catch(Exception e) {}
		});
		thread.start();
		
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
	
}

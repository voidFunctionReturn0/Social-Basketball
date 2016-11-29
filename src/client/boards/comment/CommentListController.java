package client.boards.comment;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import client.AppMain;
import client.boards.boardsRoomController;
import client.model.Comment;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;



public class CommentListController implements Initializable {
	public static CommentListController instance;

	
	//private int pageNo=1;
	private static int rowsPerPage=100;
	
	@FXML ListView<HBox> commentListView;
	
	
	private static Comment comment = new Comment();
	public static String detailwriter;
	public static String area;
	
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		
		refreshList();
		
		/*commentListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
			@Override
			public void changed(ObservableValue<? extends HBox> observable, HBox oldValue, HBox newValue) {
				//�󼼺��� �Ҷ� ������ ���� �߰� �ϴ� �͵� �߰� �ؾ� �մϴ�.
				Label cno = (Label) newValue.lookup("#comment_No");
				Label title = (Label) newValue.lookup("#boards_Title");
				Label area = (Label) newValue.lookup("#boards_Area");
				Label time = (Label) newValue.lookup("#boards_Time");
				Label commentCount = (Label) newValue.lookup("#boards_CommentCount");
				Label hitCount = (Label) newValue.lookup("#boards_HitCount");
//				System.out.println(no.getText());
				AppMain.cno = String.valueOf(cno.getText());
				//commentListView.getSelectionModel()
				commentListView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					int click = event.getClickCount();
					if((click==1)&&()){ // 2�� Ŭ���� �۵� ��;
						
						handleEnterDetailBoard(Integer.valueOf(AppMain.bno));
	
					}
				}
				
			});
		}
		});*/

		
	
	}	


	public void refreshList() {

		Thread thread = new Thread(()-> {
			//new Comment
			try {
				// ������ ����
				Socket sock=new Socket();
				sock.connect(AppMain.serverAddress);
					
				// command ������
				OutputStream os = sock.getOutputStream();
				os.write("refreshComment".getBytes());
				os.write("\n".getBytes());
				JSONObject sendData = new JSONObject();
					
				sendData.put("bno", AppMain.bno);
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
				String refreshResult = new String(datas, 0, index);
//					System.out.println(refreshResult);
				if(refreshResult.equals("success")) {
					datas = new byte[10024];
					index = 0;
					while(true) {
						int data = is.read();
						if(data != '\n') {
							datas[index] = (byte)data;
							index++;
						} else {
							break;
						}
					}
					String receiveList = new String(datas, 0, index);
					JSONArray jArray = new JSONArray(receiveList);
//					System.out.println(receiveList);
					
					
					Platform.runLater(()->{
						AppMain.commentList.clear();
						for(int i=0; i<jArray.length();i++) {
							try {
								JSONObject jobj = new JSONObject();
								jobj = jArray.getJSONObject(i);
									
								HBox hbox;
								hbox = (HBox) FXMLLoader.load(getClass().getResource("comment_item.fxml"));
								Label writer = (Label) hbox.lookup("#comment_Writer");
								Label content = (Label) hbox.lookup("#comment_Content");
								Label time = (Label) hbox.lookup("#comment_Time");
								//Button rewriteBtn = (Button) hbox.lookup("#comment_RewriteBtn");
								Button deleteBtn = (Button) hbox.lookup("#comment_DeleteBtn");
								Label commentNo = (Label) hbox.lookup("#comment_No");
									
								comment.setCno(jobj.getInt("cno"));
								comment.setCcontent(jobj.getString("ccontent"));
								comment.setCwriter(jobj.getString("cwriter"));
								comment.setCwritedate(jobj.getString("cwritedate"));
								comment.setBno(jobj.getInt("bno"));
								
								AppMain.commentList.add(comment);
								
								commentNo.setText(Integer.toString(comment.getCno()));
								
								commentNo.setVisible(false);
								writer.setText(comment.getCwriter());
								content.setText(comment.getCcontent());
								time.setText(comment.getCwritedate());
								
								
								if(!comment.getCwriter().equals(AppMain.member.getMid())){
									//rewriteBtn.setDisable(true);
									deleteBtn.setDisable(true);
								}else{
									//rewriteBtn.setDisable(false);
									deleteBtn.setDisable(false);
								}
								
								
								deleteBtn.setOnAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent event) {
										Thread thread = new Thread(()-> {
											AppMain.cno=commentNo.getText();
											try {
												// ������ ����
												Socket sock=new Socket();
												sock.connect(AppMain.serverAddress);
												
												// command ������
												OutputStream os = sock.getOutputStream();
												os.write("deleteComment".getBytes());
												os.write("\n".getBytes());
												os.write(AppMain.cno.getBytes());
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
												String deleteCommentResult = new String(datas, 0, index);


												if(deleteCommentResult.equals("success")) {
													Platform.runLater(()->{
														try {
															StackPane root = boardsRoomController.instance;
															BorderPane sp =(BorderPane)FXMLLoader.load(getClass().getResource("../detailboard/detailBoard.fxml"));
															root.getChildren().add(sp);
															root.getChildren().remove(1);
														} catch (Exception e1) {
															e1.printStackTrace();
														}
												
													});
													System.out.println("���� ����");
													/*Platform.runLater(()->{
														try {
															StackPane root = boardsRoomController.instance;
															StackPane sp =(StackPane)FXMLLoader.load(getClass().getResource("../detailboard/detailBoard.fxml"));
															root.getChildren().add(sp);
															root.getChildren().remove(1);
														} catch (Exception e) {
													
															e.printStackTrace();
														}
													});*/
												} else {
													//������ �ޱ� �����϶� ���
													System.out.println("������ ���� ����");
												}
											}catch(Exception e1) {
												
											}finally {
												try {
													sock.close();
												} catch (IOException e1) {
													e1.printStackTrace();
												}
											}
										});
										thread.start();
									}
								});
								
								
								
								commentListView.getItems().add(hbox);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				} else {
						//������ �ޱ� �����϶� ���
				}
			}catch(Exception e) {}
		});
		thread.start();
		
	}
}


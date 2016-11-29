package client.boards;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import client.AppMain;
import client.model.Board;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;



public class BoardListController implements Initializable {
	public static BoardListController instance;

	
	//private int pageNo=1;
	private static int rowsPerPage=7;
	
	@FXML ListView<HBox> listView;
	
	private static Board board = new Board();
	public static String detailwriter;
	public static String area;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		
		refreshList(AppMain.pageNo);
		

	
		
		listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
			@Override
			public void changed(ObservableValue<? extends HBox> observable, HBox oldValue, HBox newValue) {
				//상세보기 할때 프로필 사진 뜨게 하는 것도 추가 해야 합니다.
				Label no = (Label) newValue.lookup("#boards_No");
			/*	Label title = (Label) newValue.lookup("#boards_Title");
				Label area = (Label) newValue.lookup("#boards_Area");
				Label time = (Label) newValue.lookup("#boards_Time");
				Label commentCount = (Label) newValue.lookup("#boards_CommentCount");
				Label hitCount = (Label) newValue.lookup("#boards_HitCount");*/
//				System.out.println(no.getText());
				AppMain.bno = String.valueOf(no.getText());
				listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {
					int click = event.getClickCount();
					if(click==2){ // 2번 클릭시 작동 됨;
						handleEnterDetailBoard(Integer.valueOf(AppMain.bno));
	
					}
				}
				
			});
		}
		});

	
	
	}	

	public void handleEnterDetailBoard(int bno){
		
		Thread thread = new Thread(()->{
			Socket socket = null;
			try {
				socket = new Socket();
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
					Platform.runLater(()->JSONToContorols(boardJSON));
				} else {
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		thread.start();
		
	}
	
	private void JSONToContorols(JSONObject boardJSON) {
		detailwriter = boardJSON.getString("bwriter");
		try {
			BorderPane root = FXMLLoader.load(getClass().getResource("detailboard/detailBoard.fxml"));
			Label detailTitle = (Label) root.lookup("#detailBoardTitle"); 
			Label detailWriter = (Label) root.lookup("#detailBoardWriter"); 
			Label detailTime = (Label) root.lookup("#detailBoardTime"); 
			TextArea detailContent = (TextArea) root.lookup("#detailBoardContent"); 
			Label detailCommentCount = (Label) root.lookup("#detailBoardCommentCount"); 
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
				
				boardsRoomController.instance.getChildren().add(root);
				boardsRoomController.instance.getChildren().get(0).setVisible(false);
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void refreshList(int pageNo) {
		if(AppMain.search_Area!=null) {
			
			Thread thread = new Thread(()-> {
				try {
//					System.out.println("장소 입력 했을 경우 : " + AppMain.search_Area);
					// 서버와 연결
					Socket sock=new Socket();
					sock.connect(AppMain.serverAddress);
					
					// command 보내기
					OutputStream os = sock.getOutputStream();
					os.write("searchBoard".getBytes());
					os.write("\n".getBytes());
//					System.out.println("Command : searchBoard");
					JSONObject sendData = new JSONObject();
					
					sendData.put("pageNo", pageNo);
					sendData.put("rowsPerPage", rowsPerPage);
					sendData.put("area", AppMain.search_Area);
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
					String refreshResult = new String(datas, 0, index);
//					System.out.println(refreshResult);
					if(refreshResult.equals("success")) {
						datas = new byte[1024];
						index = 0;
						while(true) {
							int data = is.read();
							if(data != '\n') {
								datas[index] = (byte)data;
								index++;
								//System.out.println("totalRows : " + index);
							} else {
								break;
							}
						}
						String totalRows = new String(datas, 0, index);
//						System.out.println(totalRows);
						AppMain.totalPage=(Integer.parseInt(totalRows)/rowsPerPage+1);
//						System.out.println("총 페이지 수: " + AppMain.totalPage);
						
						datas = new byte[10024];
						index = 0;
						while(true) {
							int data = is.read();
							if(data != '\n') {
								datas[index] = (byte)data;
								index++;
								//System.out.println("JSONArray index: " + index);
							} else {
								break;
							}
						}
						String receiveList = new String(datas, 0, index);
						JSONArray jArray = new JSONArray(receiveList);
//						System.out.println(receiveList);
					
					
					
					
						Platform.runLater(()->{
							AppMain.boardList.clear();
							for(int i=0; i<jArray.length();i++) {
								try {
									JSONObject jobj = new JSONObject();
									jobj = jArray.getJSONObject(i);
									
									HBox hbox;
									hbox = (HBox) FXMLLoader.load(getClass().getResource("item.fxml"));
									Label no = (Label) hbox.lookup("#boards_No");
									Label title = (Label) hbox.lookup("#boards_Title");
									Label area = (Label) hbox.lookup("#boards_Area");
									Label time = (Label) hbox.lookup("#boards_Time");
									//Label commentCount = (Label) hbox.lookup("#boards_CommentCount");
									//Label hitCount = (Label) hbox.lookup("#boards_HitCount");
								
									
									/*
									title.setText(jobj.getString("btitle"));
									area.setText(jobj.getString("barea"));
									time.setText(jobj.getString("bdate"));
									total.setText(jobj.getString("btotal"));
									hitCount.setText(jobj.getString("bhitcount"));
									*/
									
									board.setBno(jobj.getInt("bno"));
									board.setBtitle(jobj.getString("btitle"));
									board.setBcontent(jobj.getString("bcontent"));
									board.setBhitcount(jobj.getInt("bhitcount"));
									board.setBwriter(jobj.getString("bwriter"));
									board.setBwritedate(jobj.getString("bwritedate"));
									board.setBarea(jobj.getString("barea"));
									board.setBappointtime(jobj.getString("bappointtime"));
									board.setBappointday(jobj.getString("bappointday"));
									board.setBcommentcnt(jobj.getInt("bcommentcnt"));

									AppMain.boardList.add(board);
									
									no.setText(String.valueOf(board.getBno()));
									no.setStyle("-fx-font-weight: bold;");
									title.setText(board.getBtitle());
									area.setText(board.getBarea());
									time.setText(board.getBwritedate());
									//commentCount.setText(Integer.toString(board.getBcommentcnt()));
									//hitCount.setText(Integer.toString(board.getBhitcount()));
	
									listView.getItems().add(hbox);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						/*//데이터 받기 실패일때 출력
						Platform.runLater(()->{
							try{
								Stage dialog = new Stage(StageStyle.DECORATED);
								dialog.initModality(Modality.WINDOW_MODAL);
								dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
								Parent searchFail;
								searchFail = FXMLLoader.load(getClass().getResource("fail_search.fxml"));
								Button finishBtn = (Button) searchFail.lookup("#finishBtn");
								
								finishBtn.setOnAction((e)->{
									dialog.close();		
								});
								Scene sc = new Scene(searchFail);
								dialog.setScene(sc);
								dialog.setResizable(false);
								dialog.setTitle("확인");
								dialog.show();		
							}catch(Exception e) {}
						});	*/
					}
				}catch(Exception e) {}
			});
			thread.start();
			
		} else {
//			System.out.println("장소 미입력");
			Thread thread = new Thread(()-> {
				try {
					// 서버와 연결
					Socket sock=new Socket();
					sock.connect(AppMain.serverAddress);
					
					// command 보내기
					OutputStream os = sock.getOutputStream();
					os.write("boardRefresh".getBytes());
					os.write("\n".getBytes());
					JSONObject sendData = new JSONObject();
					
					sendData.put("pageNo", pageNo);
					sendData.put("rowsPerPage", rowsPerPage);
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
					String refreshResult = new String(datas, 0, index);
//					System.out.println(refreshResult);
					if(refreshResult.equals("success")) {
						datas = new byte[1024];
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
						String totalRows = new String(datas, 0, index);
//						System.out.println(totalRows);
						AppMain.totalPage=(Integer.parseInt(totalRows)/rowsPerPage+1);
						
						datas = new byte[10024];
						index = 0;
						while(true) {
							int data = is.read();
							if(data != '\n') {
								datas[index] = (byte)data;
								index++;
								//System.out.println(index);
							} else {
								break;
							}
						}
						String receiveList = new String(datas, 0, index);
						JSONArray jArray = new JSONArray(receiveList);
//						System.out.println(receiveList);
					
					
					
						Platform.runLater(()->{
							AppMain.boardList.clear();
							for(int i=0; i<jArray.length();i++) {
								try {
									JSONObject jobj = new JSONObject();
									jobj = jArray.getJSONObject(i);
									
									HBox hbox;
									hbox = (HBox) FXMLLoader.load(getClass().getResource("item.fxml"));
									Label no = (Label) hbox.lookup("#boards_No");
									Label title = (Label) hbox.lookup("#boards_Title");
									Label area = (Label) hbox.lookup("#boards_Area");
									Label time = (Label) hbox.lookup("#boards_Time");
									//Label commentCount = (Label) hbox.lookup("#boards_CommentCount");
									//Label hitCount = (Label) hbox.lookup("#boards_HitCount");
								
									
									/*
									title.setText(jobj.getString("btitle"));
									area.setText(jobj.getString("barea"));
									time.setText(jobj.getString("bdate"));
									total.setText(jobj.getString("btotal"));
									hitCount.setText(jobj.getString("bhitcount"));
									*/
									
									board.setBno(jobj.getInt("bno"));
									board.setBtitle(jobj.getString("btitle"));
									board.setBcontent(jobj.getString("bcontent"));
									board.setBhitcount(jobj.getInt("bhitcount"));
									board.setBwriter(jobj.getString("bwriter"));
									board.setBwritedate(jobj.getString("bwritedate"));
									board.setBarea(jobj.getString("barea"));
									board.setBappointtime(jobj.getString("bappointtime"));
									board.setBappointday(jobj.getString("bappointday"));
									board.setBcommentcnt(jobj.getInt("bcommentcnt"));
									AppMain.boardList.add(board);
									
									no.setText(String.valueOf(board.getBno()));
									no.setStyle("-fx-font-weight: bold;");
									title.setText(board.getBtitle());
									area.setText(board.getBarea());
									time.setText(board.getBwritedate());
									//commentCount.setText(Integer.toString(board.getBcommentcnt()));
									//hitCount.setText(Integer.toString(board.getBhitcount()));
	
									listView.getItems().add(hbox);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
					} else {
						/*//데이터 받기 실패일때 출력
						Platform.runLater(()->{
							try{
								Stage dialog = new Stage(StageStyle.DECORATED);
								dialog.initModality(Modality.WINDOW_MODAL);
								dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
								Parent searchFail;
								searchFail = FXMLLoader.load(getClass().getResource("fail_search.fxml"));
								Button finishBtn = (Button) searchFail.lookup("#finishBtn");
								
								finishBtn.setOnAction((e)->{
									dialog.close();	
								});
								Scene sc = new Scene(searchFail);
								dialog.setScene(sc);
								dialog.setResizable(false);
								dialog.setTitle("확인");
								dialog.show();		
							}catch(Exception e) {}
						});	*/
					}
				}catch(Exception e) {}
			});
			thread.start();
		}
	}
}


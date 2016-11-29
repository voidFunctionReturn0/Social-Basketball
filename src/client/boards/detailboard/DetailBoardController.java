package client.boards.detailboard;

import java.io.*;
import java.net.*;
import java.util.*;

import org.json.*;

import client.*;
import client.boards.*;
import client.model.*;
import javafx.application.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;

public class DetailBoardController implements Initializable{
	
	@FXML Button rewriteBtn;
	@FXML Button deleteBtn;
	@FXML Button btnMakeComment;
	@FXML Label boardWriter;
	@FXML TextField txt_makeComment;
	@FXML StackPane detailBoard_stackPane;
	@FXML private Label titleLbl;
	//@FXML private ImageView imgView;
	@FXML private Label writerLbl;
	@FXML private Label areaLbl;
	@FXML private Label timeLbl;
	@FXML private TextArea cntntLbl;

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Thread thread = new Thread(()->{
			// 해당 글에 대한 정보 서버에서 불러와서 초기화
			Socket sock = null;
			try {
				sock = new Socket();
				sock.connect(AppMain.serverAddress);
				
				// command : getBoard
				OutputStream os = sock.getOutputStream();
				os.write("getBoard".getBytes());
				os.write("\n".getBytes());
				
				// bno 보냄
				os.write(AppMain.bno.getBytes());
				os.flush();
				
				// 요청처리 결과받기
				InputStream is = sock.getInputStream();
				byte[] resultByte = new byte[10];
				int idx = 0;
				int data;
				while( (data=is.read()) != '\n' ) {
					resultByte[idx++] = (byte)data;
				}
				String result = new String(resultByte, 0, idx);
				
				// 결과에 따른 처리
				// 성공시 Board객체를 JSON객체로 받아서 파싱 후 UI에 반영
				if(result.equals("success")) {
					Platform.runLater(()->{
						try {
							byte[] boardJSON = new byte[10];
							int idxJSON = 0;
							int dataJSON;
							while( (dataJSON=is.read()) != '\n' ) {
								boardJSON[idxJSON++] = (byte)dataJSON;
							}
							JSONObject board = new JSONObject(new String(boardJSON, 0, idxJSON));
							// 제목, 글쓴이, 지역, 약속시간, 내용 보여주기
							titleLbl.setText(board.getString("btitle"));
							writerLbl.setText(board.getString("bwriter"));
							areaLbl.setText(board.getString("barea"));
							cntntLbl.setText(board.getString("bcontent"));
						} catch(Exception e) {
							e.printStackTrace();
						}
					});
					
					
				// 실패시 불러오기실패 다이얼로그 띄우고 목록보기로 돌아감
				} else {
					Platform.runLater(()->{
						try {
							Stage dialog = new Stage(StageStyle.DECORATED);
							dialog.initModality(Modality.WINDOW_MODAL);
							dialog.initOwner(deleteBtn.getScene().getWindow());
							Parent adjustFail = FXMLLoader.load(getClass().getResource("fail_load.fxml"));
							Button okBtn = (Button) adjustFail.lookup("#okBtn");
							okBtn.setOnAction((e2)->{
								dialog.close();
								((Stage)deleteBtn.getScene().getWindow()).close();
							});
							Scene sc = new Scene(adjustFail);
							dialog.setScene(sc);
							dialog.setResizable(false);
							dialog.setTitle("불러오기오류");
							dialog.show();
						} catch(IOException e2) { }
					});
				}
				
				

			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sock.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		thread.start();
		
		
		// 글쓴 아이디랑 로그인된 아이디가 같을 경우에만 수정하기 삭제하기 버튼 활성화
		if(!AppMain.member.getMid().equals(BoardListController.detailwriter)){
			rewriteBtn.setDisable(true);
			deleteBtn.setDisable(true);
		}else{
			rewriteBtn.setDisable(false);
			deleteBtn.setDisable(false);
		}
		
		btnMakeComment.setOnAction(e->handleBtnMakeComment(e));
		
		try {
			ListView listView = (ListView) FXMLLoader.load(getClass().getResource("../comment/commentList.fxml"));
			detailBoard_stackPane.getChildren().add(listView);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		
	}
	public void handleBtnMakeComment(ActionEvent e) {
		Thread thread = new Thread(()-> {
			Comment comment = new Comment();
			comment.setCwriter(AppMain.member.getMid());
			comment.setCcontent(txt_makeComment.getText());
			comment.setBno(Integer.parseInt(AppMain.bno));
				
			Socket sock = null;
			try {
				// 서버와 연결
				sock = new Socket();
				sock.connect(AppMain.serverAddress);
				
				// command 보내기
				OutputStream os = sock.getOutputStream();
				os.write("makeComment".getBytes());
				os.write("\n".getBytes());
				JSONObject sendData = new JSONObject();
				
				sendData.put("cwriter", comment.getCwriter());
				sendData.put("ccontent", comment.getCcontent());
				sendData.put("bno", comment.getBno());
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
				String makeCommentResult = new String(datas, 0, index);
				System.out.println(makeCommentResult);
	
				
				if(makeCommentResult.equals("success")) {
					Platform.runLater(()->{
						try {
							StackPane root = boardsRoomController.instance;
							BorderPane sp =(BorderPane)FXMLLoader.load(getClass().getResource("detailBoard.fxml"));
							root.getChildren().add(sp);
							root.getChildren().remove(1);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
				
					});
				} else {
					//데이터 받기 실패일때 출력
//					System.out.println("데이터 저장 실패");
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
		
		
	
	public void handleDeleteBtn(){
		try {
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
			Parent dia = (Parent)FXMLLoader.load(getClass().getResource("../deleteCheck.fxml"));
	
			Button deleteOkBtn = (Button) dia.lookup("#deleteOk");
			Button deleteCancelBtn =  (Button) dia.lookup("#deleteCancel");
			
			deleteCancelBtn.setOnAction((e)->dialog.close());
			deleteOkBtn.setOnAction((e)->{
				dialog.close();
				handledeleteBoard();
				
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
	
	}
	
	public void handledeleteBoard(){ //삭제 처리
		Thread thread = new Thread(()-> {
			
			Socket sock = null;
			try {
				// 서버와 연결
				sock = new Socket();
				sock.connect(AppMain.serverAddress);
				
				// command 보내기
				OutputStream os = sock.getOutputStream();
				os.write("deleteBoard".getBytes());
				os.write("\n".getBytes());
				os.write(AppMain.bno.getBytes());
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
				String deleteCommentResult = new String(datas, 0, index);


				if(deleteCommentResult.equals("success")) {
					Platform.runLater(()->{
						try {
							StackPane root = boardsRoomController.instance;
							BorderPane sp =(BorderPane)FXMLLoader.load(getClass().getResource("detailBoard.fxml"));
							root.getChildren().add(sp);
							root.getChildren().remove(1);
						} catch (Exception e) {
					
							e.printStackTrace();
						}
					});
				} else {
					//데이터 받기 실패일때 출력
					System.out.println("데이터 저장 실패");
				}
			}catch(Exception e1) {
			} finally {
				try {
					sock.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	
	
	
	public void handleRewriteBtn(){
		try {
			Stage dialog = new Stage(StageStyle.DECORATED);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.initOwner(AppMain.primaryStage.getScene().getWindow());
			Parent dia = (Parent)FXMLLoader.load(getClass().getResource("../rewriteCheck.fxml"));
	
			Button rewriteOkBtn = (Button) dia.lookup("#rewriteOk");
			Button rewriteCancelBtn =  (Button) dia.lookup("#rewriteCancel");
			
			rewriteCancelBtn.setOnAction((e)->dialog.close());
			rewriteOkBtn.setOnAction((e)->{
				dialog.close();
				try {
					BorderPane root = FXMLLoader.load(getClass().getResource("../updateBoard/updateBoard.fxml"));
					boardsRoomController.instance.getChildren().add(root);
					boardsRoomController.instance.getChildren().get(0).setVisible(false);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
	}
	
	public void enterList(){
		try {
			StackPane root = boardsRoomController.instance;
			StackPane sp =(StackPane)FXMLLoader.load(getClass().getResource("../boardsRoom.fxml"));
			root.getChildren().add(sp);
			root.getChildren().remove(1);
		} catch (Exception e) {
	
			e.printStackTrace();
		}
	}
	
	
}

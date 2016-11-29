package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import server.controller.*;


public class ServerController implements Initializable {
	@FXML private Button startStopBtn;
	@FXML private TextArea txtDisplay;
	private ServerSocket servSock;
	private ExecutorService executorService;
	public static ServerController instance;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;	// 요청처리클래스에서 스레드풀을 이용할 수 있도록 instance만듦
		startStopBtn.setText("start");
	}
	
	public void handleStartStopBtn(ActionEvent e) {
		if(startStopBtn.getText().equals("start")) {
			startServer();
			displayText("[서버 시작]");
			startStopBtn.setText("stop");
		} else if(startStopBtn.getText().equals("stop")) {
			stopServer();
			displayText("[서버 종료]");
			startStopBtn.setText("start");
		} else {
			System.out.println("버튼의 text가 잘못되었습니다.");
		}
	}
	
	private void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}

	private void startServer() {
		// 스레드풀 생성
		executorService = Executors.newFixedThreadPool(
				100);
		
		// 서버소켓생성 & 포트바인딩
		try {
			servSock = new ServerSocket();
			servSock.bind(new InetSocketAddress("192.168.0.105", 50001));
		} catch(IOException e) {
			e.printStackTrace();
			if(!servSock.isClosed()) { stopServer(); }
			return;
		}
			
		// accept() 호출을 위한 작업스레드를 스레드풀에 넣기
		executorService.submit(()->{
			while(true) {
				try {
					Socket sock = servSock.accept();
					String msgAccpt = "[연결 수락: " + sock.getRemoteSocketAddress() + "]";
					Platform.runLater( () -> displayText(msgAccpt) );
					executorService.submit(()->{
						try {
							InputStream is = sock.getInputStream();
							OutputStream os = sock.getOutputStream();
							
							byte[] datas = new byte[1024];
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
							String command = new String(datas, 0, index);	
							System.out.println(command+ "작동");
							
							// 요청문자열에 따른 처리
							if(command.equals("idCheck")) {
								ServerIdCheckController.execute(is, os);
								
							}else if(command.equals("join")) {
								ServerJoinController.execute(is, os);
								
							}else if(command.equals("login")) {
								ServerLoginController.execute(is, os);
								
							}else if(command.equals("getPicture")) {
								ServerGetPictureController.execute(is, os);
								
							}else if(command.equals("updateProfile")) {
								ServerUpdateProfileController.execute(is, os);

							}else if(command.equals("boardRefresh")) {
								
								ServerBoardRefreshController.execute(is, os);

							}else if(command.equals("getBoard")) {
								ServerGetBoardController.execute(is, os);
							
							}else if(command.equals("makeBoard")) {
								ServerMakeBoardController.execute(is, os);
								
							}else if(command.equals("searchBoard")) {
								ServerSearchBoardController.execute(is, os);
							
							}else if(command.equals("updateBoard")) {
								ServerUpdateBoardController.execute(is, os);
							}else if(command.equals("deleteBoard")) {
								ServerDeleteBoardController.execute(is, os);
							}else if(command.equals("makeComment")) {
								ServerMakeCommentController.execute(is, os);
							}else if(command.equals("refreshComment")) {
								ServerRefreshCommentController.execute(is, os);
							}else if(command.equals("deleteComment")) {
								ServerDeleteCommentController.execute(is, os);
							}
							
							
						} catch(Exception e) {
							e.printStackTrace();
						} finally {
							try {
								sock.close();
							} catch (Exception e) {
									
							}
						}
					});
				} catch(Exception e) {
					if(!servSock.isClosed()) { stopServer(); }
				}
			}
		});
	}
	
	// 연결된 소켓들과 연결종료, Vector<Client>비우기, 서버소켓닫기, 스레드풀닫기 
	private void stopServer() {
		if(servSock!=null && servSock.isClosed()) {
			try {
				servSock.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(executorService!=null && executorService.isShutdown()) {
			executorService.shutdown();
		}
	}
}
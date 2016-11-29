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
		instance = this;	// ��ûó��Ŭ�������� ������Ǯ�� �̿��� �� �ֵ��� instance����
		startStopBtn.setText("start");
	}
	
	public void handleStartStopBtn(ActionEvent e) {
		if(startStopBtn.getText().equals("start")) {
			startServer();
			displayText("[���� ����]");
			startStopBtn.setText("stop");
		} else if(startStopBtn.getText().equals("stop")) {
			stopServer();
			displayText("[���� ����]");
			startStopBtn.setText("start");
		} else {
			System.out.println("��ư�� text�� �߸��Ǿ����ϴ�.");
		}
	}
	
	private void displayText(String text) {
		txtDisplay.appendText(text + "\n");
	}

	private void startServer() {
		// ������Ǯ ����
		executorService = Executors.newFixedThreadPool(
				100);
		
		// �������ϻ��� & ��Ʈ���ε�
		try {
			servSock = new ServerSocket();
			servSock.bind(new InetSocketAddress("192.168.0.105", 50001));
		} catch(IOException e) {
			e.printStackTrace();
			if(!servSock.isClosed()) { stopServer(); }
			return;
		}
			
		// accept() ȣ���� ���� �۾������带 ������Ǯ�� �ֱ�
		executorService.submit(()->{
			while(true) {
				try {
					Socket sock = servSock.accept();
					String msgAccpt = "[���� ����: " + sock.getRemoteSocketAddress() + "]";
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
							System.out.println(command+ "�۵�");
							
							// ��û���ڿ��� ���� ó��
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
	
	// ����� ���ϵ�� ��������, Vector<Client>����, �������ϴݱ�, ������Ǯ�ݱ� 
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
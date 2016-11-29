package server.controller;

import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

import server.jdbc.Board;
import server.jdbc.BoardDao;

public class ServerDeleteBoardController {
	public static void execute(InputStream is, OutputStream os) throws Exception{
		try {
			boolean isSuccess = false;
			BoardDao boardDao = new BoardDao();
			Board board = new Board();
			
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
			String receiveData = new String(datas, 0, index);

			int num = boardDao.deleteByPk(Integer.valueOf(receiveData));
			if(num!=0) {
				isSuccess=true;
			}
			
			if(isSuccess==true) {			
				os.write("success".getBytes());
				os.write("\n".getBytes());			
//				System.out.println("서버 : DB입력 성공");
			} else {
				os.write("fail".getBytes());
				os.write("\n".getBytes());
			}
			os.flush();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			os.write("fail".getBytes());
			os.write("\n".getBytes());
			os.flush();
		}
		
	}
}

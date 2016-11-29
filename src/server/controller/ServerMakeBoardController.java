package server.controller;

import java.io.*;

import org.json.*;

import server.jdbc.*;

public class ServerMakeBoardController {
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
			JSONObject receiveJson = new JSONObject(receiveData);
			
			board.setBwriter(receiveJson.getString("bwriter"));
			board.setBtitle(receiveJson.getString("btitle"));
			board.setBappointday(receiveJson.getString("bappointday"));
			board.setBappointtime(receiveJson.getString("bappointtime"));
			board.setBarea(receiveJson.getString("barea"));
			board.setBcontent(receiveJson.getString("bcontent"));
			
			int bno = boardDao.insert(board);
			
			if(bno!=0) {
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
			os.write("fail".getBytes());
			os.write("\n".getBytes());
			os.flush();
		}
		
	}

}

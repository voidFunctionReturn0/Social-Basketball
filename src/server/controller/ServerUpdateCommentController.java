package server.controller;

import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

import server.jdbc.Board;
import server.jdbc.BoardDao;

public class ServerUpdateCommentController {
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
			System.out.println(receiveData);
			
			JSONObject receiveJson = new JSONObject(receiveData);
			int bno =Integer.valueOf(receiveJson.getString("bno"));
			board.setBno(bno);
			board.setBwriter(receiveJson.getString("bwriter"));
			board.setBtitle(receiveJson.getString("btitle"));
			board.setBappointday(receiveJson.getString("bappointday"));
			board.setBappointtime(receiveJson.getString("bappointtime"));
			board.setBarea(receiveJson.getString("barea"));
			board.setBcontent(receiveJson.getString("bcontent"));
			int num = boardDao.update(board);
			
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

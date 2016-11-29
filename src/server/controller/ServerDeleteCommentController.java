package server.controller;

import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

import server.jdbc.Board;
import server.jdbc.BoardDao;
import server.jdbc.Comment;
import server.jdbc.CommentDao;

public class ServerDeleteCommentController {
	public static void execute(InputStream is, OutputStream os) throws Exception{
		try {
			boolean isSuccess = false;
			CommentDao commentDao = new CommentDao();
			Comment comment = new Comment();
			
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

			int num = commentDao.deleteByPk(Integer.valueOf(receiveData));
			if(num!=0) {
				isSuccess=true;
			}
			
			if(isSuccess==true) {			
				os.write("success".getBytes());
				os.write("\n".getBytes());			

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

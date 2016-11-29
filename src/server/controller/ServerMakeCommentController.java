package server.controller;

import java.io.*;

import org.json.*;

import server.jdbc.*;

public class ServerMakeCommentController {
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
			JSONObject receiveJson = new JSONObject(receiveData);
			
			comment.setCwriter(receiveJson.getString("cwriter"));
			comment.setCcontent(receiveJson.getString("ccontent"));
			comment.setBno(receiveJson.getInt("bno"));
			
			int cno = commentDao.insert(comment);
			
			if(cno!=0) {
				isSuccess=true;
			}
			
			if(isSuccess==true) {			
				// bno에 해당하는 Board객체의 댓글수에 1더하기
				BoardDao bDao = new BoardDao();
				Board board = bDao.selectByPk(comment.getBno());
				board.setBcommentcnt(board.getBcommentcnt()+1);
				int rows = bDao.update(board);
				
				if(rows != 0) {
					os.write("success".getBytes());
					os.write("\n".getBytes());			
//					System.out.println("서버 : DB입력 성공");
				} else {
					throw new Exception();
				}
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

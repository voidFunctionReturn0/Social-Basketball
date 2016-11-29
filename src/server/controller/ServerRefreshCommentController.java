package server.controller;

import java.io.*;
import java.util.*;

import org.json.*;

import server.jdbc.*;


public class ServerRefreshCommentController {
	public static void execute(InputStream is, OutputStream os) throws Exception{
		try {
			
			boolean isSuccess=false;
			CommentDao commentDao = new CommentDao();
			List<Comment> list = new ArrayList<Comment>();
			
			
			byte[] datas = new byte[100024];
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
			int bno = receiveJson.getInt("bno");
			
			
			
			
			
			list=commentDao.selectAll(bno);
			
			if(!list.isEmpty()) {				
				isSuccess=true;
			} else {
				isSuccess=false;
			}
			
			
			
			if(isSuccess==true) {
				int totalSize = 0;
				totalSize = commentDao.count();
				
				
				os.write("success".getBytes());
				os.write("\n".getBytes());
				
				JSONArray dataArray = new JSONArray(list);
				System.out.println(dataArray.toString());
				os.write(dataArray.toString().getBytes());
				os.write("\n".getBytes());
				System.out.println("Comment 리스트 데이터 보냄");
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

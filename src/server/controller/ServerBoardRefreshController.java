package server.controller;

import java.io.*;
import java.util.*;

import org.json.*;

import server.jdbc.*;


public class ServerBoardRefreshController {
	public static void execute(InputStream is, OutputStream os) throws Exception{
		try {
			
			boolean isSuccess=false;
			BoardDao boardDao = new BoardDao();
			List<Board> list = new ArrayList<Board>();
			
			
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
			int pageNo = receiveJson.getInt("pageNo");
			int rowsPerPage = receiveJson.getInt("rowsPerPage");
			
			
			
			
			list=boardDao.getListByPage(pageNo, rowsPerPage);
			
			if(!list.isEmpty()) {				
				isSuccess=true;
			} else {
				isSuccess=false;
			}
			
			
			
			if(isSuccess==true) {
				int totalSize = 0;
				totalSize = boardDao.count();
				
				
				os.write("success".getBytes());
				os.write("\n".getBytes());
				os.write(Integer.toString(totalSize).getBytes());
				os.write("\n".getBytes());
				
				JSONArray dataArray = new JSONArray(list);

				//System.out.println(dataArray.toString());
				
				/*for(int i=0; i<dataArray.length();i++) {
					System.out.println(dataArray.getJSONObject(i));
				}*/
				
				os.write(dataArray.toString().getBytes());
				os.write("\n".getBytes());
//				System.out.println("리스트 데이터 보냄");
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

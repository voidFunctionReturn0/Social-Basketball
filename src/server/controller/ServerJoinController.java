package server.controller;

import java.io.*;

import org.json.*;

import server.*;
import server.jdbc.*;




public class ServerJoinController {
	public static void execute(InputStream is, OutputStream os) throws Exception{
		try {
			byte[] datas = new byte[1048576];
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
			JSONObject joinJSON = new JSONObject(new String(datas, 0, index));
			
			// 클라이언트에서 받은 data로 Member객체만들기
			String mid = joinJSON.getString("mid");
			String mpassword = joinJSON.getString("mpassword");
			String mheight = joinJSON.getString("mheight");
			String msex = joinJSON.getString("msex");
			String mposition = joinJSON.getString("mposition");
			String marea = joinJSON.getString("marea");
			String mlevel = joinJSON.getString("mlevel");
			String mimage = joinJSON.getString("mimage");
			int fileSize = joinJSON.getInt("fileSize");
			
			Member member = new Member(mid, mpassword, mheight, msex, mposition, marea, mlevel, mimage);
			
			// DB에 member를 저장
			MemberDao memberDao = new MemberDao();
			boolean isSuccess = memberDao.insert(member);
			
			// 이미지파일이 있으면 images/id디렉토리에 저장
			if(!mimage.equals("")) {			
				File file = new File(ServerAppMain.class.getResource("images/").toURI());
				FileOutputStream fos = new FileOutputStream(file.getPath() + "/" + mimage);
				byte[] imageData = new byte[1024];
				int totalReadNum = 0;
				int readByteNum = -1;
				while(true) {
					readByteNum = is.read(imageData);
					fos.write(imageData, 0, readByteNum);
					totalReadNum += readByteNum;
					if(totalReadNum == fileSize) {
						break;
					}
				}
				fos.flush();
				fos.close();
			}
			
			
			if(isSuccess==true){
				os.write("success".getBytes());
				os.write("\n".getBytes());
			} else {
				os.write("fail".getBytes());
				os.write("\n".getBytes());
			}
			os.flush();
		} catch(Exception e) {
			e.printStackTrace();
			os.write("fail".getBytes());
			os.write("\n".getBytes());
			os.flush();
		}
	}
}

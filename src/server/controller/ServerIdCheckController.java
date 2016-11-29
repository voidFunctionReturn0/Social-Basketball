package server.controller;

import java.io.*;
import java.net.*;

import server.jdbc.*;

public class ServerIdCheckController {
	public static void execute(InputStream is, OutputStream os) throws Exception {
		byte[] datas = new byte[100];
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
		String mid = new String(datas, 0, index);	
	
		MemberDao memberDao = new MemberDao();
		boolean isSuccess = memberDao.isNotMid(mid);
		
		if(isSuccess==true){
			os.write("success".getBytes());
			os.write("\n".getBytes());
		} else {
			os.write("fail".getBytes());
			os.write("\n".getBytes());
		}
		os.flush();
		

	}
}



package server.controller;

import java.io.*;

import org.json.*;

public class ServerUploadController {
	public static void execute(InputStream is, OutputStream os) {
		try {
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
			
			String fileName = new String(datas, 0, index);	
			
			
			//나머지는 모두 파일, 서버의 해당 장소에 파일 저장 
			FileOutputStream fos = new FileOutputStream("C:/Temp/" + fileName);
			byte[] data = new byte[1024];
			int readByteNum = -1;
			while((readByteNum = is.read(data)) != -1) {
				fos.write(data, 0, readByteNum);
			}
			fos.flush();
			fos.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	
	}
	
}

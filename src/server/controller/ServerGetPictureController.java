package server.controller;

import java.io.*;

public class ServerGetPictureController {
	public static void execute(InputStream is, OutputStream os) {
		try {
			// 사진 파일명 수신
			byte[] mimageBytes = new byte[1024];
			int index = 0;
			int data;
			while(true) {
				data = is.read();
				if(data != '\n') {
					mimageBytes[index++] = (byte)data;
				} else {
					break;
				}
			}
			String mimage = new String(mimageBytes, 0, index);
			if(mimage.equals("")) {
				mimage = "profile.png";
			}
			
			// 사진이미지를 송신
			File imageFile = new File(ServerGetPictureController.class.getResource("../images/" + mimage).toURI());
			int fileSize = (int)imageFile.length();
			FileInputStream fis = new FileInputStream(imageFile.getPath());
			int readByteNum = -1;
			int totalByteNum = 0;
			byte[] datas = new byte[1024];
			
			while(true) {
				readByteNum = fis.read(datas);
				totalByteNum += readByteNum;
				os.write(datas);
				if(totalByteNum == fileSize) {
					break;
				}
			}
			os.flush();
			fis.close();
			is.close();
			os.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

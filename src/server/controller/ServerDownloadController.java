package server.controller;

import java.io.*;

import org.json.*;

public class ServerDownloadController {
	public static void execute(InputStream is, OutputStream os) {
		//JSONObject resultJSON = new JSONObject();
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
			//다운로드한 파일이름 얻기
			String fileName = new String(datas, 0, index);	
//			System.out.println("fileName: " + fileName);
			
			//우선 파일존재 여부 검사 
			//파일객체는 파일존재여부 메소드가 존재해서 파일 객체 생성
			File file = new File("C:/Temp/" + fileName); 
			if(!file.exists()) return ;
			
			FileInputStream fis = new FileInputStream("C:/Temp/" + fileName);
			byte[] data = new byte[1024];
			int readByteNum = -1;
			while((readByteNum = fis.read(data)) != -1) {
				os.write(data, 0, readByteNum); //client에게 파일 전송
			}
			fis.close();
			os.flush();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
/*	public JSONObject returnVal(JSONObject jsonObject) {
		JSONObject resultJSON = jsonObject;
		boolean isSuccess=false;
		if(isSuccess==true){
			// 클라이언트에게 가입이 완료되었음을 알리도록 JSONObject 설정
			resultJSON.put("command", "uploadResult");
			resultJSON.put("data", "success");
		} else {
			// 클라이언트에게 가입이 실패되었음을 알리도록 JSONObject 설정
			resultJSON.put("command", "uploadResult");
			resultJSON.put("data", "fail");
		}
		return resultJSON;
	}*/
}

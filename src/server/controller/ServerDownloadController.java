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
			//�ٿ�ε��� �����̸� ���
			String fileName = new String(datas, 0, index);	
//			System.out.println("fileName: " + fileName);
			
			//�켱 �������� ���� �˻� 
			//���ϰ�ü�� �������翩�� �޼ҵ尡 �����ؼ� ���� ��ü ����
			File file = new File("C:/Temp/" + fileName); 
			if(!file.exists()) return ;
			
			FileInputStream fis = new FileInputStream("C:/Temp/" + fileName);
			byte[] data = new byte[1024];
			int readByteNum = -1;
			while((readByteNum = fis.read(data)) != -1) {
				os.write(data, 0, readByteNum); //client���� ���� ����
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
			// Ŭ���̾�Ʈ���� ������ �Ϸ�Ǿ����� �˸����� JSONObject ����
			resultJSON.put("command", "uploadResult");
			resultJSON.put("data", "success");
		} else {
			// Ŭ���̾�Ʈ���� ������ ���еǾ����� �˸����� JSONObject ����
			resultJSON.put("command", "uploadResult");
			resultJSON.put("data", "fail");
		}
		return resultJSON;
	}*/
}

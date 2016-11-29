package server.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.json.JSONObject;

import server.jdbc.Member;
import server.jdbc.MemberDao;



public class ServerLoginController {
	public static void execute(InputStream is, OutputStream os) {
		try {
			// id,password�� JSON��ü�� �ޱ�
			int data;
			byte[] datas = new byte[1024];
			int index = 0;
			while(true) {
				data = is.read();
				if(data != '\n') {
					datas[index++] = (byte)data;
				} else {
					break;
				}
			}
			JSONObject idPw = new JSONObject(new String(datas,0, index));
			
			// Ŭ���̾�Ʈ���� ���� �����ͷ� id, password ���ڿ� �����
			String mid = idPw.getString("mid");
			String mpassword = idPw.getString("mpassword");
			
			// DB���� mid�� �ش��ϴ� ��ü �ҷ�����
			MemberDao memberDao = new MemberDao();
			Member member = memberDao.selectByMid(mid);

			// DB�� mid�� �ش��ϴ� ������ ������ ����ó��
			if(member == null) {
				sendFail(os);
			}
			
			// ��ûó����� �۽�(���̵� �ش��ϴ� ��й�ȣ�� ��ġ�ϸ� ����, �ƴϸ� ����)
			if(member.getMpassword().equals(mpassword)) {
				// �����̸� ó����� ������ ȸ�������� JSON��ü�� ������
				os.write("success".getBytes());
				os.write("\n".getBytes());
				os.flush();
				
				JSONObject memberJSON = memToJSON(member);
				os.write(memberJSON.toString().getBytes());
				os.write("\n".getBytes());
				os.flush();
				
			} else {
				sendFail(os);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			sendFail(os);
		}
	}
	
	// Member��ü�� JSON��ü�� ��ȯ
	private static JSONObject memToJSON(Member member) {
		JSONObject memJSON = new JSONObject();
		
		memJSON.put("mid", member.getMid());
		memJSON.put("mpassword", member.getMpassword());
		memJSON.put("mheight", member.getMheight());
		memJSON.put("msex", member.getMsex());
		memJSON.put("mposition", member.getMposition());
		memJSON.put("marea", member.getMarea());
		memJSON.put("mlevel", member.getMlevel());
		if(member.getMimage()==null){
			memJSON.put("mimage", "nullProfile.png");
		}else{
			memJSON.put("mimage", member.getMimage());
		}
		System.out.println(memJSON.toString());
		return memJSON;
	}
	
	// ����ó�� �޼ҵ�
	private static void sendFail(OutputStream os) {
		try {
			os.write("fail".getBytes());
			os.write("\n".getBytes());
			os.flush();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}

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
			// id,password를 JSON객체로 받기
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
			
			// 클라이언트에서 받은 데이터로 id, password 문자열 만들기
			String mid = idPw.getString("mid");
			String mpassword = idPw.getString("mpassword");
			
			// DB에서 mid에 해당하는 객체 불러오기
			MemberDao memberDao = new MemberDao();
			Member member = memberDao.selectByMid(mid);

			// DB에 mid에 해당하는 정보가 없으면 실패처리
			if(member == null) {
				sendFail(os);
			}
			
			// 요청처리결과 송신(아이디에 해당하는 비밀번호가 일치하면 성공, 아니면 실패)
			if(member.getMpassword().equals(mpassword)) {
				// 성공이면 처리결과 보내고 회원정보를 JSON객체로 보내기
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
	
	// Member객체를 JSON객체로 변환
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
	
	// 실패처리 메소드
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

package server.controller;

import java.io.*;

import org.json.JSONObject;

import server.jdbc.*;

public class ServerUpdateProfileController {
	public static void execute(InputStream is, OutputStream os) {
		try{
			byte[] datas = new byte[1048576];
			int idx = 0;
			int data;
			
			while(true) {
				data = is.read();
				if(data != '\n') {
					datas[idx++] = (byte)data;
				} else {
					break;
				}
			}
			
			JSONObject memberJSON = new JSONObject(new String(datas, 0, idx));
			
			// 클라이언트에서 받은 data로 Member객체만들기
			String mid = memberJSON.getString("mid");
			String mpassword = memberJSON.getString("mpassword");
			String mheight = memberJSON.getString("mheight");
			String msex = memberJSON.getString("msex");
			String mposition = memberJSON.getString("mposition");
			String marea = memberJSON.getString("marea");
			String mlevel = memberJSON.getString("mlevel");
			String mimage = memberJSON.getString("mimage");
					
			Member member = new Member(mid, mpassword, mheight, msex, mposition, marea, mlevel, mimage);
			
			// 이미지파일이 변경되었는지 확인해서 다르면 이미지파일받기
			MemberDao memberDao = new MemberDao();
			Member preMember = memberDao.selectByMid(mid);
			
			
//			System.out.println(preMember.getMimage());
//			System.out.println(mimage);
			
			if(!mimage.equals(preMember.getMimage())) {
				
//				System.out.println("이미지파일이 변경되었다고 인식");
				
				// 변경된 이미지가 디폴트이미지인지 확인
				if(!mimage.equals("")) {
					// 이미지파일 크기 받기
					byte[] sizeDatas = new byte[1024];
					int sizeData;
					int sizeIdx = 0;
					while(true) {
						sizeData = is.read();
						if(sizeData != '\n') {
							sizeDatas[sizeIdx++] = (byte)sizeData;
						} else {
							break;
						}
					}
					String sizeStr = new String(sizeDatas, 0, sizeIdx);
					Long fileSize = new Long(sizeStr);
					
//					System.out.println("파일크기받음");
					
					// 파일크기만큼 파일데이터 수신
					File file = new File(ServerUpdateProfileController.class.getResource("../images").toURI());
					FileOutputStream fos = new FileOutputStream(file.getPath() + "/" + mimage);
//					System.out.println(file.getPath());
					byte[] imageDatas = new byte[1024];
					int readByteNum = -1;
					int totalByteNum = 0;
					while(true) {
						readByteNum = is.read(imageDatas);
						fos.write(imageDatas, 0, readByteNum);
						totalByteNum += readByteNum;
//						System.out.println(totalByteNum);
						if(totalByteNum == fileSize) {
							fos.flush();
							break;
						}
					}
					fos.close();
					
//					System.out.println("이미지받음");
				
				// 변경된 이미지가 디폴트 이미지인 경우
				} else {
					
				}
			}
			
			// DB에 Member객체 수정 요청
			boolean isSuccess = memberDao.update(member);
			
			if(isSuccess==true){
				os.write("success".getBytes());
				os.write("\n".getBytes());
			} else {
//				System.out.println("fail1");
				os.write("fail".getBytes());
				os.write("\n".getBytes());
			}
			os.flush();
			
		} catch(Exception e) {
			e.printStackTrace();
			try {
//				System.out.println("fail2");
				os.write("fail".getBytes());
				os.write("\n".getBytes());
				os.flush();
			} catch(IOException e2) {
				e2.printStackTrace();
			}
		}
		
	}

}

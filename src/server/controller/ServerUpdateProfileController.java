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
			
			// Ŭ���̾�Ʈ���� ���� data�� Member��ü�����
			String mid = memberJSON.getString("mid");
			String mpassword = memberJSON.getString("mpassword");
			String mheight = memberJSON.getString("mheight");
			String msex = memberJSON.getString("msex");
			String mposition = memberJSON.getString("mposition");
			String marea = memberJSON.getString("marea");
			String mlevel = memberJSON.getString("mlevel");
			String mimage = memberJSON.getString("mimage");
					
			Member member = new Member(mid, mpassword, mheight, msex, mposition, marea, mlevel, mimage);
			
			// �̹��������� ����Ǿ����� Ȯ���ؼ� �ٸ��� �̹������Ϲޱ�
			MemberDao memberDao = new MemberDao();
			Member preMember = memberDao.selectByMid(mid);
			
			
//			System.out.println(preMember.getMimage());
//			System.out.println(mimage);
			
			if(!mimage.equals(preMember.getMimage())) {
				
//				System.out.println("�̹��������� ����Ǿ��ٰ� �ν�");
				
				// ����� �̹����� ����Ʈ�̹������� Ȯ��
				if(!mimage.equals("")) {
					// �̹������� ũ�� �ޱ�
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
					
//					System.out.println("����ũ�����");
					
					// ����ũ�⸸ŭ ���ϵ����� ����
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
					
//					System.out.println("�̹�������");
				
				// ����� �̹����� ����Ʈ �̹����� ���
				} else {
					
				}
			}
			
			// DB�� Member��ü ���� ��û
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

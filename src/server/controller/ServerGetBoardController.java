package server.controller;

import java.io.*;

import org.json.JSONObject;

import server.jdbc.*;

// bno�� �޾Ƽ� ó������� �����̸� Board�� ������
public class ServerGetBoardController {
	public static void execute(InputStream is, OutputStream os) {
		try {
			// data : "bno" �ޱ�
			byte[] datas = new byte[10];
			int data;
			int idx = 0;
			while( (data=is.read()) != '\n' ) {
				datas[idx++] = (byte)data;
			}

			String bnoStr = new String(datas, 0, idx);
			int bno = Integer.parseInt(bnoStr);
			
			// DB���� bno�� Board�� �޾ƿ���
			BoardDao bDao = new BoardDao();
			Board board = bDao.selectByPk(bno);
			
			// ��ûó����� ������		
			//������ "success" ������ Board��ü�� JSON��ü�� ����
			if(board != null) {
				os.write("success".getBytes());
				os.write("\n".getBytes());
				JSONObject boardJSON = boardToJSON(board);
				os.write(boardJSON.toString().getBytes());
				os.write("\n".getBytes());
			
			// ���н�
			} else {
				throw new IOException();
			}
		// "fail" ó��
		} catch(Exception e) {
			try {
				os.write("fail".getBytes());
				os.write("\n".getBytes());
			} catch(IOException e2) { }
		} finally{
			try { os.flush(); } catch (IOException e) { e.printStackTrace();}
		}

	}
	
	// Board��ü�� JSON��ü�� ��ȯ 
	private static JSONObject boardToJSON(Board board) {
		JSONObject boardJSON = new JSONObject();
		boardJSON.put("bno", board.getBno());
		boardJSON.put("btitle", board.getBtitle());
		boardJSON.put("bcontent", board.getBcontent());
		boardJSON.put("bhitcount", board.getBhitcount());
		boardJSON.put("bwriter", board.getBwriter());
		boardJSON.put("bwritedate", board.getBwritedate());
		boardJSON.put("barea", board.getBarea());
		boardJSON.put("bappointtime", board.getBappointtime());
		boardJSON.put("bappointday", board.getBappointday());
		boardJSON.put("bcommentcnt", board.getBcommentcnt());
		
		return boardJSON;
	}
}

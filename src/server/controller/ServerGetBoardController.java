package server.controller;

import java.io.*;

import org.json.JSONObject;

import server.jdbc.*;

// bno를 받아서 처리결과가 성공이면 Board를 보내기
public class ServerGetBoardController {
	public static void execute(InputStream is, OutputStream os) {
		try {
			// data : "bno" 받기
			byte[] datas = new byte[10];
			int data;
			int idx = 0;
			while( (data=is.read()) != '\n' ) {
				datas[idx++] = (byte)data;
			}

			String bnoStr = new String(datas, 0, idx);
			int bno = Integer.parseInt(bnoStr);
			
			// DB에서 bno로 Board를 받아오기
			BoardDao bDao = new BoardDao();
			Board board = bDao.selectByPk(bno);
			
			// 요청처리결과 보내기		
			//성공시 "success" 보내고 Board객체를 JSON객체로 보냄
			if(board != null) {
				os.write("success".getBytes());
				os.write("\n".getBytes());
				JSONObject boardJSON = boardToJSON(board);
				os.write(boardJSON.toString().getBytes());
				os.write("\n".getBytes());
			
			// 실패시
			} else {
				throw new IOException();
			}
		// "fail" 처리
		} catch(Exception e) {
			try {
				os.write("fail".getBytes());
				os.write("\n".getBytes());
			} catch(IOException e2) { }
		} finally{
			try { os.flush(); } catch (IOException e) { e.printStackTrace();}
		}

	}
	
	// Board객체를 JSON객체로 변환 
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

package server.jdbc;

import java.text.*;
import java.util.*;

public class BoardDaoExample {

	public static void main(String[] args) throws Exception {
		BoardDao boardDao = new BoardDao();
		Board board = new Board();
		//임시 데이터 생성
		for(int i=1;i<=100;i++) {
			board.setBtitle("제목"+i);
			board.setBcontent("내용"+i);
			board.setBwriter("글쓴이"+i);
			board.setBarea("장소"+i);			
			board.setBappointtime("12시"+i);
			board.setBappointday("2016-05-11");			
			boardDao.insert(board);
		}
		
		//첫 화면에서 일정 갯수만 갖고오고 페이지 이동 버튼을 만들어 다음, 이전 페이지 번호를 이용하여 
		//페이지의 번호의 갯수 만큼만 가져와 보여준다. 데이터들 다 가져오지 말고
		//x페이지 갖고 오기, 페이지당 y개
		/*List<Board> list = boardDao.getListByPage(2, 5);
		
		for(Board b : list) {
			System.out.println(
					b.getBno() + " | " + b.getBtitle() + " | " + b.getBwriter() + 
					" | " + b.getBhitcount() + " | " + b.getBdate()
					);
		}*/
		
		
		
		/*List<Board> list = new ArrayList<Board>();
		list=boardDao.selectAll();
		
		
		Integer size = 0;
		size = list.size();*/
		
	/*	os.write("success".getBytes());
		os.write("\n".getBytes());
		os.write(size.toString().getBytes());
		os.write("\n".getBytes());*/
		
		
		
		/*JSONObject data = new JSONObject();
		for(Board board : list) {
			data.put("bno", board.getBno());
			data.put("btitle", board.getBtitle());
			data.put("bcontent", board.getBcontent());
			data.put("bhitcount", board.getBhitcount());
			data.put("bwriter", board.getBwriter());
			data.put("bdate", board.getBdate());
			data.put("btotal", board.getBtotal());
			String message = data.toString();
			byte[] bytes = message.getBytes();
			//os.write(bytes);
			System.out.println(message+",");
			
		
		}
		System.out.println(size);*/
		

	/*	JSONArray dataArray = new JSONArray(list);

		System.out.println(dataArray);
		
		String message = dataArray.toString();
		JSONArray jArray = new JSONArray(message);
		
		
		for(int i=0; i<dataArray.length();i++) {
			System.out.println(jArray.getJSONObject(i));
		}*/
		
		
		
	}
 
}

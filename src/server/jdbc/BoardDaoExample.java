package server.jdbc;

import java.text.*;
import java.util.*;

public class BoardDaoExample {

	public static void main(String[] args) throws Exception {
		BoardDao boardDao = new BoardDao();
		Board board = new Board();
		//�ӽ� ������ ����
		for(int i=1;i<=100;i++) {
			board.setBtitle("����"+i);
			board.setBcontent("����"+i);
			board.setBwriter("�۾���"+i);
			board.setBarea("���"+i);			
			board.setBappointtime("12��"+i);
			board.setBappointday("2016-05-11");			
			boardDao.insert(board);
		}
		
		//ù ȭ�鿡�� ���� ������ ������� ������ �̵� ��ư�� ����� ����, ���� ������ ��ȣ�� �̿��Ͽ� 
		//�������� ��ȣ�� ���� ��ŭ�� ������ �����ش�. �����͵� �� �������� ����
		//x������ ���� ����, �������� y��
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

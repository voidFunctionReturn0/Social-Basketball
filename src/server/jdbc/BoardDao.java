package server.jdbc;

import java.sql.*;
import java.text.*;
import java.util.*;


public class BoardDao {
	public int insert(Board board) throws Exception{
		
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
		String sql = "insert into boards values(seq_boards_bno.nextval,?,?,0,?,?,?,?,?,0)";

		PreparedStatement ps = conn.prepareStatement(sql, new String[] {"bno"});
		ps.setString(1, board.getBtitle());
		ps.setString(2, board.getBcontent());
		ps.setString(3, board.getBwriter());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd  HH:mm");
		ps.setString(4, dateFormat.format(calendar.getTime()));
		ps.setString(5, board.getBarea());
		ps.setString(6, board.getBappointtime());
		ps.setString(7, board.getBappointday());

		ps.executeUpdate(); 
				
		int bno=0;
		ResultSet rs = ps.getGeneratedKeys();
		if(rs.next()) {
			bno = rs.getInt(1);
		}
		rs.close();
		ps.close();
		conn.close();
				
		return bno;
		
	}
	
	/*public List<Board> selectAll() throws Exception{
		//select * from phones
		//���� ����¡ �۾� ����(���� ������ŭ �޾ƿ� ����)
		List<Board> list = new ArrayList<Board>();
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
		String sql = "select * from boards";
		PreparedStatement ps = conn.prepareStatement(sql); //sql�� �����ϴ� ��ü
		ResultSet rs = ps.executeQuery(); //�����ü
		while(rs.next()){
			Board board = new Board();
			
			board.setBno(rs.getInt("bno"));
			board.setBtitle(rs.getString("btitle"));
			board.setBcontent(rs.getString("bcontent"));
			board.setBhitcount(rs.getInt("bhitcount"));
			board.setBwriter(rs.getString("bwriter"));
			board.setBwritedate(rs.getString("bwritedate"));
			board.setBarea(rs.getString("barea"));
			board.setBappointtime(rs.getString("bappointtime"));
			board.setBappointday(rs.getString("bappointday"));
			board.setBcommentcnt(rs.getInt("bcommentcnt"));
			list.add(board);
	
			
		}
		
		rs.close();
		ps.close();
		conn.close();
		return list;
	}*/
	
	
	
	
	
	
	
	//����¡ ó���Ͽ� �Խù� ���� ����pageNo : ������ ��ȣ, rowsPerPage : ������ �� �Խù� �� 
	public List<Board> getListByPage(int pageNo, int rowsPerPage) throws SQLException { 
		List<Board> list = new ArrayList<Board>();
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
		String sql = "select rownum, bno, btitle, bcontent, bhitcount, bwriter, bwritedate, barea, bappointtime, bappointday, bcommentcnt "; //�÷�����
		sql+= "from( ";
		sql+= "select rownum as rno, bno, btitle, bcontent, bhitcount, bwriter, bwritedate, barea, bappointtime, bappointday, bcommentcnt "; //�÷�����
		sql+= "from (select bno, btitle, bcontent, bhitcount, bwriter, bwritedate, barea, bappointtime, bappointday, bcommentcnt from boards order by bno desc) "; //�̰͸� �÷� �����Ͽ� ����.
		sql+= "where rownum<=?";
		sql+= ") ";
		sql+= "where rno>=?";
		//?�� ��� �Ϸ�� ���̿��� �Ѵ�. ��� ���߿��� �ǿ����ڷ� �Ұ���
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, pageNo*rowsPerPage);
		ps.setInt(2, (pageNo-1)*rowsPerPage+1);
		
		ResultSet rs = ps.executeQuery();//select��
		//�ִ� 10��
		while(rs.next()) {
			Board board = new Board();
			board.setBno(rs.getInt("bno"));
			board.setBtitle(rs.getString("btitle"));
			board.setBcontent(rs.getString("bcontent"));
			board.setBhitcount(rs.getInt("bhitcount"));
			board.setBwriter(rs.getString("bwriter"));
			board.setBwritedate(rs.getString("bwritedate"));
			board.setBarea(rs.getString("barea"));
			board.setBappointtime(rs.getString("bappointtime"));
			board.setBappointday(rs.getString("bappointday"));
			board.setBcommentcnt(rs.getInt("bcommentcnt"));
			list.add(board);
		}
		
		
		rs.close();
		ps.close();
		conn.close();
		
		
		
		return list;
	}
	
	
	//����¡ ó���Ͽ� �Խù� ���� ����pageNo : ������ ��ȣ, rowsPerPage : ������ �� �Խù� �� 
	public List<Board> getSearchListByPage(int pageNo, int rowsPerPage, String area) throws SQLException { 
		List<Board> list = new ArrayList<Board>();
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
		String sql = "select rownum, bno, btitle, bcontent, bhitcount, bwriter, bwritedate, barea, bappointtime, bappointday, bcommentcnt "; //�÷�����
		sql+= "from( ";
		sql+= "select rownum as rno, bno, btitle, bcontent, bhitcount, bwriter, bwritedate, barea, bappointtime, bappointday, bcommentcnt "; //�÷�����
		sql+= "from (select bno, btitle, bcontent, bhitcount, bwriter, bwritedate, barea, bappointtime, bappointday, bcommentcnt from boards where barea like ? order by bno asc) "; //�̰͸� �÷� �����Ͽ� ����.
		sql+= "where rownum<=?";
		sql+= ") ";
		sql+= "where rno>=?";
		//?�� ��� �Ϸ�� ���̿��� �Ѵ�. ��� ���߿��� �ǿ����ڷ� �Ұ���
		PreparedStatement ps = conn.prepareStatement(sql);
			
		ps.setString(1, '%'+area+'%');
		ps.setInt(2, pageNo*rowsPerPage);
		ps.setInt(3, (pageNo-1)*rowsPerPage+1);
			
		ResultSet rs = ps.executeQuery();//select��
		//�ִ� 10��
		while(rs.next()) {
			Board board = new Board();
			board.setBno(rs.getInt("bno"));
			board.setBtitle(rs.getString("btitle"));
			board.setBcontent(rs.getString("bcontent"));
			board.setBhitcount(rs.getInt("bhitcount"));
			board.setBwriter(rs.getString("bwriter"));
			board.setBwritedate(rs.getString("bwritedate"));
			board.setBarea(rs.getString("barea"));
			board.setBappointtime(rs.getString("bappointtime"));
			board.setBappointday(rs.getString("bappointday"));
			board.setBcommentcnt(rs.getInt("bcommentcnt"));
			list.add(board);
		}
		
			
		rs.close();
		ps.close();
		conn.close();
			
			
			
		return list;
	}

	//�� ���� ��
	public int count() throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(*) from boards";
		//String sql = "select count(*) as rows from boards";
		PreparedStatement ps = conn.prepareStatement(sql); 
		ResultSet rs = ps.executeQuery();
		rs.next();	//data�� �ִ� ������ �̵�
		int count = rs.getInt(1);
		//int rows = rs.getInt(rows); //as�� �÷��� �����ؼ� ����ص� �ȴ�.
		rs.close();
		ps.close();
		conn.close();
		
		return count;
	}
	
	// bno�� Board �޾ƿ���
	public Board selectByPk(int bno) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select * from boards where bno = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, bno);
		ResultSet rs = ps.executeQuery();
		Board board = null;
		if(rs.next()) {
			board = new Board();
			board.setBno(rs.getInt("bno"));
			board.setBtitle(rs.getString("btitle"));
			board.setBcontent(rs.getString("bcontent"));
			board.setBhitcount(rs.getInt("bhitcount"));
			board.setBwriter(rs.getString("bwriter"));
			board.setBwritedate(rs.getString("bwritedate"));
			board.setBarea(rs.getString("barea"));
			board.setBappointtime(rs.getString("bappointtime"));
			board.setBappointday(rs.getString("bappointday"));
			board.setBhitcount(rs.getInt("bcommentcnt"));
		}
		rs.close();
		ps.close();
		conn.close();
		return board;
	}

	public int update(Board board) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql= "UPDATE boards SET btitle=?, bcontent=?, barea=?, bappointtime=?, bappointday=? WHERE bno=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, board.getBtitle());
		ps.setString(2, board.getBcontent());
		ps.setString(3, board.getBarea());
		ps.setString(4, board.getBappointtime());
		ps.setString(5, board.getBappointday());
		ps.setString(6, Integer.toString(board.getBno()));
		int rows =ps.executeUpdate(); 
		ps.close();
		conn.close();
		return rows;
	}
	
	public int deleteByPk(int bno) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "delete from boards where bno=?";
		PreparedStatement ps =conn.prepareStatement(sql);
		ps.setInt(1, bno);
		int rows = ps.executeUpdate();
		ps.close();
		conn.close(); //
		return rows;
	}
	
	
}

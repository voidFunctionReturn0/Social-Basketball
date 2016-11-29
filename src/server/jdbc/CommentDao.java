package server.jdbc;

import java.sql.*;
import java.text.*;
import java.util.*;


public class CommentDao {
	public int insert(Comment comment) throws Exception{
		
		Connection conn = ConnectionProvider.getConnection(); //연결객체
		String sql = "insert into comments values(seq_comments_cno.nextval,?,?,?,?)";

		PreparedStatement ps = conn.prepareStatement(sql, new String[] {"cno"});
		ps.setString(1, comment.getCwriter());
		ps.setString(2, comment.getCcontent());
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd  HH:mm");
		ps.setString(3, dateFormat.format(calendar.getTime()));
		ps.setInt(4, comment.getBno());

		ps.executeUpdate(); 
				
		int cno=0;
		ResultSet rs = ps.getGeneratedKeys();
		if(rs.next()) {
			cno = rs.getInt(1);
		}
		rs.close();
		ps.close();
		conn.close();
				
		return cno;
		
	}
	
	
	//bno를 통한 comments리스트 가져오기
	public List<Comment> selectAll(int bno) throws Exception{
		List<Comment> list = new ArrayList<Comment>();
		Connection conn = ConnectionProvider.getConnection(); //연결객체
		String sql = "select * from comments where bno=? order by cno desc";
		PreparedStatement ps = conn.prepareStatement(sql); //sql을 실행하는 객체
		ps.setInt(1, bno);
		ResultSet rs = ps.executeQuery(); //결과객체
		while(rs.next()){
			Comment comment = new Comment();
			
			comment.setCno(rs.getInt("cno"));
			comment.setCwriter(rs.getString("cwriter"));
			comment.setCcontent(rs.getString("ccontent"));
			comment.setCwritedate(rs.getString("cwritedate"));
			comment.setBno(rs.getInt("bno"));
			list.add(comment);			
		}
		
		rs.close();
		ps.close();
		conn.close();
		return list;
	}
	
	
	
	
	
	
	
	//페이징 처리하여 게시물 갖고 오기pageNo : 페이지 번호, rowsPerPage : 페이지 당 게시물 수 
	public List<Comment> getListByPage(int pageNo, int rowsPerPage) throws SQLException { 
		List<Comment> list = new ArrayList<Comment>();
		Connection conn = ConnectionProvider.getConnection(); //연결객체
		String sql = "select rownum, cno, cwriter, ccontent, cwritedate, bno "; //컬럼변경
		sql+= "from( ";
		sql+= "select rownum as rno, cno, cwriter, ccontent, cwritedate, bno "; //컬럼변경
		sql+= "from (select cno, cwriter, ccontent, cwritedate, bno from comments order by cno desc) "; //이것만 컬럼 변경하여 응용.
		sql+= "where rownum<=?";
		sql+= ") ";
		sql+= "where rno>=?";
		//?는 계산 완료된 값이여야 한다. 계산 도중에는 피연산자로 불가능
		PreparedStatement ps = conn.prepareStatement(sql);
		
		ps.setInt(1, pageNo*rowsPerPage);
		ps.setInt(2, (pageNo-1)*rowsPerPage+1);
		
		ResultSet rs = ps.executeQuery();//select문
		//최대 10개
		while(rs.next()) {
			Comment comment = new Comment();
			comment.setCno(rs.getInt("cno"));
			comment.setCwriter(rs.getString("cwriter"));
			comment.setCcontent(rs.getString("ccontent"));
			comment.setCwritedate(rs.getString("cwritedate"));
			comment.setBno(rs.getInt("bno"));
			list.add(comment);
		}
		
		
		rs.close();
		ps.close();
		conn.close();
		
		
		
		return list;
	}
	
	//총 행의 수
	public int count() throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(*) from comments";
		//String sql = "select count(*) as rows from boards";
		PreparedStatement ps = conn.prepareStatement(sql); 
		ResultSet rs = ps.executeQuery();
		rs.next();	//data가 있는 행으로 이동
		int count = rs.getInt(1);
		//int rows = rs.getInt(rows); //as로 컬럼명 정의해서 사용해도 된다.
		rs.close();
		ps.close();
		conn.close();
		
		return count;
	}
	
	// cno로 Comments 받아오기 필요없을듯
	public Comment selectByPk(int cno) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select * from comments where cno = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, cno);
		ResultSet rs = ps.executeQuery();
		Comment comment = null;
		if(rs.next()) {
			comment = new Comment();
			comment.setCno(rs.getInt("cno"));
			comment.setCwriter(rs.getString("cwriter"));
			comment.setCcontent(rs.getString("ccontent"));
			comment.setCwritedate(rs.getString("cwritedate"));
			comment.setBno(rs.getInt("bno"));
		}
		rs.close();
		ps.close();
		conn.close();
		return comment;
	}

	public int update(Comment comment) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql= "UPDATE comments SET ccontent=? WHERE cno=?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, comment.getCcontent());
		ps.setInt(2, comment.getCno());

		int rows =ps.executeUpdate(); 
		ps.close();
		conn.close();
		return rows;
	}
	
	public int deleteByPk(int cno) throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "delete from comments where cno=?";
		PreparedStatement ps =conn.prepareStatement(sql);
		ps.setInt(1, cno);
		int rows = ps.executeUpdate();
		ps.close();
		conn.close(); //
		return rows;
	}
	
	
}

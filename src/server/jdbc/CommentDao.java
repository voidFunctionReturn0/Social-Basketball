package server.jdbc;

import java.sql.*;
import java.text.*;
import java.util.*;


public class CommentDao {
	public int insert(Comment comment) throws Exception{
		
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
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
	
	
	//bno�� ���� comments����Ʈ ��������
	public List<Comment> selectAll(int bno) throws Exception{
		List<Comment> list = new ArrayList<Comment>();
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
		String sql = "select * from comments where bno=? order by cno desc";
		PreparedStatement ps = conn.prepareStatement(sql); //sql�� �����ϴ� ��ü
		ps.setInt(1, bno);
		ResultSet rs = ps.executeQuery(); //�����ü
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
	
	
	
	
	
	
	
	//����¡ ó���Ͽ� �Խù� ���� ����pageNo : ������ ��ȣ, rowsPerPage : ������ �� �Խù� �� 
	public List<Comment> getListByPage(int pageNo, int rowsPerPage) throws SQLException { 
		List<Comment> list = new ArrayList<Comment>();
		Connection conn = ConnectionProvider.getConnection(); //���ᰴü
		String sql = "select rownum, cno, cwriter, ccontent, cwritedate, bno "; //�÷�����
		sql+= "from( ";
		sql+= "select rownum as rno, cno, cwriter, ccontent, cwritedate, bno "; //�÷�����
		sql+= "from (select cno, cwriter, ccontent, cwritedate, bno from comments order by cno desc) "; //�̰͸� �÷� �����Ͽ� ����.
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
	
	//�� ���� ��
	public int count() throws SQLException {
		Connection conn = ConnectionProvider.getConnection();
		String sql = "select count(*) from comments";
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
	
	// cno�� Comments �޾ƿ��� �ʿ������
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

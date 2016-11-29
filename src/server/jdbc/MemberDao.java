package server.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MemberDao {
	public boolean insert(Member member) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// kosa2team1 DB�� ����
			conn = ConnectionProvider.getConnection();
			
			String sql = "insert into members values(?,?,?,?,?,?,?,?)";	
			ps = conn.prepareStatement(sql);
			
			// member�� �ʵ带 members���̺� �����ϱ� ���� �غ�
			ps.setString(1, member.getMid());
			ps.setString(2, member.getMpassword());
			ps.setString(3, member.getMheight());
			ps.setString(4, member.getMsex());
			ps.setString(5, member.getMposition());
			ps.setString(6, member.getMarea());
			ps.setString(7, member.getMlevel());
			ps.setString(8, member.getMimage());
			
			// ���̺� sql���� ����
			ps.executeUpdate();
			return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}

	// id�� �����ͺ��̽��� �����ü ã�Ƽ� ��ȯ
	public Member selectByMid(String mid) {
		Member member = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = ConnectionProvider.getConnection();
			
			String sql = "select * from members where mid = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, mid);
			rs = ps.executeQuery();// ���� �޾ƿü� �ִ�.
			
			if(rs.next()) {
				member = new Member();
				member.setMid(rs.getString("mid"));
				member.setMpassword(rs.getString("mpassword"));
				member.setMheight(rs.getString("mheight"));
				member.setMsex(rs.getString("msex"));
				member.setMposition(rs.getString("mposition"));
				member.setMarea(rs.getString("marea"));
				member.setMlevel(rs.getString("mlevel"));
				member.setMimage(rs.getString("mimage"));
			} 
			return member;

			
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			return null;
		} finally {
			try {
				rs.close();
				ps.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}
	}
	
	public boolean isNotMid(String mid){
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			conn = ConnectionProvider.getConnection();
			String sql = "select mid from members where mid = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, mid);
			rs = ps.executeQuery();// ���� �޾ƿü� �ִ�.
			
			if(rs.next()) { //���� ������ �ߺ����� �ִٴ� ��
				return false;
			} else {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return true;
		} finally {
			try {
				rs.close();
				ps.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
	
		}
	}
	
	// Member ������ ����
	public boolean update(Member member) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			// kosa2team1 DB�� ����
			conn = ConnectionProvider.getConnection();
			
			String sql = "update members "
					+ "set mpassword=?,mheight=?,msex=?,mposition=?,marea=?,mlevel=?,mimage=? "
					+ "where mid = ?";	
			ps = conn.prepareStatement(sql);
			
			// member�� �ʵ带 members���̺� ������Ʈ�ϱ� ���� �غ�
			ps.setString(1, member.getMpassword());
			ps.setString(2, member.getMheight());
			ps.setString(3, member.getMsex());
			ps.setString(4, member.getMposition());
			ps.setString(5, member.getMarea());
			ps.setString(6, member.getMlevel());
			ps.setString(7, member.getMimage());
			
			ps.setString(8, member.getMid());
			
			// ���̺� sql���� ����
			ps.executeQuery();		
			return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				ps.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
			}
		}	
	}
}

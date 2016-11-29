package server.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@106.253.56.123:1521:orcl", "kosa2team1", "kosa2team1");
		
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("JDBC ����̹��� �������� �ʽ��ϴ�.");
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("���������� ���� �ʽ��ϴ�." + e.getMessage());
		}
		
		return conn;
	}

}

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
			System.out.println("JDBC 드라이버가 존재하지 않습니다.");
		} catch(SQLException e) {
			e.printStackTrace();
			System.out.println("연결정보가 맞지 않습니다." + e.getMessage());
		}
		
		return conn;
	}

}

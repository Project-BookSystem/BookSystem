
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String Driver = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://192.168.0.94:3306/booksystem"; // DB 주소
	private static final String USER = "Book"; // MySQL 사용자명
	private static final String PASSWORD = "0212"; // MySQL 비밀번호
	
	//a
//	public static Connection getConnection() {
//		Connection conn = null;
//		try {
//			Class.forName("com.mysql.cj.jdbc.Driver");
//			conn = DriverManager.getConnection(URL, USER, PASSWORD);
//		} catch (ClassNotFoundException | SQLException e) {
//			e.printStackTrace();
//		}
//		return conn;
//	}
	
	public static Connection conn = null;
	
	public static void setConnection() {

		try {
			Class.forName(Driver);
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
			System.out.println("DB Connection [성공]\n");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("DB Connection [실패]\n");
			e.printStackTrace();
		}
	}
	
	public static void disConnnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
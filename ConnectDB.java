import java.sql.*;
public class ConnectDB {

	public Connection connection(){
		Connection con = null;
		try {
			String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			Class.forName(driver);
			String url = "jdbc:sqlserver://localhost:1433;databasename=DATN";
			String user = "sa";
			String pass = "123456";
			con = DriverManager.getConnection(url, user, pass);
			
			System.out.println("ket noi thanh cong");
		} catch (ClassNotFoundException e) {
			System.err.println("Khong tim thay driver");

		} catch (SQLException exx) {
			System.err.println("Khong the ket noi");
		}
		return con;
	}

	public static void main(String[] args) {
		ConnectDB db = new ConnectDB();
		db.connection();
	}
}

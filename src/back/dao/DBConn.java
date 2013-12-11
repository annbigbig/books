package back.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConn {
	private static Connection conn = null;
	public static Connection getConn(){
		try {
			Class.forName("com.mysql.jdbc.Driver");//加載數據庫連接驅動
			String user="root";//用戶名
			String pwd="mynameistony";//密碼
			String url="jdbc:mysql://localhost:3306/bookstore?useUnicode=true&characterEncoding=utf8";//連接URL
			conn=DriverManager.getConnection(url, user, pwd);//獲取連接
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}

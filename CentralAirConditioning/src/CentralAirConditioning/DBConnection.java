package CentralAirConditioning;

import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Connection;

public class DBConnection {
	private Connection conn;
	private static DBConnection dbConnection = null;
	
	private DBConnection() {}
	
	public static DBConnection getInstance() {
		if (dbConnection == null)
			dbConnection = new DBConnection();
		return dbConnection;
	}
	
	public boolean createConnection() {
        try {
        	// MySQL的JDBC URL编写方式：jdbc:mysql://主机名称：连接端口/数据库的名称?参数=值
            // 避免中文乱码要指定useUnicode和characterEncoding
            // 执行数据库操作之前要在数据库管理系统上创建一个数据库，名字自己定，
            Class.forName("com.mysql.jdbc.Driver");// 动态加载mysql驱动
            
            System.out.println("成功加载MySQL驱动程序");
            
            conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/airConditioning?"
                    + "useUnicode=true&characterEncoding=utf-8&useSSL=false","root","0724");

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
	}
	
	public Connection getConnection() {
 		return conn;
	}
	
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

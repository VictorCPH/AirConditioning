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
        	// MySQL��JDBC URL��д��ʽ��jdbc:mysql://�������ƣ����Ӷ˿�/���ݿ������?����=ֵ
            // ������������Ҫָ��useUnicode��characterEncoding
            // ִ�����ݿ����֮ǰҪ�����ݿ����ϵͳ�ϴ���һ�����ݿ⣬�����Լ�����
            Class.forName("com.mysql.jdbc.Driver");// ��̬����mysql����
            
            System.out.println("�ɹ�����MySQL��������");
            
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

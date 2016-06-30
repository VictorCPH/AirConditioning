package CentralAirConditioning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class ManagerMapper implements IMapper{

	@Override
	public boolean insert(Object object) {
		// TODO Auto-generated method stub
		Manager manager = (Manager)object;

		String sql = "insert into manager(mName, mPassword) values (";
		sql += "'" + manager.getName() + "',";
		sql += "'" + manager.getPassword() + "')";
		
		Connection conn = DBConnection.getInstance().getConnection();
		try {
			Statement stat = (Statement) conn.createStatement();
			int num = stat.executeUpdate(sql);
			if (num == 1) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Object get(String condition) {
		// TODO Auto-generated method stub
		String sql = "select * from manager where mName = " + "'" + condition + "'";
		
		Connection conn = DBConnection.getInstance().getConnection();
		try {
			Statement stat = (Statement) conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
    
            rs.next();
            Manager manager = new Manager(rs.getString(1), rs.getString(2));
            return manager;
   
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return new Manager(null, null);
	}

	@Override
	public LinkedList<?> gets(Timestamp startTime, Timestamp endTime, int roomNumber) {//不用实现，用不到
		// TODO Auto-generated method stub
		return null;
	}

}

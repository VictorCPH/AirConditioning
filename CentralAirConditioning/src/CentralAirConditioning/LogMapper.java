package CentralAirConditioning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class LogMapper implements IMapper {

	@Override
	public boolean insert(Object object) {
		// TODO Auto-generated method stub
		Log log = (Log)object;

		String sql = "insert into log(roomNumber, eventTime, clientEvent) values (";
		sql += log.getRoomNumber() + ",";
		sql += "'" + log.getTime() + "',";
		sql += "'" + log.getEvent() + "')";
		
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
	public Object get(String condition) {//不用实现，用不到
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedList<?> gets(Timestamp startTime, Timestamp endTime, int roomNumber) {
		// TODO Auto-generated method stub
		LinkedList<Log> logs = new LinkedList<Log>();
		
		String sql = "select * from log where eventTime < " 
					+ "'" + endTime + "'" + " and " + "roomNumber = " + roomNumber;
		
		Connection conn = DBConnection.getInstance().getConnection();
		try {
			Statement stat = (Statement) conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
    
            while (rs.next()) {
                Log log = new Log(rs.getInt(1), rs.getTimestamp(2), rs.getString(3));
                logs.add(log);
            }
   
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return logs;
	}



}

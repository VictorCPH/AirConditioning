package CentralAirConditioning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

public class FeeMapper implements IMapper {

	@Override
	public boolean insert(Object object) {
		// TODO Auto-generated method stub
		Fee fee = (Fee)object;
		
		String sql = "insert into fee(roomNumber, startTime, endTime, startTemperature,"
				+ "endTemperature, wind, consumption, fee) values (";
		sql += fee.getRoomNumber() + ",";
		sql += "'" + fee.getStartTime() + "',";
		sql += "'" + fee.getEndTime() + "',";
		sql += fee.getStartTempterature() + ",";
		sql += fee.getEndTempterature() + ",";
		sql += fee.getWind() + ",";
		sql += fee.getConsumption() + ",";
		sql += fee.getFee() + ")";
		
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
		LinkedList<Fee> fees = new LinkedList<Fee>();
		
		String sql = "select * from fee where (endTime between " + "'" + startTime + "'" 
				+ " and " + "'" + endTime + "')" + " and " + "roomNumber = " + roomNumber;
		
		Connection conn = DBConnection.getInstance().getConnection();
		try {
			Statement stat = (Statement) conn.createStatement();

            ResultSet rs = stat.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值
    
            while (rs.next()) {
                Fee fee = new Fee(rs.getInt(1), rs.getTimestamp(2), rs.getTimestamp(3), 
                		rs.getDouble(4), rs.getDouble(5), rs.getInt(6), rs.getDouble(7), rs.getDouble(8));
                fees.add(fee);
            }
   
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return fees;
	}



}
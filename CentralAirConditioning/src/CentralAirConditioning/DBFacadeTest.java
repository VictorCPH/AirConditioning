package CentralAirConditioning;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.Test;

public class DBFacadeTest {

	@Test
	public void test() {
		DBFacade dBFacade = DBFacade.getInstance();
		DBConnection dbConnection = DBConnection.getInstance();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		if (dbConnection.createConnection() == true) {
			dBFacade.insert(new Fee(2001, timestamp, timestamp, 21.1, 22.3, 1, 0.0, 0.0), Fee.class);
			dBFacade.insert(new Log(2001, timestamp, "¿ª»ú"), Log.class);
			//dBFacade.insert(new Manager("root", "123456"), Manager.class);
			
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -5);
			Timestamp startTime = new Timestamp(calendar.getTimeInMillis());
			
			
			Manager manager = (Manager) dBFacade.get("root", Manager.class);
			System.out.println(manager.toString());
			LinkedList<Fee> fees = (LinkedList<Fee>) dBFacade.gets(startTime, timestamp, 2001, Fee.class);
			for (Fee fee : fees) {
				System.out.println(fee.toString());
			}
			LinkedList<Log> logs = (LinkedList<Log>) dBFacade.gets(null, new Timestamp(System.currentTimeMillis()), 2001, Log.class);
			for (Log log : logs) {
				System.out.println(log.toString());
			}
		}
	}

	public static void main(String[] args) {
		new DBFacadeTest().test();
	}
}

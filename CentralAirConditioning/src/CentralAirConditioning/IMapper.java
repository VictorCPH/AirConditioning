package CentralAirConditioning;

import java.sql.Timestamp;
import java.util.LinkedList;

public interface IMapper {

	public boolean insert(Object object);

	public Object get(String condition);

	public LinkedList<?> gets(Timestamp startTime, Timestamp endTime, int roomNumber);
	
}

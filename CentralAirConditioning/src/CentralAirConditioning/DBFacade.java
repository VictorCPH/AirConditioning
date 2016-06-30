package CentralAirConditioning;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;

public class DBFacade {//µ¥Àý
	@SuppressWarnings("rawtypes")
	private HashMap<Class, Class> mapperContainer;
	private static DBFacade dbFacade = null;
	
	public static DBFacade getInstance() {
		if (dbFacade == null) 
			dbFacade = new DBFacade();
		return dbFacade;
	}
	
	@SuppressWarnings("rawtypes")
	private DBFacade() {
		mapperContainer = new HashMap<Class, Class>();
		mapperContainer.put(Manager.class, ManagerMapper.class);
		mapperContainer.put(Fee.class, FeeMapper.class);
		mapperContainer.put(Log.class, LogMapper.class);
	}
	
	@SuppressWarnings("rawtypes")
	public IMapper getMapper( Class entityClass) {
		IMapper mapper = null;	
		Class mapperClass = mapperContainer.get(entityClass);
		
		if (mapperClass != null) {
			try {
				mapper = (IMapper)mapperClass.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mapper;
	}
	
	@SuppressWarnings("rawtypes")
	public Object get(String condition, Class entityClass) {
		IMapper mapper = getMapper(entityClass);
		
		if (mapper != null)
			return mapper.get(condition);
		else 
			return new Object();		
	}
	
	@SuppressWarnings("rawtypes")
	public boolean insert(Object object, Class entityClass) {
		IMapper mapper = getMapper(entityClass);
		
		if (mapper != null)
			return mapper.insert(object);
		else 
			return false;
	}
	
	@SuppressWarnings("rawtypes")
	public LinkedList<?> gets(Timestamp startTime, Timestamp endTime, int roomNumber, Class entityClass) {
		IMapper mapper = getMapper(entityClass);
		
		if (mapper != null)
			return mapper.gets(startTime, endTime, roomNumber);
		else 
			return null;
	}
}

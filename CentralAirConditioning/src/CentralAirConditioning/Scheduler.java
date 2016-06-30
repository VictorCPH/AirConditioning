package CentralAirConditioning;

import java.sql.Timestamp;
import java.util.LinkedList;


public class Scheduler {
	private Server server;
	private LinkedList<Request> executeList;
	private LinkedList<Request> waitList;
	
	public Scheduler(Server server) {
		// TODO Auto-generated constructor stub
		executeList = new LinkedList<Request>();
		waitList = new LinkedList<Request>();
		this.server = server;
	}
	
	public synchronized void addRequest(Request request) {//添加一条合法的请求到等待链，等待链中同一房间的请求最多存在一个
		waitList.add(request);
		server.getRoomlist().setOneRoomState(request.getRoomNumber(), State.WAITING);
		DBFacade.getInstance().insert(new Log(request.getRoomNumber(), new Timestamp(System.currentTimeMillis()), 
				"温度调节请求被放入等待队列,目标温度: " + request.getGoalTemperature() + ",风速: " + request.getWind()), Log.class);
	}
	
	public synchronized void startOneRequest() {
		System.out.println("startOneResquest");
		Request request = waitList.getFirst();
		request.setStartTime(new Timestamp(System.currentTimeMillis()));
		request.setStartTemperature(server.getRoomlist().
				getOneRoomCurTemperature(request.getRoomNumber()));
		
		server.getRoomlist().setOneRoomState(request.getRoomNumber(), State.RUNNING);
		server.getRoomlist().setOneRoomWind(request.getRoomNumber(), request.getWind());
		
		ClientSocketThread clientSocketThread = ServerSocketThread.getInstance().getOneRoomSocket(request.getRoomNumber());
		
		clientSocketThread.sendStartWind();
		System.out.println("*******************");
		
		
		executeList.add(request);
		waitList.removeFirst();
		DBFacade.getInstance().insert(new Log(request.getRoomNumber(), new Timestamp(System.currentTimeMillis()), 
				"温度调节请求开始被响应,目标温度: " + request.getGoalTemperature() + ",风速: " + request.getWind()), Log.class);
	}

	public LinkedList<Request> getExecuteList() {
		return executeList;
	}

	public LinkedList<Request> getWaitList() {
		return waitList;
	}
	
	public boolean isValid(Request request) {
		if (request.getGoalTemperature() >= server.getLowTemperature()
			&& request.getGoalTemperature() <= server.getUpTemperature())
			return true;
		else
			return false;
	}
	
	public boolean isServer(int roomNumber) {
		for (Request request : executeList) {
			if (request.getRoomNumber() == roomNumber)
				return true;
		}
		return false;
	}
	
	public synchronized void removeExecutelistRequest(int roomNumber) {
		int i = 0;
		for (Request request : executeList) {
			if (request.getRoomNumber() == roomNumber) {
				double endTemperture = server.getRoomlist().getOneRoomCurTemperature(request.getRoomNumber()); //记录结束时的温度
				Timestamp endTime = new Timestamp(System.currentTimeMillis());//记录结束时的时间
				long interval = endTime.getTime() - request.getStartTime().getTime();
				double oneConsumption = calculateConsumption(interval, request.getWind());
				double oneFee = calculateFee(interval, request.getWind());//计算费用
				
				int wind;
				if (request.getWind() == Wind.LOW)
					wind = 0;
				else if (request.getWind() == Wind.MEDIUM)
					wind = 1;
				else 
					wind = 2;
							
				Fee fee = new Fee(request.getRoomNumber(), request.getStartTime(), 
						endTime, request.getStartTemperature(), endTemperture, wind, oneConsumption, oneFee);
		
				DBFacade.getInstance().insert(fee, Fee.class);
				
				server.getRoomlist().setOneRoomState(request.getRoomNumber(), State.STANDBY);
				executeList.remove(i);
				DBFacade.getInstance().insert(new Log(request.getRoomNumber(), new Timestamp(System.currentTimeMillis()), 
						"温度调节请求响应结束,目标温度: " + request.getGoalTemperature() + ",风速: " + request.getWind()), Log.class);
				break;
			}
			++i;
		}
	}

	public synchronized void removeWaitlistRequest(int roomNumber) {
		int i = 0;
		for (Request request : waitList) {
			if (request.getRoomNumber() == roomNumber) {
				server.getRoomlist().setOneRoomState(request.getRoomNumber(), State.STANDBY);
				waitList.remove(i);
				DBFacade.getInstance().insert(new Log(request.getRoomNumber(), new Timestamp(System.currentTimeMillis()), 
						"温度调节请求从等待队列中移除,目标温度: " + request.getGoalTemperature() + ",风速: " + request.getWind()), Log.class);
				break;
			}
			++i;
		}
	}
	
	public double calculateFee(long interval, Wind wind) {//时间间隔毫秒计算
	
		return calculateConsumption(interval, wind) * server.getChargeRate();		 
	}
	
	public double calculateConsumption(long interval, Wind wind) {
		double minute = interval/(1000);
		
		if (wind == Wind.LOW) {
			return 0.8 * minute;
		}
		else if (wind == Wind.MEDIUM) {
			return 1.0 * minute;
		}
		else {
			return 1.3 * minute;
		}
	}
	
	public synchronized void running() {
		while (executeList.size() < server.getMaxLoad() && waitList.size() > 0) {
			startOneRequest();		
		}
	}
	
	public Request getExecuteRequest(int roomNumber) {
		for (Request request : executeList) {
			if (request.getRoomNumber() == roomNumber) {
				return request;
			}
		}
		return null;
	}
	

}
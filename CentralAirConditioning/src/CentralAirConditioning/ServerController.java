package CentralAirConditioning;

import javafx.application.Platform;

import java.sql.Timestamp;
import java.util.LinkedList;

public class ServerController {
	private Server server;
	private Scheduler scheduler;
	private ServerSocketThread serverSocketThread;
	
	public ServerController() {
		server = new Server();
		scheduler = new Scheduler(server);
		serverSocketThread = ServerSocketThread.getInstance();
	}
	
	public void setAttributes(Mode mode, int frequency, 
			int maxLoad, Strategy strategy, double chargeRate) {
		server.setMode(mode);

		LinkedList<ClientSocketThread> clientSocketThreads = serverSocketThread.getSockets();
			
		for (ClientSocketThread clientSocketThread : clientSocketThreads) {
			if (clientSocketThread.isLink()) {
				clientSocketThread.sendMode(mode);
			}
		}
		
		server.setFrequency(frequency);
		server.setMaxLoad(maxLoad);
		server.setStrategy(strategy);
		server.setChargeRate(chargeRate);
	}
	
	
	public Server getServer() {
		return server;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public Mode getMode() {
		return server.getMode();
	}

	public int getFrequency() {
		return server.getFrequency();
	}

	public int getMaxLoad() {
		return server.getMaxLoad();
	}

	public Strategy getStrategy() {
		return server.getStrategy();
	}

	public double getChargeRate() {
		return server.getChargeRate();
	}

	public double getLowTemperature() {
		return server.getLowTemperature();
	}

	public double getUpTemperature() {
		return server.getUpTemperature();
	}

	public RoomList getRoomlist() {
		return server.getRoomlist();
	}
	
	public LinkedList<Request> getExecuteList() {
		return scheduler.getExecuteList();
	}

	public LinkedList<Request> getWaitList() {
		return scheduler.getWaitList();
	}
	
	/*
	*返回一段时间内某个房间的费用链表
	*每个节点：房间号，开始时间，结束时间，开始温度，结束温度，风速(0表示低速，1表示中速，2表示高速)，能耗，费用
	*/
	public LinkedList<Fee> getReport(Timestamp startTime, Timestamp endTime, int roomNumber) {
		
		return (LinkedList<Fee>) DBFacade.getInstance().gets(startTime, endTime, roomNumber,Fee.class);
	}
	
	@SuppressWarnings("unchecked")
	/*
	*返回一段时间内某个房间的日志信息（所有发生的事件）
	*/
	public LinkedList<Log> getLogs(Timestamp startTime, Timestamp endTime, int roomNumber) {
		
		return (LinkedList<Log>) DBFacade.getInstance().gets(startTime, endTime, roomNumber, Log.class);
	}
	
	/*
	 * 返回一段时间内某个房间的开机次数
	 */
	@SuppressWarnings("unchecked")
	public int getStartUpTimes(Timestamp startTime, Timestamp endTime, int roomNumber) {
		LinkedList<Log> logs = (LinkedList<Log>) DBFacade.getInstance().gets(startTime, endTime, roomNumber, Log.class);
		
		int times = 0;
		for (Log log : logs) {
			if (log.getEvent().equals("开机")) {
				times++;
			}
		}
		return times;
	}
	
	public void timeUpdate(MainWindowUI mainWindowUI)
	{
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException exc) {
					throw new Error("Unexpected interruption", exc);
				}
				Platform.runLater(() -> mainWindowUI.updateTime());
			}
		}).start();
	}
	public void timeUpdate_m(MainWindowUI mainWindowUI)
	{
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException exc) {
					throw new Error("Unexpected interruption", exc);
				}
				Platform.runLater(() -> mainWindowUI.updateDate_m());
				Platform.runLater(() -> mainWindowUI.updateDate_d());
				Platform.runLater(() -> mainWindowUI.updateDate_w());
			}
		}).start();
	}
	
	public void timeObsever(MainWindowUI mainWindowUI)
	{
		ServerSocketThread serverSocketThread = ServerSocketThread.getInstance();
		serverSocketThread.setScheduler(getScheduler());
		serverSocketThread.setServer(getServer());
		
		new Thread(serverSocketThread).start();//启动服务器socket,实时监控从机的连入
		
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(getFrequency() * 1000);
				} catch (InterruptedException exc) {
					throw new Error("Unexpected interruption", exc);
				}
				LinkedList<ClientSocketThread> clientSocketThreads = serverSocketThread.getSockets();
				
				for (ClientSocketThread clientSocketThread : clientSocketThreads) {
					if (clientSocketThread.isLink()) {
						clientSocketThread.sendApplyTemperature();//发送温度查询请求，更新温度
						clientSocketThread.sendConsumptionAndFee();//发送一次费用信息，更新费用
					}
					
				}
				
				System.out.println("房间" + getServer().getRoomlist());//显示房间状态
				System.out.println("运行" + getScheduler().getExecuteList());//显示运行的请求
				System.out.println("等待" + getScheduler().getWaitList());//显示等待的请求
				Platform.runLater(() -> mainWindowUI.update());
			}
		}).start();
	}
}


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
	*����һ��ʱ����ĳ������ķ�������
	*ÿ���ڵ㣺����ţ���ʼʱ�䣬����ʱ�䣬��ʼ�¶ȣ������¶ȣ�����(0��ʾ���٣�1��ʾ���٣�2��ʾ����)���ܺģ�����
	*/
	public LinkedList<Fee> getReport(Timestamp startTime, Timestamp endTime, int roomNumber) {
		
		return (LinkedList<Fee>) DBFacade.getInstance().gets(startTime, endTime, roomNumber,Fee.class);
	}
	
	@SuppressWarnings("unchecked")
	/*
	*����һ��ʱ����ĳ���������־��Ϣ�����з������¼���
	*/
	public LinkedList<Log> getLogs(Timestamp startTime, Timestamp endTime, int roomNumber) {
		
		return (LinkedList<Log>) DBFacade.getInstance().gets(startTime, endTime, roomNumber, Log.class);
	}
	
	/*
	 * ����һ��ʱ����ĳ������Ŀ�������
	 */
	@SuppressWarnings("unchecked")
	public int getStartUpTimes(Timestamp startTime, Timestamp endTime, int roomNumber) {
		LinkedList<Log> logs = (LinkedList<Log>) DBFacade.getInstance().gets(startTime, endTime, roomNumber, Log.class);
		
		int times = 0;
		for (Log log : logs) {
			if (log.getEvent().equals("����")) {
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
		
		new Thread(serverSocketThread).start();//����������socket,ʵʱ��شӻ�������
		
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
						clientSocketThread.sendApplyTemperature();//�����¶Ȳ�ѯ���󣬸����¶�
						clientSocketThread.sendConsumptionAndFee();//����һ�η�����Ϣ�����·���
					}
					
				}
				
				System.out.println("����" + getServer().getRoomlist());//��ʾ����״̬
				System.out.println("����" + getScheduler().getExecuteList());//��ʾ���е�����
				System.out.println("�ȴ�" + getScheduler().getWaitList());//��ʾ�ȴ�������
				Platform.runLater(() -> mainWindowUI.update());
			}
		}).start();
	}
}


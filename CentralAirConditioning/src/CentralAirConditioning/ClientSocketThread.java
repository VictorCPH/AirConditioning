package CentralAirConditioning;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Scanner;
import java.util.concurrent.locks.Lock;


public class ClientSocketThread implements Runnable {
	private int roomNumber;
	private Scheduler scheduler;
	private Server server;
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	private boolean isLink;
	
	public ClientSocketThread(Socket incoming, Scheduler scheduler, Server server) {
		// TODO Auto-generated constructor stub
		this.scheduler = scheduler;
		this.server = server;
		this.socket = incoming;
		this.isLink = true;
		
		try {	
			InputStream inStream = incoming.getInputStream();
			OutputStream outStream = incoming.getOutputStream();
			
			in = new Scanner(inStream);
			out = new PrintWriter(outStream, true);	
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}

	public Scanner getIn() {
		return in;
	}

	public PrintWriter getOut() {
		return out;
	}
	
	public synchronized void sendMode(Mode mode) {//消息号2
		int tmpMode;
		if (mode == Mode.COOL)
			tmpMode = 0;
		else 
			tmpMode = 1;
			
		out.print("2 " + tmpMode + " ");
		out.flush();
	
		if(out.checkError()) {
			isLink = false;
		}
		
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "上线后主机返回模式信息");
	}
	
	//“0”合法请求 “1”非法请求
	public synchronized void replyIsValid(int isValid) {//消息号4
		
		out.print("4 " + isValid + " ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		if (isValid == 0)
			System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "调节温度请求回应:合法");		
		else
			System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "调节温度请求回应:非法");	
	}
	
	public synchronized void sendStartWind() {//消息号6
		out.print("6 ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "主机送风信息");	
	}
	
	public synchronized void sendStopWind(int reason) {//消息号7
		out.print("7 " + reason + " ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		RoomInfo roomInfo = server.getRoomlist().getOneRoom(roomNumber);
		roomInfo.setSumConsumption(roomInfo.getSumConsumption() + roomInfo.getConsumption());//更新总能耗
		roomInfo.setSumFee(roomInfo.getSumFee() + roomInfo.getFee());//更新总费用
		
		server.getRoomlist().getOneRoom(roomNumber).setConsumption(0);//一次能耗清零
		server.getRoomlist().getOneRoom(roomNumber).setFee(0);//一次消费清零
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "主机送停风信息");	
	}
	
	public synchronized void sendApplyTemperature() {//消息号8
		out.print("8 ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "主机请求获取温度");	
	}
	
	public synchronized void sendConsumptionAndFee() {//消息号10
		double consumption = 0;
		double fee = 0;
		
		
		if (server.getRoomlist().getOneRoomState(roomNumber) == State.RUNNING) {
			try {
				long interval = System.currentTimeMillis() - scheduler.getExecuteRequest(roomNumber).getStartTime().getTime();
			
				consumption = scheduler.calculateConsumption(interval, 
						server.getRoomlist().getOneRoomWind(roomNumber));
				fee = scheduler.calculateFee(interval, 
						server.getRoomlist().getOneRoomWind(roomNumber));		
			}
			catch (Exception e) {
				//scheduler.getExecuteRequest(roomNumber)为null
				//说明此时该房间状态修改为STANDBY，所以费用不用计算，跳出该if子句
			}
		}
		
		
		out.print("10 " + consumption + " " + fee + " ");
		out.flush();
		if(out.checkError()) {
			isLink = false;
		}
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "发送计费用量信息" + consumption + " " + fee);	
		server.getRoomlist().updateOneRoomConsumption(roomNumber, consumption);
		server.getRoomlist().updateOneRoomFee(roomNumber, fee);
	}
	
	public void decode() {
		if (in.hasNext()) {
			int infoNumber = in.nextInt();
			switch (infoNumber) {
			/*消息号1：从机上线*/
			case 1:
				//需要设置房间号
				roomNumber = in.nextInt();//获取房间号
				server.getRoomlist().addOneRoom(roomNumber);//创建一个房间信息
				sendMode(server.getMode());//发送主控的运行模式
				
				DBFacade.getInstance().insert(new Log(roomNumber, new Timestamp(System.currentTimeMillis()), "开机"), Log.class);
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "收到从机上线信息");
				break;
				
			/*消息号3：调节温度请求*/	
			case 3:
				double goalTemperature = in.nextDouble();
				int tmpWind = in.nextInt();
				Wind wind;
				if (tmpWind == 0)
					wind = Wind.LOW;
				else if (tmpWind == 1)
					wind = Wind.MEDIUM;
				else 
					wind = Wind.HIGH;
				
				Request request = new Request(roomNumber, goalTemperature, wind);
				
				DBFacade.getInstance().insert(new Log(roomNumber, new Timestamp(System.currentTimeMillis()), 
						"收到调节温度请求,目标温度: " + goalTemperature + ",风速: " + wind), Log.class);
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "收到调节温度请求");
				/*下面处理过程， 保证了任何时刻等待链和运行链中同一个房间的请求个数之和不超过一*/
				if (scheduler.isValid(request)) {//请求合理
					if (scheduler.isServer(roomNumber)) {//正在被服务
						scheduler.removeExecutelistRequest(roomNumber);//移除正在运行的请求
						sendStopWind(1);//发送停风信息，被自己抢占
						replyIsValid(0);//回复请求合法
						scheduler.addRequest(request);//添加新请求到等待链
					}
					else {//不正在被服务
						scheduler.removeWaitlistRequest(roomNumber);//删除等待链中该房间之前的请求
						scheduler.addRequest(request);//加入新的请求
						replyIsValid(0);//回复请求合法
					}
				}
				else {
					replyIsValid(1);//回复请求非法
				}
				scheduler.running();
				
				break;
			
			/*消息号5：停风请求*/	
			case 5:
				DBFacade.getInstance().insert(new Log(roomNumber, new Timestamp(System.currentTimeMillis()), 
						"收到停风请求"), Log.class);
				
				scheduler.removeExecutelistRequest(roomNumber);
				sendStopWind(0);//发送停风信息，正常停风
				
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "收到停风请求");
				break;
				
			/*消息号9：从机返回温度*/	
			case 9:
				double temperature = in.nextDouble();
				server.getRoomlist().setOneRoomCurTemperature(roomNumber, temperature);
				
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "从机返回温度");
				break;
			default:
				break;
			}
			
		}
	}

	public int getRoomNumber() {
		return roomNumber;
	}
	
	public void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public boolean isLink() {
		return isLink;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("startOneCilent");
		try {
			try {				
				while (isLink) {
					decode();
				}				
			}
			finally {
				socket.close();
				scheduler.removeExecutelistRequest(roomNumber);//移除正在运行的请求
				scheduler.removeWaitlistRequest(roomNumber);//移除该房间残留在等待链中的请求
				server.getRoomlist().removeOneRoom(roomNumber);//删除该房间信息
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}

}

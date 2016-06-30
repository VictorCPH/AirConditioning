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
	
	public synchronized void sendMode(Mode mode) {//��Ϣ��2
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
		
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "���ߺ���������ģʽ��Ϣ");
	}
	
	//��0���Ϸ����� ��1���Ƿ�����
	public synchronized void replyIsValid(int isValid) {//��Ϣ��4
		
		out.print("4 " + isValid + " ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		if (isValid == 0)
			System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�����¶������Ӧ:�Ϸ�");		
		else
			System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�����¶������Ӧ:�Ƿ�");	
	}
	
	public synchronized void sendStartWind() {//��Ϣ��6
		out.print("6 ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�����ͷ���Ϣ");	
	}
	
	public synchronized void sendStopWind(int reason) {//��Ϣ��7
		out.print("7 " + reason + " ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		RoomInfo roomInfo = server.getRoomlist().getOneRoom(roomNumber);
		roomInfo.setSumConsumption(roomInfo.getSumConsumption() + roomInfo.getConsumption());//�������ܺ�
		roomInfo.setSumFee(roomInfo.getSumFee() + roomInfo.getFee());//�����ܷ���
		
		server.getRoomlist().getOneRoom(roomNumber).setConsumption(0);//һ���ܺ�����
		server.getRoomlist().getOneRoom(roomNumber).setFee(0);//һ����������
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "������ͣ����Ϣ");	
	}
	
	public synchronized void sendApplyTemperature() {//��Ϣ��8
		out.print("8 ");
		out.flush();
		
		if(out.checkError()) {
			isLink = false;
		}
		
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "���������ȡ�¶�");	
	}
	
	public synchronized void sendConsumptionAndFee() {//��Ϣ��10
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
				//scheduler.getExecuteRequest(roomNumber)Ϊnull
				//˵����ʱ�÷���״̬�޸�ΪSTANDBY�����Է��ò��ü��㣬������if�Ӿ�
			}
		}
		
		
		out.print("10 " + consumption + " " + fee + " ");
		out.flush();
		if(out.checkError()) {
			isLink = false;
		}
		System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "���ͼƷ�������Ϣ" + consumption + " " + fee);	
		server.getRoomlist().updateOneRoomConsumption(roomNumber, consumption);
		server.getRoomlist().updateOneRoomFee(roomNumber, fee);
	}
	
	public void decode() {
		if (in.hasNext()) {
			int infoNumber = in.nextInt();
			switch (infoNumber) {
			/*��Ϣ��1���ӻ�����*/
			case 1:
				//��Ҫ���÷����
				roomNumber = in.nextInt();//��ȡ�����
				server.getRoomlist().addOneRoom(roomNumber);//����һ��������Ϣ
				sendMode(server.getMode());//�������ص�����ģʽ
				
				DBFacade.getInstance().insert(new Log(roomNumber, new Timestamp(System.currentTimeMillis()), "����"), Log.class);
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�յ��ӻ�������Ϣ");
				break;
				
			/*��Ϣ��3�������¶�����*/	
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
						"�յ������¶�����,Ŀ���¶�: " + goalTemperature + ",����: " + wind), Log.class);
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�յ������¶�����");
				/*���洦����̣� ��֤���κ�ʱ�̵ȴ�������������ͬһ��������������֮�Ͳ�����һ*/
				if (scheduler.isValid(request)) {//�������
					if (scheduler.isServer(roomNumber)) {//���ڱ�����
						scheduler.removeExecutelistRequest(roomNumber);//�Ƴ��������е�����
						sendStopWind(1);//����ͣ����Ϣ�����Լ���ռ
						replyIsValid(0);//�ظ�����Ϸ�
						scheduler.addRequest(request);//��������󵽵ȴ���
					}
					else {//�����ڱ�����
						scheduler.removeWaitlistRequest(roomNumber);//ɾ���ȴ����и÷���֮ǰ������
						scheduler.addRequest(request);//�����µ�����
						replyIsValid(0);//�ظ�����Ϸ�
					}
				}
				else {
					replyIsValid(1);//�ظ�����Ƿ�
				}
				scheduler.running();
				
				break;
			
			/*��Ϣ��5��ͣ������*/	
			case 5:
				DBFacade.getInstance().insert(new Log(roomNumber, new Timestamp(System.currentTimeMillis()), 
						"�յ�ͣ������"), Log.class);
				
				scheduler.removeExecutelistRequest(roomNumber);
				sendStopWind(0);//����ͣ����Ϣ������ͣ��
				
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�յ�ͣ������");
				break;
				
			/*��Ϣ��9���ӻ������¶�*/	
			case 9:
				double temperature = in.nextDouble();
				server.getRoomlist().setOneRoomCurTemperature(roomNumber, temperature);
				
				System.out.println(roomNumber + " " + new Timestamp(System.currentTimeMillis()) + "�ӻ������¶�");
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
				scheduler.removeExecutelistRequest(roomNumber);//�Ƴ��������е�����
				scheduler.removeWaitlistRequest(roomNumber);//�Ƴ��÷�������ڵȴ����е�����
				server.getRoomlist().removeOneRoom(roomNumber);//ɾ���÷�����Ϣ
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		} 
	}

}

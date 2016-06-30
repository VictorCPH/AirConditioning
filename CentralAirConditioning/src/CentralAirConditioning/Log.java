package CentralAirConditioning;

import java.sql.Timestamp;

public class Log {
	/*
	 * *�¼�:�������ػ������յ��¶ȵ������󣬿�ʼִ������ִֹͣ������
	 */
	private int roomNumber;
	private Timestamp eventTime;
	private String clientEvent;
	
	public Log(int roomNumber, Timestamp time, String event) {
		super();
		this.roomNumber = roomNumber;
		this.eventTime = time;
		this.clientEvent = event;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public Timestamp getTime() {
		return eventTime;
	}

	public String getEvent() {
		return clientEvent;
	}

	@Override
	public String toString() {
		return "Log [roomNumber=" + roomNumber + ", eventTime=" + eventTime + ", clientEvent=" + clientEvent + "]";
	}
	
	
}

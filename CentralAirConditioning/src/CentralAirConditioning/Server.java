package CentralAirConditioning;


public class Server {
	private Mode mode;
	private double lowTemperature;//�ڶ�Ӧģʽ�µ��¶ȷ�Χ����
	private double upTemperature;//�ڶ�Ӧģʽ�µ��¶ȷ�Χ����
	private int frequency;
	private int maxLoad;
	private Strategy strategy;//���޸�
	private double chargeRate;
	private RoomList roomList;
	
	public Server() {//���ó�ʼĬ��ֵ
		this.mode = Mode.COOL;
		this.lowTemperature = 18.0;
		this.upTemperature = 25.0;
		this.frequency = 1;
		this.maxLoad = 3;
		this.strategy = Strategy.FIFO;
		this.chargeRate = 5;
		this.roomList = new RoomList();
	}

	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
		if (mode == Mode.COOL) {
			this.lowTemperature = 18.0;
			this.upTemperature = 25.0;
		}
		else {
			this.lowTemperature = 25.0;
			this.upTemperature = 30.0;
		}
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public int getMaxLoad() {
		return maxLoad;
	}

	public void setMaxLoad(int maxLoad) {
		this.maxLoad = maxLoad;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public double getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(double chargeRate) {
		this.chargeRate = chargeRate;
	}

	public double getLowTemperature() {
		return lowTemperature;
	}

	public double getUpTemperature() {
		return upTemperature;
	}

	public RoomList getRoomlist() {
		return roomList;
	}

	@Override
	public String toString() {
		return "Server [mode=" + mode + ", lowTemperature=" + lowTemperature + ", upTemperature=" + upTemperature
				+ ", frequency=" + frequency + ", maxLoad=" + maxLoad + ", strategy=" + strategy + ", chargeRate="
				+ chargeRate + ", roomList=" + roomList + "]";
	}
	
	
}


enum Mode {
	COOL,HEAT//���䣬����
}

enum State {
	RUNNING, WAITING, STANDBY//���У��ȴ�������
}

enum Wind {
	LOW, MEDIUM, HIGH//���ٷ磬���ٷ磬���ٷ�
}

enum Strategy {
	FIFO, HWF, RR;//�����ȷ��� �߷������ȣ� ��ת����
}
package CentralAirConditioning;


public class Server {
	private Mode mode;
	private double lowTemperature;//在对应模式下的温度范围下限
	private double upTemperature;//在对应模式下的温度范围上限
	private int frequency;
	private int maxLoad;
	private Strategy strategy;//待修改
	private double chargeRate;
	private RoomList roomList;
	
	public Server() {//设置初始默认值
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
	COOL,HEAT//制冷，制热
}

enum State {
	RUNNING, WAITING, STANDBY//运行，等待，待机
}

enum Wind {
	LOW, MEDIUM, HIGH//低速风，中速风，高速风
}

enum Strategy {
	FIFO, HWF, RR;//先来先服务， 高风速优先， 轮转调度
}
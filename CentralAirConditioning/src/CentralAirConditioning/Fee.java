package CentralAirConditioning;

import java.sql.Timestamp;

public class Fee {
	private int roomNumber;
	private Timestamp startTime;
	private Timestamp endTime;
	private double startTempterature;
	private double endTempterature;
	private int wind;
	private double consumption;
	private double fee;
	
	public Fee(int roomNumber, Timestamp startTime, Timestamp endTime, double startTempterature, 
			double endTempterature, int wind, double consumption, double fee) {
		super();
		this.roomNumber = roomNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startTempterature = startTempterature;
		this.endTempterature = endTempterature;
		this.wind = wind;
		this.consumption = consumption;
		this.fee = fee;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public double getStartTempterature() {
		return startTempterature;
	}

	public double getEndTempterature() {
		return endTempterature;
	}

	public int getWind() {
		return wind;
	}

	public double getFee() {
		return fee;
	}

	
	public double getConsumption() {
		return consumption;
	}

	@Override
	public String toString() {
		return "Fee [roomNumber=" + roomNumber + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", startTempterature=" + startTempterature + ", endTempterature=" + endTempterature + ", wind=" + wind
				+ ", consumption=" + consumption + ", fee=" + fee + "]";
	}

	
	
	
}

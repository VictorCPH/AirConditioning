package CentralAirConditioning;

import java.sql.Timestamp;

public class Request {
	private int roomNumber;
	private double goalTemperature;
	private Wind wind;
	private Timestamp startTime;
	private double startTemperature;
	
	public Request(int roomNumber, double goalTemperature, Wind wind) {
		super();
		this.roomNumber = roomNumber;
		this.goalTemperature = goalTemperature;
		this.wind = wind;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public double getGoalTemperature() {
		return goalTemperature;
	}

	public Wind getWind() {
		return wind;
	}

	public double getStartTemperature() {
		return startTemperature;
	}

	public void setStartTemperature(double startTemperature) {
		this.startTemperature = startTemperature;
	}

	@Override
	public synchronized String toString() {
		return "Request [roomNumber=" + roomNumber + ", goalTemperature=" + goalTemperature + ", wind=" + wind
				+ ", startTime=" + startTime + ", startTemperature=" + startTemperature + "]";
	}

	
}
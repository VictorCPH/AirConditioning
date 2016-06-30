package CentralAirConditioning;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RoomInfo {
	private int roomNumber;
	private State state;
	private double temperature;
	private Wind wind;
	private double fee;
	private double consumption;
	private double sumFee;
	private double sumConsumption;


	public RoomInfo(int roomNumber) {
		// TODO Auto-generated constructor stub
		this.roomNumber = roomNumber;
		this.state = State.STANDBY;
		this.wind = Wind.MEDIUM;
	}

	public int getRoomNumber() {
		return roomNumber;
	}

	public synchronized void setRoomNumber(int roomNumber) {
		this.roomNumber = roomNumber;
	}

	public State getState() {
		return state;
	}

	public synchronized void setState(State state) {
		this.state = state;
	}

	public double getTemperature() {
		return temperature;
	}

	public synchronized void setTemperature(double temperature) {
		this.temperature = temperature;
	}

	public Wind getWind() {
		return wind;
	}

	public synchronized void setWind(Wind wind) {
		this.wind = wind;
	}

	public double getFee() {
		return fee;
	}

	public synchronized void setFee(double fee) {
		this.fee = fee;
	}

	public double getConsumption() {
		return consumption;
	}

	public synchronized void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	

	public synchronized void setMode(State state) {
		// TODO Auto-generated method stub
		this.state = state;
	}

	public double getSumFee() {
		return sumFee;
	}

	public synchronized void setSumFee(double sumFee) {
		this.sumFee = sumFee;
	}

	public double getSumConsumption() {
		return sumConsumption;
	}

	public synchronized void setSumConsumption(double sumConsumption) {
		this.sumConsumption = sumConsumption;
	}

	@Override
	public synchronized String toString() {
		return "RoomInfo [roomNumber=" + roomNumber + ", state=" + state + ", temperature=" + temperature + ", wind="
				+ wind + ", fee=" + fee + ", consumption=" + consumption + ", sumFee=" + sumFee + ", sumConsumption="
				+ sumConsumption + "]" + "\n";
	}
	
	
}

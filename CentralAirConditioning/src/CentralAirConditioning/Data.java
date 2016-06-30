package CentralAirConditioning;

import javafx.beans.property.SimpleStringProperty;

public final class Data{
	private final SimpleStringProperty rNumber;
	private final SimpleStringProperty sta;
	private final SimpleStringProperty temp;
	private final SimpleStringProperty win;
	private final SimpleStringProperty cons;
	private final SimpleStringProperty fee;
	
	public Data(String roomNumber, String state, String temperature, String wind, String consumption, String sumfee){
		this.rNumber = new SimpleStringProperty(roomNumber);
		this.sta = new SimpleStringProperty(state);
		this.temp = new SimpleStringProperty(temperature);
		this.win = new SimpleStringProperty(wind);
		this.cons = new SimpleStringProperty(consumption);
		this.fee = new SimpleStringProperty(sumfee);
	}
	
	public String getRoomNumber(){
		return rNumber.get();
	}
	
	public void setRoomNumber(String roomNumber){
		rNumber.set(roomNumber);
	}
	
	public String getState(){
		return sta.get();
	}
	
	public void setState(String state){
		sta.set(state);
	}
	
	public String getTemperature(){
		return temp.get();
	}
	
	public void setTemperature(String temperature){
		temp.set(temperature);
	}
	
	public String getWind(){
		return win.get();
	}
	
	public void setWind(String wind){
		win.set(wind);
	}
	
	public String getConsumption(){
		return cons.get();
	}
	
	public void setConsumption(String consumption){
		cons.set(consumption);
	}
	
	public String getFee(){
		return fee.get();
	}
	
	public void setFee(String sumfee){
		fee.set(sumfee);
	}
}

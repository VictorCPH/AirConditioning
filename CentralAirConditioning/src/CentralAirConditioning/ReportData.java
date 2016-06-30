package CentralAirConditioning;

import javafx.beans.property.SimpleStringProperty;

public final class ReportData{
	private final SimpleStringProperty D_start;
	private final SimpleStringProperty D_end;
	private final SimpleStringProperty T_start;
	private final SimpleStringProperty T_end;
	private final SimpleStringProperty W_run;
	private final SimpleStringProperty Fee;
	
	public ReportData(String DStart, String DEnd, String TStart, String TEnd, String WRun, String fee){
		this.D_start = new SimpleStringProperty(DStart);
		this.D_end = new SimpleStringProperty(DEnd);
		this.T_start = new SimpleStringProperty(TStart);
		this.T_end = new SimpleStringProperty(TEnd);
		this.W_run = new SimpleStringProperty(WRun);
		this.Fee = new SimpleStringProperty(fee);
	}
	
	public String getStartDate(){
		return D_start.get();
	}
	
	public void setStartDate(String DStart){
		D_start.set(DStart);
	}
	
	public String getEndDate(){
		return D_end.get();
	}
	
	public void setEndDate(String DEnd){
		D_end.set(DEnd);
	}
	
	public String getStartTemperature(){
		return T_start.get();
	}
	
	public void setStartTemperature(String temperature){
		T_start.set(temperature);
	}
	
	public String getEndTemperature(){
		return T_end.get();
	}
	
	public void setEndTemperature(String temperature){
		T_end.set(temperature);
	}
	
	public String getWind(){
		return W_run.get();
	}
	
	public void setWind(String wind){
		W_run.set(wind);
	}
		
	public String getFee(){
		return Fee.get();
	}
	
	public void setFee(String sumfee){
		Fee.set(sumfee);
	}
}

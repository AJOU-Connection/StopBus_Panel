package panel.model;

import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BusStop {

	private final StringProperty busStopNum;
	private final StringProperty busStopName;
	private final StringProperty busStopInfo;
	private final IntegerProperty availability;
	
	public BusStop() {
		this(null, null, null);
	}
	
	public BusStop(String busStopNum, String busStopName, String busStopInfo){
		
		this.busStopName = new SimpleStringProperty(busStopName);
		this.busStopNum = new SimpleStringProperty(busStopNum);
		this.busStopInfo = new SimpleStringProperty(busStopInfo);
		this.availability = new SimpleIntegerProperty(0);
	}
	
	
	public String getBusStopNum() {
		return busStopNum.get();
	}
	
	public void setBusStopNum(String busStopNum) {
		this.busStopNum.set(busStopNum);
	}
	
	public StringProperty busStopNumProperty() {
		return busStopNum;
	}
	
	public String getBusStopName() {
		return busStopName.get();
	}
	
	public void setBusStopName(String busStopName) {
		this.busStopName.set(busStopName);
	}
	
	public StringProperty busStopNameProperty() {
		return busStopName;
	}
	
	public String getBusStopInfo() {
		return busStopInfo.get();
	}
	
	public void setBusStopInfo(String busStopInfo) {
		this.busStopInfo.set(busStopInfo);
	}
	
	public StringProperty busStopInfoProperty() {
		return busStopInfo;
	}
	
	public int getAvailability() {
		return availability.get();
	}
	
	public void setAvailability(int availability) {
		this.availability.set(availability);
	}
	
	public IntegerProperty availabilityProperty() {
		return availability;
	}
}

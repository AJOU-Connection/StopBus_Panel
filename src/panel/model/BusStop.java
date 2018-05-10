package panel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BusStop {

	private final IntegerProperty busStopNum;
	private final StringProperty busStopName;
	private final StringProperty busStopInfo;
	
	public BusStop() {
		this(0, null, null);
	}
	
	public BusStop(int busStopNum, String busStopName, String busStopInfo){
		
		this.busStopName = new SimpleStringProperty(busStopName);
		
		this.busStopNum = new SimpleIntegerProperty(busStopNum);
		this.busStopInfo = new SimpleStringProperty(busStopInfo);
	}
	
	public int getBusStopNum() {
		return busStopNum.get();
	}
	
	public void setBusStopNum(int busStopNum) {
		this.busStopNum.set(busStopNum);
	}
	
	public IntegerProperty busStopNumProperty() {
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
}

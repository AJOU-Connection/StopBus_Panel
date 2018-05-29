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
	private final StringProperty stationID;
	private final StringProperty stationSeq;
	
	public BusStop() {
		this(null, null, null);
	}
	
	public BusStop(String busStopNum, String busStopName, String busStopInfo){
		this.busStopName = new SimpleStringProperty(busStopName);
		this.busStopNum = new SimpleStringProperty(busStopNum);
		this.busStopInfo = new SimpleStringProperty(busStopInfo);
		
		this.stationID = new SimpleStringProperty(null);
		this.stationSeq = new SimpleStringProperty(null);
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
	
	public String getStationID() {
		return stationID.get();
	}
	
	public void setStationID(String stationID) {
		this.stationID.set(stationID);
	}
	
	public StringProperty statoinIDProperty() {
		return stationID;
	}
	
	public String getStationSeq() {
		return stationSeq.get();
	}
	
	public void setStationSeq(String stationSeq) {
		this.stationSeq.set(stationSeq);
	}
	
	public StringProperty stationSeqProperty() {
		return stationSeq;
	}
}

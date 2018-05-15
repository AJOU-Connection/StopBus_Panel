package panel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BusInfo {

	private final StringProperty busNum;
	private final StringProperty timeRemaining;
	private final StringProperty currentStop;
	
	public BusInfo() {
		this(null, null, null);
	}
	
	public BusInfo(String busNum, String timeRemaining, String currentStop) {
		this.busNum = new SimpleStringProperty(busNum);
		this.timeRemaining = new SimpleStringProperty(timeRemaining);
		this.currentStop = new SimpleStringProperty(currentStop);
	}
	
	public String getBusNum() {
		return busNum.get();
	}
	
	public void setBusNum(String busNum) {
		this.busNum.set(busNum);
	}
	
	public StringProperty busNumProperty() {
		return busNum;
	}
	
	public String getTimeRemaining() {
		return timeRemaining.get();
	}
	
	public void setTimeRemaining(String timeRemaining) {
		this.timeRemaining.set(timeRemaining);
	}
	
	public StringProperty timeRemainingProperty() {
		return timeRemaining;
	}
	public String getCurrentStop() {
		return currentStop.get();
	}
	
	public void setCurrentStop(String currentStop) {
		this.currentStop.set(currentStop);
	}
	
	public StringProperty currentStopProperty() {
		return currentStop;
	}
	
}

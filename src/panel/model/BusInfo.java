package panel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BusInfo {

	private final StringProperty busNum;
	private final StringProperty timeRemaining;
	private final StringProperty currentStop;
	
	private final IntegerProperty availability;
	
	public BusInfo() {
		this(null, null, null);
	}
	
	public BusInfo(String busNum, String timeRemaining, String currentStop) {
		this.busNum = new SimpleStringProperty(busNum);
		this.timeRemaining = new SimpleStringProperty(timeRemaining);
		this.currentStop = new SimpleStringProperty(currentStop);
		this.availability = new SimpleIntegerProperty(0);
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

package panel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class ArrivingBus {
	
	private final StringProperty busNumber;
	private final StringProperty timeRemaining;
	private final StringProperty currentStop;
	
	public ArrivingBus() {
		this(null, null, null);
	}
	
	public ArrivingBus(String busNumber, String timeRemaining, String currentStop) {
		// TODO Auto-generated constructor stub
		this.busNumber = new SimpleStringProperty(busNumber);
		this.timeRemaining = new SimpleStringProperty(timeRemaining);
		this.currentStop = new SimpleStringProperty(currentStop);
	}

	public String getBusNumber() {
		return busNumber.get();
	}
	
	public void setBusNumber(String busNumber) {
		this.busNumber.set(busNumber);
	}
	
	public StringProperty busNumberProperty() {
		return busNumber;
	}
	
	public String getTimeRemaining() {
		return timeRemaining.get();
	}
	
	public void setTimeRemaining(String timeRemaing) {
		this.timeRemaining.set(timeRemaing);
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

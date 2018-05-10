package panel.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ArrivingBus {
	
	private final StringProperty busNumber;
	private final StringProperty timeRemaining;
	private final StringProperty currentStop;
	
	private final IntegerProperty availability;
	
	public ArrivingBus() {
		this(null, null, null);
	}
	
	public ArrivingBus(String busNumber, String timeRemaining, String currentStop) {
		// TODO Auto-generated constructor stub
		this.busNumber = new SimpleStringProperty(busNumber);
		this.timeRemaining = new SimpleStringProperty(timeRemaining);
		this.currentStop = new SimpleStringProperty(currentStop);
		this.availability = new SimpleIntegerProperty(0);
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

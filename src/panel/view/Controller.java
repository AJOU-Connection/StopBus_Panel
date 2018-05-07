package panel.view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import panel.MainApp;
import panel.model.*;
import panel.view.*;

public class Controller{

	@FXML
	private Label busNumLable;
	@FXML
	private Label timeRemainingLabel;
	@FXML
	private Label currentStopLable;
	
	private Model busStop;
	
	private MainApp mainApp;
	
	private void initialize() {
		
	}
	
	public Controller() {
		
	}
	
	public void setBusStop(Model busStop) {
		this.busStop = busStop;
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}

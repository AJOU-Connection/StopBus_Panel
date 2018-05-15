package panel.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import panel.model.BusStop;

public class SettingDialogController {
	
	@FXML
	private TextField busStopNumField;
	@FXML
	private TextField busStopNameField;
	@FXML
	private TextField busStopInfoField;
	
	private Stage dialogStage;
	private BusStop busStop;
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
		
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	public void setBusStop(BusStop busStop) {
		this.busStop = busStop;
		busStopNumField.setText(Integer.toString(busStop.getBusStopNum()));
		busStopNameField.setText(busStop.getBusStopName());
		busStopInfoField.setText(busStop.getBusStopInfo());
	}
	
	public boolean isOkClicked() {
		return okClicked;
	}
	
	@FXML
	private void handleOk() {
		if(isInputValid()) {
			busStop.setBusStopNum(Integer.parseInt(busStopNumField.getText()));
			busStop.setBusStopName(busStopNameField.getText());
			busStop.setBusStopInfo(busStopInfoField.getText());
			okClicked = true;
			dialogStage.close();
		}
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	
	private boolean isInputValid() {
		String errorMessage = "";
		if(busStopNumField.getText() == null || busStopNumField.getText().length() == 0) {
			errorMessage += "Np Valid bus stop number!\n";
		}
		else {
			try {
				Integer.parseInt(busStopNumField.getText());
			}catch(NumberFormatException e) {
				errorMessage += "No Valid bus stop number(must be an integer)!\n";
			}
		}
		if(busStopNameField.getText() == null || busStopNameField.getText().length() == 0) {
			errorMessage += "No valid bus stop name!\n";
		}
		if(busStopInfoField.getText() == null || busStopInfoField.getText().length() == 0) {
			errorMessage += "No valid bus stop info!\n";
		}
		
		if(errorMessage.length() == 0) {
			return true;
		}else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			return false;
		}
	}
	
}

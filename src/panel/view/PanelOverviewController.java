package panel.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import panel.MainApp;
import panel.model.ArrivingBus;
import panel.model.BusStop;

public class PanelOverviewController {

	@FXML
	private TableView<ArrivingBus> arrivingBusTable;
	@FXML
	private TableColumn<ArrivingBus, String> busNumberColumn;
	@FXML
	private TableColumn<ArrivingBus, String> timeRemainingColumn;
	@FXML
	private TableColumn<ArrivingBus, String> currentStopColumn;
	
	@FXML
	private Label busStopNumLabel;
	@FXML
	private Label busStopNameLabel;
	@FXML
	private Label busStopInfoLabel;

	private MainApp mainApp;
	
	public PanelOverviewController() {
		
	}
	
	@FXML
	private void initialize() {
		busNumberColumn.setCellValueFactory(cellData -> cellData.getValue().busNumberProperty());
		timeRemainingColumn.setCellValueFactory(cellData -> cellData.getValue().timeRemainingProperty());
		currentStopColumn.setCellValueFactory(cellData -> cellData.getValue().currentStopProperty());
	}
	
	
	@FXML
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		arrivingBusTable.setItems(mainApp.getPanelData());
	}

}

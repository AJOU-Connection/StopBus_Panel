package panel.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
	private TableColumn<ArrivingBus, Integer> availabilityColumn;

	@FXML
	private Label busStopNumLabel;
	@FXML
	private Label busStopNameLabel;
	@FXML
	private Label busStopInfoLabel;

	private MainApp mainApp;
	
	public PanelOverviewController() {
		
	}
	
	private void showBusStopDetails(BusStop busStop) {
		busStopNumLabel.setText(Integer.toString(busStop.getBusStopNum()));
		busStopNameLabel.setText(busStop.getBusStopName());
		busStopInfoLabel.setText(busStop.getBusStopInfo());
	}
	
	@FXML
	private void initialize() {
		busNumberColumn.setCellValueFactory(cellData -> cellData.getValue().busNumberProperty());
		timeRemainingColumn.setCellValueFactory(cellData -> cellData.getValue().timeRemainingProperty());
		currentStopColumn.setCellValueFactory(cellData -> cellData.getValue().currentStopProperty());
		availabilityColumn.setCellValueFactory(cellData -> cellData.getValue().availabilityProperty().asObject());
		

		busNumberColumn.setCellFactory(column -> {
			return new TableCell<ArrivingBus, String>(){
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					setText(empty ? "" : getItem().toString());
					setGraphic(null);
					
					TableRow<ArrivingBus> currentRow = getTableRow();
					ArrivingBus rowData = currentRow.getItem();
					
					//add delay
					currentRow.setOnMouseClicked(event-> {
						rowData.setAvailability(1);
					});
				}
			};
		});
	}
	
	
	@FXML
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		arrivingBusTable.setItems(mainApp.getArrivingBusData());
		
	}

}

package panel.view;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
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
	private Label busStopNumLabel;
	@FXML
	private Label busStopNameLabel;
	@FXML
	private Label busStopInfoLabel;

	private MainApp mainApp;
	
	public PanelOverviewController() {
		
	}
	
	@FXML
	private void handleSettingBusStop() {
		BusStop tempBusStop = new BusStop();
		boolean okClicked = mainApp.showSettingDialog(tempBusStop);
		if(okClicked) {
			//mainApp.getBusStop().setBusStopNum(tempBusStop.getBusStopNum());
			//mainApp.getBusStop().setBusStopName(tempBusStop.getBusStopName());
			//mainApp.getBusStop().setBusStopInfo(tempBusStop.getBusStopInfo());
		}
		busStopNumLabel.setText(mainApp.getBusStop().getBusStopNum());
		busStopNameLabel.setText(mainApp.getBusStop().getBusStopName());
		busStopInfoLabel.setText(mainApp.getBusStop().getBusStopInfo());
	}
	
	@FXML
	private void initialize() {
		busNumberColumn.setCellValueFactory(cellData -> cellData.getValue().busNumberProperty());
		timeRemainingColumn.setCellValueFactory(cellData -> cellData.getValue().timeRemainingProperty());
		currentStopColumn.setCellValueFactory(cellData -> cellData.getValue().currentStopProperty());

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
						if(event.getClickCount() == 2 && (! currentRow.isEmpty())) {
							rowData.setAvailability(1);
						}
					});

					/*
					if(!isEmpty()) {
						if(rowData.getAvailability() != 0) {
							currentRow.setStyle("-fx-background-color:lightcoral");
						}
					}
					*/
					arrivingBusTable.refresh();
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

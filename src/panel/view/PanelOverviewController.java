package panel.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import panel.MainApp;
import panel.model.ArrivingBus;
import panel.model.BusInfo;
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

	@FXML
	private TableView<BusInfo> busInfoTable;
	@FXML
	private TableColumn<BusInfo, String> busInfoNumColumn;
	@FXML
	private TableColumn<BusInfo, String> busInfoTimeColumn;
	@FXML
	private TableColumn<BusInfo, String> busInfoStopColumn;
	
	private ObservableList<BusInfo> busInfoList = FXCollections.observableArrayList();
	
	private Pagination pagination;
	
	private MainApp mainApp;
	
	public PanelOverviewController() {
		
	}
	
	//--------------------------------------------------------------

	public int itemsPerPage() {
		return 1;
	}
	
	public int rowsPerPage() {
		return 10;
	}
	
	public VBox createPage(int pageIndex) {
		int lastIndex = 0;
		int displace = busInfoList.size() % rowsPerPage();
		if(displace > 0) {
			lastIndex = busInfoList.size() / rowsPerPage();
		}else {
			lastIndex = busInfoList.size() / rowsPerPage();
		}
		
		VBox box = new VBox(5);
		int page = pageIndex * itemsPerPage();
		
		for(int i = page; i<page + itemsPerPage(); i++) {
			busInfoNumColumn.setCellValueFactory(
					new PropertyValueFactory<BusInfo, String>("busNum"));
			busInfoTimeColumn.setCellValueFactory(
					new PropertyValueFactory<BusInfo, String>("timeRemaining"));
			busInfoStopColumn.setCellValueFactory(
					new PropertyValueFactory<BusInfo, String>("currentStop"));
			busInfoTable.getColumns().addAll(busInfoNumColumn, busInfoTimeColumn, busInfoStopColumn);
			if(lastIndex == pageIndex) {
				busInfoTable.setItems(FXCollections.observableArrayList(busInfoList.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + displace)));
			}else {
				busInfoTable.setItems(FXCollections.observableArrayList(busInfoList.subList(pageIndex * rowsPerPage(), pageIndex * rowsPerPage() + rowsPerPage())));
			}
			
			box.getChildren().add(busInfoTable);
		}
		return box;
	}
	
	//---------------------------------------------------------------
	
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
		
		pagination = new Pagination((busInfoList.size() / rowsPerPage() + 1), 0);
		pagination.setPageFactory(new Callback<Integer, Node>(){
			@Override
			public Node call(Integer pageIndex) {
				if(pageIndex > busInfoList.size() / rowsPerPage() + 1) {
					return null;
				}else {
					return createPage(pageIndex);
				}
			}
		});
		
	}
	
	public Pagination getPagination() {
		return pagination;
	}
	
	@FXML
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		arrivingBusTable.setItems(mainApp.getArrivingBusData());
		busInfoList = mainApp.getBusInfoData();
		busStopNumLabel.setText(mainApp.getBusStop().getBusStopNum());
		busStopNameLabel.setText(mainApp.getBusStop().getBusStopName());
		busStopInfoLabel.setText(mainApp.getBusStop().getBusStopInfo());
	}

}

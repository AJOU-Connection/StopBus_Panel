package panel.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import panel.MainApp;
import panel.model.ArrivingBus;
import panel.model.BusInfo;
import panel.model.BusStop;
import panel.util.SearchingStationUtil;

public class PanelOverviewController {

	@FXML
	private VBox arrivingBusBox = new VBox(5);

	@FXML
	private Label busStopNumLabel;
	@FXML
	private Label busStopNameLabel;
	@FXML
	private Label busStopInfoLabel;
	
	@FXML
	private Button searching;	
	@FXML
	private Button checking;
	@FXML
	private Button textsearching;
	@FXML
	private TextField searchText;
	@FXML
	private TextArea searchResult;
	@FXML
	private VBox searchBox;
	
	@FXML
	private Button refreshBtn;
	
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	private List<BusStop> searchingList = new ArrayList<BusStop>();
	private MainApp mainApp;
	
	
	public PanelOverviewController() {
		
	}
	@FXML
	private void reloadInfo() {
		mainApp.updateBusInfoList(mainApp.getBusInfoListData());
		mainApp.addPagination();
		updateBoxes();
	}
	
	@FXML
	private void getSearchingText() {
		
		String keyword = searchText.getText();
		String result = "";
		SearchingStationUtil searchingUtil = new SearchingStationUtil();

		searchingUtil.searchingStation(keyword);
		searchingList = searchingUtil.getSearchingList();
		
		result += "총 "+searchingList.size()+"개의 검색결과가 있습니다.\n\n";
		for(int i = 0; i < searchingList.size(); i++) {
			result += "정류장 번호 : " + searchingList.get(i).getBusStopNum() + "\n";
			result += "정류장 이름 : " + searchingList.get(i).getBusStopName() + "\n";
			result += "정류장 방면 : " + searchingList.get(i).getBusStopInfo() + "\n\n";
		}
		searchResult.setText(result);
	}
	
	@FXML
	private void setVisible() {
		searchBox.setVisible(true);
		mainApp.setPaginationUnvisible();
		searching.setStyle("-fx-background-color: #00838F");
		checking.setStyle("-fx-background-color: #00ACC1");
	}
	
	@FXML
	private void setUnvisible() {
		searchBox.setVisible(false);
		mainApp.setPaginationVisible();
		searching.setStyle("-fx-background-color: #00ACC1");
		checking.setStyle("-fx-background-color: #00838F");
	}
	
	@FXML
	private void initialize() {
		
	}
	
	public void createArrivingBusBox() {
		
		arrivingBusBox.setSpacing(5);
		
		for (int i = 0; i < arrivingBusData.size(); i++) {
			ArrivingBus tempArrivingBus = arrivingBusData.get(i);
					
        	HBox hbox = new HBox();
        	hbox.setStyle("-fx-background-color:white");
        	hbox.setAlignment(Pos.CENTER);
        	
        	Label busNum = new Label(tempArrivingBus.getBusNumber()+" 번");
    		Label busTime = new Label(tempArrivingBus.getTimeRemaining()+" 분 전");
    		Label busStop = new Label(tempArrivingBus.getCurrentStop()+" 정거장 전");
    		
    		busNum.setFont(new Font("Hancom Gothic", 16));
    		busNum.setStyle("-fx-padding: 10;");
    		busNum.setMinWidth(160.0);
    		busTime.setFont(new Font("Hancom Gothic", 16));
    		busTime.setStyle("-fx-padding: 10;");
    		busTime.setMinWidth(160.0);
    		busStop.setFont(new Font("Hancom Gothic", 16));
    		busStop.setStyle("-fx-padding: 10;");
    		busStop.setMinWidth(160.0);
    		
    		hbox.getChildren().add(busNum);
    		hbox.getChildren().add(busTime);
    		hbox.getChildren().add(busStop);
            
    		arrivingBusBox.getChildren().add(hbox);
        }
	}
	
	private void boxifyBoxes() {
		
		for(Node child : arrivingBusBox.getChildren()) {
			
			HBox hb = (HBox) child;
			hb.setOnMouseClicked((e) -> {
				hb.setStyle("-fx-background-color:lightcoral");
				setAvailability((int) hb.getLayoutY());
				mainApp.updateBusInfoList(mainApp.getBusInfoListData());
				mainApp.addPagination();
			});

		}
	}
	//문제가 심각함
	public void updateBoxes() {
		
		int index;
		for(Node child : arrivingBusBox.getChildren()) {	
			HBox hb = (HBox) child;
			hb.getChildren().clear();	
			index = (int) hb.getLayoutY() / 40;
			
			ArrivingBus tempArrivingBus = arrivingBusData.get(index);
			
			Label busNum = new Label(tempArrivingBus.getBusNumber()+" 번");
    		Label busTime = new Label(tempArrivingBus.getTimeRemaining()+" 분 전");
    		Label busStop = new Label(tempArrivingBus.getCurrentStop()+" 정거장 전");
    		
    		busNum.setFont(new Font("Hancom Gothic", 16));
    		busNum.setStyle("-fx-padding: 10;");
    		busNum.setMinWidth(160.0);
    		busTime.setFont(new Font("Hancom Gothic", 16));
    		busTime.setStyle("-fx-padding: 10;");
    		busTime.setMinWidth(160.0);
    		busStop.setFont(new Font("Hancom Gothic", 16));
    		busStop.setStyle("-fx-padding: 10;");
    		busStop.setMinWidth(160.0);
    		
    		hb.getChildren().add(busNum);
    		hb.getChildren().add(busTime);
    		hb.getChildren().add(busStop);
			
			if(arrivingBusData.get(index).getAvailability() != 0) {
				hb.setStyle("-fx-background-color:lightcoral");
			}
			else {
				hb.setStyle("-fx-background-color:white");
			}
		}
	}

	
	private void setAvailability(int i) {
		int index = i / 40;
		arrivingBusData.get(index).setAvailability(1);
		for(int j = 0; j < mainApp.getBusInfoListData().size(); j++) {
			if(arrivingBusData.get(index).getBusNumber() == mainApp.getBusInfoListData().get(j).getBusNum()) {
				mainApp.getBusInfoListData().get(j).setAvailability(1);
			}
		}
	}

	@FXML
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		busStopNumLabel.setText(mainApp.getBusStop().getBusStopNum());
		busStopNameLabel.setText(mainApp.getBusStop().getBusStopName());
		busStopInfoLabel.setText(mainApp.getBusStop().getBusStopInfo());
		arrivingBusData = mainApp.getArrivingBusData();
		createArrivingBusBox();
		boxifyBoxes();
	}

}

package panel;

import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Callback;
import panel.view.PanelOverviewController;
import panel.model.ArrivingBus;
import panel.model.BusInfo;
import panel.model.BusStop;
import panel.util.BusInfoUtil;
import panel.util.SearchingStationUtil;
import panel.util.ReservationUtil;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private BusStop busStop = new BusStop();
	private ObservableList<BusInfo> busInfoList = FXCollections.observableArrayList();
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	
	private AnchorPane panelOverview;
	private static PanelOverviewController controller;
	private Pagination pagination;
	
	private String stationSetting = "03126";
	
	BusInfoUtil busInfoUtil = new BusInfoUtil();
	ReservationUtil reservationUtil = new ReservationUtil();
	
	
	//UI 초기 화면에 필요한 data를 API로부터 받아온 후 observable list에 저장한다.
	public MainApp() {
		getBusStopInfo();
		getBusInfoList();
		getArrivingBusInfo();
	}
	
	//-------------------------------------초기 data 받아오기-------------------------------------
	
	//버스 정류장의 정보를 API로부터 받아와 busStop에 저장한다.
	public void getBusStopInfo() {
		SearchingStationUtil busStopUtil = new SearchingStationUtil();
		busStop = busStopUtil.searchingBusStop(stationSetting);
		
	}	
	
	//정류장을 지나는 모든 버스의 정보를 API로부터 받아와 busInfoList에 저장한다.
	public void getBusInfoList() {
		
		busInfoUtil.setBusInfo(busStop.getStationID());
		busInfoList = busInfoUtil.getBusInfoList();
	}
	
	public void updateBusInfoList(ObservableList<BusInfo> updateBusList) {

		busInfoUtil.updateBusInfo(busStop.getStationID(), updateBusList);
		updateArrivingBusData();
		
	}
		
	//busInfoList의 정보를 바탕으로 선착순 버스 정보를 arrivingBusData에 저장한다.
	public void getArrivingBusInfo() {
		
		BusInfo tempInfo;
		int printSize = 6;
		
		ObservableList<BusInfo> sortedBusInfoList = FXCollections.observableArrayList();
		
		for(BusInfo bi : busInfoList) {
			BusInfo tempBI = new BusInfo(bi.getBusNum(), bi.getTimeRemaining(), bi.getCurrentStop());
			
			sortedBusInfoList.add(tempBI);
		}
		
		busInfoList.sort((a, b) -> a.getBusNum().compareTo(b.getBusNum()));
		sortedBusInfoList.sort((a, b) -> Integer.compare(Integer.parseInt(a.getTimeRemaining()), Integer.parseInt(b.getTimeRemaining())));
		
		if(sortedBusInfoList.size() < 6) {
			printSize = sortedBusInfoList.size();
		}
		
		for(int i = 0; i < printSize; i++) {
			ArrivingBus tempBus = new ArrivingBus();
			tempInfo = sortedBusInfoList.get(i);
			
			if(Integer.parseInt(tempInfo.getTimeRemaining()) < 1000 || Integer.parseInt(tempInfo.getCurrentStop()) < 1000) {
				tempBus.setBusNumber(tempInfo.getBusNum());
				tempBus.setCurrentStop(tempInfo.getCurrentStop());
				tempBus.setTimeRemaining(tempInfo.getTimeRemaining());
				tempBus.setAvailability(tempInfo.getAvailability());
				arrivingBusData.add(tempBus);
			}
			
		}
		
	}
	
	//-------------------------------------data 업데이트 하기-------------------------------------
	
	//busInfoList의 정보를 바탕으로 arrvingBus를 업데이트 한다. createPage()에서 사용한다.
	public void updateArrivingBusData() {
		
		BusInfo tempInfo;
		int printSize = 6;
		
		ObservableList<BusInfo> sortedBusInfoList = FXCollections.observableArrayList();
		
		for(BusInfo bi : busInfoList) {
			BusInfo tempBI = new BusInfo(bi.getBusNum(), bi.getTimeRemaining(), bi.getCurrentStop());
			tempBI.setAvailability(bi.getAvailability());
			sortedBusInfoList.add(tempBI);
		}
		
		busInfoList.sort((a, b) -> a.getBusNum().compareTo(b.getBusNum()));
		sortedBusInfoList.sort((a, b) -> Integer.compare(Integer.parseInt(a.getTimeRemaining()), Integer.parseInt(b.getTimeRemaining())));
		
		if(sortedBusInfoList.size() < 6) {
			printSize = sortedBusInfoList.size();
		}
		
		for(int i = 0; i < printSize; i++) {
			tempInfo = sortedBusInfoList.get(i);
			if(Integer.parseInt(tempInfo.getCurrentStop()) < 1000 || Integer.parseInt(tempInfo.getTimeRemaining()) < 1000) {
				arrivingBusData.get(i).setBusNumber(tempInfo.getBusNum());
				arrivingBusData.get(i).setCurrentStop(tempInfo.getCurrentStop());
				arrivingBusData.get(i).setTimeRemaining(tempInfo.getTimeRemaining());
				arrivingBusData.get(i).setAvailability(tempInfo.getAvailability());
			}
			
		}
		
	}
	
	//-------------------------------------controller에 data 넘기기-------------------------------------
	
	public BusStop getBusStop() {
		return busStop;
	}
	
	public ObservableList<ArrivingBus> getArrivingBusData(){
		return arrivingBusData;
	}
			
	public ObservableList<BusInfo> getBusInfoListData(){
		return busInfoList;
	}		
	
	//-------------------------------------pagination 생성하기-------------------------------------
 
	//pagination을 위한 method로, 페이지 당 표시할 아이템의 수를 리턴한다.
    public int itemsPerPage() {
        return 8;
    }
 
    //pagination의 페이지마다 들어갈 VBox를 생성한다.
    public VBox createPage(int pageIndex) {        
        VBox box = new VBox(5);
        box.setStyle("-fx-background-color: #F4F4F4;");
        
        int page = pageIndex * itemsPerPage();
        
        for (int i = page; i < page + itemsPerPage(); i++) {
 
        	final int click = i;
        	
        	HBox hbox = new HBox();
        	hbox.setAlignment(Pos.CENTER);
        	
        	if(i < busInfoList.size()) {
        		BusInfo tempBusStop = busInfoList.get(i);
                boolean flag = false;
        		Label busNum = new Label(tempBusStop.getBusNum()+" 번");
        		Label busTime;
        		Label station;
        		
        		if(Integer.parseInt(tempBusStop.getTimeRemaining()) > 1000 || Integer.parseInt(tempBusStop.getCurrentStop()) > 1000) {
        			busTime = new Label("버스 정보 없음");
        			station = new Label("버스 정보 없음");
        		}
        		else {
        			busTime = new Label(tempBusStop.getTimeRemaining()+" 분 전");
            		station = new Label(tempBusStop.getCurrentStop()+" 정거장 전");
        		}
        		
        		busNum.setFont(new Font("Hancom Gothic", 16));
        		busNum.setStyle("-fx-padding: 10;");
        		busNum.setMinWidth(160.0);
        		busTime.setFont(new Font("Hancom Gothic", 16));
        		busTime.setStyle("-fx-padding: 10;");
        		busTime.setMinWidth(160.0);
        		station.setFont(new Font("Hancom Gothic", 16));
        		station.setStyle("-fx-padding: 10;");
        		station.setMinWidth(160.0);
        		
        		hbox.getChildren().add(busNum);
        		hbox.getChildren().add(busTime);
        		hbox.getChildren().add(station);
        		hbox.setStyle("-fx-background-color: white");
        		
                box.getChildren().add(hbox);
                
                if(busInfoList.get(i).getAvailability() != 0) {
                	hbox.setStyle("-fx-background-color:lightcoral");
                }
                else {
    
                	hbox.setStyle("-fx-background-color: white");
                	
                }
                
                //pagination click
                hbox.setOnMouseClicked((e) -> {
    				if(Integer.parseInt(busInfoList.get(click).getCurrentStop()) < 1000 || Integer.parseInt(busInfoList.get(click).getTimeRemaining()) < 1000) {
    					if(reservationUtil.postReservation(busInfoList.get(click).getRouteID(), busStop.getStationID())) {
        					hbox.setStyle("-fx-background-color:lightcoral");
        					busInfoList.get(click).setAvailability(1);
        					updateBusInfoList(busInfoList);
            				controller.updateBoxes();
        				}
    				}
    			});
        		
        	}
        	else {
            	Label nullLabel = new Label("");
            	nullLabel.setFont(new Font("Hancom Gothic", 16));
            	nullLabel.setStyle("-fx-padding: 10;");
                hbox.getChildren().add(nullLabel);
                box.getChildren().add(hbox);
        	}

        }
        return box;
    }
    
    //pagination을 panelOverview에 표시한다.
    public void addPagination() {
		Pagination newPagination = new Pagination(busInfoList.size()/itemsPerPage() + 1, 0);
        newPagination.setStyle("-fx-border-color: #C8C8C8;");
        newPagination.setPageFactory(new Callback<Integer, Node>() {
 
            @Override
            public Node call(Integer pageIndex) {          
                return createPage(pageIndex);               
            }
        });
        
        pagination = newPagination;
        
        panelOverview.setTopAnchor(pagination, 520.0);
        panelOverview.setRightAnchor(pagination, 80.0);
        panelOverview.setLeftAnchor(pagination, 80.0);
        panelOverview.getChildren().addAll(pagination);
	}
    
    public void updatePagination() {
  
    	pagination.setPageFactory(new Callback<Integer, Node>() {
    		 
            @Override
            public Node call(Integer pageIndex) {          
                return createPage(pageIndex);               
            }
        });
    }
    
    //-------------------------------------기타 기능을 위한 잡다한 method-------------------------------------
	/*
    getArrivingBusInfo() 때문에 busInfoList가 시간순대로 정렬되어버리는 슬픈 비극때문에 만들었음
    busInfoList를 버스 번호순대로 재정렬해주는 method
    */
    
    public void sortingBusInfoList() {
		
		for(int i = 0; i < busInfoList.size(); i++) {
			busInfoList.get(i).setBusNum(busInfoList.get(i).getBusNum().replace("-", "."));
		}
		
		busInfoList.sort((a, b) -> Double.compare(Double.parseDouble(a.getBusNum()), Double.parseDouble(b.getBusNum())));
		
		
		for(int i = 0; i < busInfoList.size(); i++) {
			busInfoList.get(i).setBusNum(busInfoList.get(i).getBusNum().replace(".", "-"));
		}
	}
    
    //말 그대로 pagination이 보이게 해주는 method. 여기서는 안쓰고 controller가 쓴다.
    public void setPaginationVisible() {
		pagination.setVisible(true);
	}
	
    //말 그대로 pagination이 안보이게 해주는 method. 여기서는 안쓰고 controller가 쓴다.
	public void setPaginationUnvisible() {
		pagination.setVisible(false);
	}
	
    //-------------------------------------본격 stage 실행하기-------------------------------------
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("StopBus");
		
		initRootLayout();
		showPanelOverview();
	}
	
	//상위 레이아웃 초기화
	public void initRootLayout() {
		try {
			//fxml 파일에서 상위 레이아웃을 가져온다
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//상위 레이아웃을 포함하는 scene을 보여준다
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//상위 레이아웃 안에 panel overview를 보여준다.
	public void showPanelOverview() {
		try {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PanelOverview.fxml"));
			panelOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(panelOverview);
			
			controller = loader.getController();
			
			controller.setMainApp(this);
			//updatePagination();
			addPagination();
			updatePagination();
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//메인 스테이지를 반환한다
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		launch(args);
	}
}

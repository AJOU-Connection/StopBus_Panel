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
	
	
	//UI �ʱ� ȭ�鿡 �ʿ��� data�� API�κ��� �޾ƿ� �� observable list�� �����Ѵ�.
	public MainApp() {
		getBusStopInfo();
		getBusInfoList();
		getArrivingBusInfo();
	}
	
	//-------------------------------------�ʱ� data �޾ƿ���-------------------------------------
	
	//���� �������� ������ API�κ��� �޾ƿ� busStop�� �����Ѵ�.
	public void getBusStopInfo() {
		SearchingStationUtil busStopUtil = new SearchingStationUtil();
		busStop = busStopUtil.searchingBusStop(stationSetting);
		
	}	
	
	//�������� ������ ��� ������ ������ API�κ��� �޾ƿ� busInfoList�� �����Ѵ�.
	public void getBusInfoList() {
		
		busInfoUtil.setBusInfo(busStop.getStationID());
		busInfoList = busInfoUtil.getBusInfoList();
	}
	
	public void updateBusInfoList(ObservableList<BusInfo> updateBusList) {

		busInfoUtil.updateBusInfo(busStop.getStationID(), updateBusList);
		updateArrivingBusData();
		
	}
		
	//busInfoList�� ������ �������� ������ ���� ������ arrivingBusData�� �����Ѵ�.
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
	
	//-------------------------------------data ������Ʈ �ϱ�-------------------------------------
	
	//busInfoList�� ������ �������� arrvingBus�� ������Ʈ �Ѵ�. createPage()���� ����Ѵ�.
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
	
	//-------------------------------------controller�� data �ѱ��-------------------------------------
	
	public BusStop getBusStop() {
		return busStop;
	}
	
	public ObservableList<ArrivingBus> getArrivingBusData(){
		return arrivingBusData;
	}
			
	public ObservableList<BusInfo> getBusInfoListData(){
		return busInfoList;
	}		
	
	//-------------------------------------pagination �����ϱ�-------------------------------------
 
	//pagination�� ���� method��, ������ �� ǥ���� �������� ���� �����Ѵ�.
    public int itemsPerPage() {
        return 8;
    }
 
    //pagination�� ���������� �� VBox�� �����Ѵ�.
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
        		Label busNum = new Label(tempBusStop.getBusNum()+" ��");
        		Label busTime;
        		Label station;
        		
        		if(Integer.parseInt(tempBusStop.getTimeRemaining()) > 1000 || Integer.parseInt(tempBusStop.getCurrentStop()) > 1000) {
        			busTime = new Label("���� ���� ����");
        			station = new Label("���� ���� ����");
        		}
        		else {
        			busTime = new Label(tempBusStop.getTimeRemaining()+" �� ��");
            		station = new Label(tempBusStop.getCurrentStop()+" ������ ��");
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
    
    //pagination�� panelOverview�� ǥ���Ѵ�.
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
    
    //-------------------------------------��Ÿ ����� ���� ����� method-------------------------------------
	/*
    getArrivingBusInfo() ������ busInfoList�� �ð������ ���ĵǾ������ ���� ��ض����� �������
    busInfoList�� ���� ��ȣ����� ���������ִ� method
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
    
    //�� �״�� pagination�� ���̰� ���ִ� method. ���⼭�� �Ⱦ��� controller�� ����.
    public void setPaginationVisible() {
		pagination.setVisible(true);
	}
	
    //�� �״�� pagination�� �Ⱥ��̰� ���ִ� method. ���⼭�� �Ⱦ��� controller�� ����.
	public void setPaginationUnvisible() {
		pagination.setVisible(false);
	}
	
    //-------------------------------------���� stage �����ϱ�-------------------------------------
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("StopBus");
		
		initRootLayout();
		showPanelOverview();
	}
	
	//���� ���̾ƿ� �ʱ�ȭ
	public void initRootLayout() {
		try {
			//fxml ���Ͽ��� ���� ���̾ƿ��� �����´�
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			//���� ���̾ƿ��� �����ϴ� scene�� �����ش�
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//���� ���̾ƿ� �ȿ� panel overview�� �����ش�.
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
	
	//���� ���������� ��ȯ�Ѵ�
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		launch(args);
	}
}

package panel;

import java.awt.Panel;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import panel.view.PanelOverviewController;
import panel.view.SettingDialogController;
import panel.model.ArrivingBus;
import panel.model.BusInfo;
import panel.model.BusStop;
import panel.util.BusInfoUtil;
import panel.util.BusStopUtil;
import panel.util.GetDataExample;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private BusStop busStop = new BusStop();
	private ObservableList<BusInfo> busInfoList = FXCollections.observableArrayList();
	//panel에 대한 observable 리스트
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	
	private Pagination pagination;
	
	
	
	//생성자
	public MainApp() {
		getBusStopInfo();
		getBusInfoList();
		getArrivingBusInfo();
	}
 
    public int itemsPerPage() {
        return 8;
    }
 
    public VBox createPage(int pageIndex) {        
        VBox box = new VBox(5);
        box.setStyle("-fx-border-color: #C8C8C8;");
        int page = pageIndex * itemsPerPage();
        
        for (int i = page; i < page + itemsPerPage(); i++) {
 
        	final int click = i;
        	
        	HBox hbox = new HBox();
        	hbox.setStyle("-fx-background-color: #F9F9F9");
        	hbox.setAlignment(Pos.CENTER);
        	
        	if(i < busInfoList.size()) {
        		BusInfo tempBusStop = busInfoList.get(i);
                boolean flag = false;
        		
        		Label busNum = new Label(tempBusStop.getBusNum()+" 번");
        		Label busTime = new Label(tempBusStop.getTimeRemaining()+" 분 전");
        		Label busStop = new Label(tempBusStop.getCurrentStop()+" 정거장 전");
        		
        		busNum.setFont(new Font("Arial", 16));
        		busNum.setStyle("-fx-padding: 10;");
        		busNum.setMinWidth(160.0);
        		busTime.setFont(new Font("Arial", 16));
        		busTime.setStyle("-fx-padding: 10;");
        		busTime.setMinWidth(160.0);
        		busStop.setFont(new Font("Arial", 16));
        		busStop.setStyle("-fx-padding: 10;");
        		busStop.setMinWidth(160.0);
        		
        		hbox.getChildren().add(busNum);
        		hbox.getChildren().add(busTime);
        		hbox.getChildren().add(busStop);
                
                box.getChildren().add(hbox);
                
                if(busInfoList.get(i).getAvailability() != 0) {
                	hbox.setStyle("-fx-background-color:lightcoral");
                }
                
                hbox.setOnMouseClicked((e) -> {
    				hbox.setStyle("-fx-background-color:lightcoral");
    				
    				busInfoList.get(click).setAvailability(1);
    				//System.out.println(hb.getLayoutX()+", "+hb.getLayoutY());
    			});
        	}
        	else {
            	Label nullLabel = new Label("");
            	nullLabel.setFont(new Font("Arial", 16));
            	nullLabel.setStyle("-fx-padding: 10;");
                hbox.getChildren().add(nullLabel);
                box.getChildren().add(hbox);
        	}

        }
        return box;
    }
    
	
	//도착하는 버스 정보 삽입
	public void getArrivingBusInfo() {
		
		ObservableList<BusInfo> tempList = busInfoList;
		BusInfo tempInfo;
		
		tempList.sort((a, b) -> Integer.compare(Integer.parseInt(a.getTimeRemaining()), Integer.parseInt(b.getTimeRemaining())));
		
		for(int i = 0; i < 6; i++) {
			ArrivingBus tempBus = new ArrivingBus();
			tempInfo = tempList.get(i);
			tempBus.setBusNumber(tempInfo.getBusNum());
			tempBus.setCurrentStop(tempInfo.getCurrentStop());
			tempBus.setTimeRemaining(tempInfo.getTimeRemaining());
			arrivingBusData.add(tempBus);
		}
		
		arrivingBusData.sort((a, b) -> Integer.compare(Integer.parseInt(a.getCurrentStop()), Integer.parseInt(b.getCurrentStop())));
	}
	
	
	
	//버스 정류장 정보 삽입
	public void getBusStopInfo() {
		BusStopUtil busStopUtil = new BusStopUtil();
		busStopUtil.setBusStop("04237");
		busStop = busStopUtil.getBusStop();
		
	}
	
	public BusStop getBusStop() {
		return busStop;
	}

	public void getBusInfoList() {
		BusInfoUtil busInfoUtil = new BusInfoUtil();
		busInfoUtil.setBusInfo("04237");
		busInfoList = busInfoUtil.getBusInfoList();
	}
	
	//도착하는 버스 정보를 리스트로부터 가져오기
	public ObservableList<ArrivingBus> getArrivingBusData(){
		return arrivingBusData;
	}
	
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
	
	//상위 레이아웃 안에 panel overview를 보여준다
	public void showPanelOverview() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PanelOverview.fxml"));
			AnchorPane panelOverview = (AnchorPane) loader.load();
			rootLayout.setCenter(panelOverview);
			
			PanelOverviewController controller = loader.getController();
	        
	        pagination = new Pagination(busInfoList.size()/itemsPerPage() + 1, 0);
	        pagination.setStyle("-fx-background-color:white;");
	        pagination.setPageFactory(new Callback<Integer, Node>() {
	 
	            @Override
	            public Node call(Integer pageIndex) {          
	                return createPage(pageIndex);               
	            }
	        });
	        
	        panelOverview.setTopAnchor(pagination, 560.0);
	        panelOverview.setRightAnchor(pagination, 80.0);
	        panelOverview.setLeftAnchor(pagination, 80.0);
	        //panelOverview.setBottomAnchor(pagination, 80.0);
	        panelOverview.getChildren().addAll(pagination);
	        
	        controller.setMainApp(this);
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public boolean showSettingDialog(BusStop busStop) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SettingDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Bus Stop");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			
			SettingDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setBusStop(busStop);
			
			dialogStage.showAndWait();
			return controller.isOkClicked();
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	//메인 스테이지를 반환한다
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	

	public static void main(String[] args) throws Exception {
		
		
	    //GetDataExample exam = new GetDataExample();
	    //exam.getArrivalTimes("04237");
		
		//BusInfoUtil busInfoUtil = new BusInfoUtil();
		//busInfoUtil.getArrivalTimes("04237");
		
		launch(args);
		
	}
}

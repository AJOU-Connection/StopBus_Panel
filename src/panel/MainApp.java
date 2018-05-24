package panel;

import java.awt.Panel;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
	//panel�� ���� observable ����Ʈ
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	
	private Pagination pagination;
	
	//UI �ʱ� ȭ�鿡 �ʿ��� data�� API�κ��� �޾ƿ� �� observable list�� �����Ѵ�.
	public MainApp() {
		getBusStopInfo();
		getBusInfoList();
		getArrivingBusInfo();
	}
	
	//-------------------------------------�ʱ� data �޾ƿ���-------------------------------------
	
	//���� �������� ������ API�κ��� �޾ƿ� busStop�� �����Ѵ�.
	public void getBusStopInfo() {
		BusStopUtil busStopUtil = new BusStopUtil();
		busStopUtil.setBusStop("04237");
		busStop = busStopUtil.getBusStop();
		
	}	
	
	//�������� ������ ��� ������ ������ API�κ��� �޾ƿ� busInfoList�� �����Ѵ�.
	public void getBusInfoList() {
		BusInfoUtil busInfoUtil = new BusInfoUtil();
		busInfoUtil.setBusInfo("04237");
		busInfoList = busInfoUtil.getBusInfoList();
	}
		
	//busInfoList�� ������ �������� ������ ���� ������ arrivingBusData�� �����Ѵ�.
	public void getArrivingBusInfo() {
		
		BusInfo tempInfo;
		
		busInfoList.sort((a, b) -> Integer.compare(Integer.parseInt(a.getTimeRemaining()), Integer.parseInt(b.getTimeRemaining())));
		
		for(int i = 0; i < 6; i++) {
			ArrivingBus tempBus = new ArrivingBus();
			tempInfo = busInfoList.get(i);
			tempBus.setBusNumber(tempInfo.getBusNum());
			tempBus.setCurrentStop(tempInfo.getCurrentStop());
			tempBus.setTimeRemaining(tempInfo.getTimeRemaining());
			tempBus.setAvailability(tempInfo.getAvailability());
			arrivingBusData.add(tempBus);
		}
		arrivingBusData.sort((a, b) -> Integer.compare(Integer.parseInt(a.getCurrentStop()), Integer.parseInt(b.getCurrentStop())));
	}
	
	//-------------------------------------data ������Ʈ �ϱ�-------------------------------------
	
	//busInfoList�� ������ �������� arrvingBus�� ������Ʈ �Ѵ�. createPage()���� ����Ѵ�.
	public void updateArrivingBusData() {
		
		BusInfo tempInfo;
		
		busInfoList.sort((a, b) -> Integer.compare(Integer.parseInt(a.getTimeRemaining()), Integer.parseInt(b.getTimeRemaining())));
		
		for(int i = 0; i < 6; i++) {
			
			tempInfo = busInfoList.get(i);
			arrivingBusData.get(i).setBusNumber(tempInfo.getBusNum());
			arrivingBusData.get(i).setCurrentStop(tempInfo.getCurrentStop());
			arrivingBusData.get(i).setTimeRemaining(tempInfo.getTimeRemaining());
			arrivingBusData.get(i).setAvailability(tempInfo.getAvailability());
		}
		
		arrivingBusData.sort((a, b) -> Integer.compare(Integer.parseInt(a.getCurrentStop()), Integer.parseInt(b.getCurrentStop())));

		for(int i = 0; i < 6; i++) {
        	System.out.println(arrivingBusData.get(i).getBusNumber()+"\t"+arrivingBusData.get(i).getAvailability());
        }
		
		System.out.println("-----------------------------------");

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
        box.setStyle("-fx-border-color: #C8C8C8;");
        int page = pageIndex * itemsPerPage();
        
        for (int i = page; i < page + itemsPerPage(); i++) {
 
        	final int click = i;
        	
        	HBox hbox = new HBox();
        	hbox.setStyle("-fx-background-color: #FFFFFF");
        	hbox.setAlignment(Pos.CENTER);
        	
        	if(i < busInfoList.size()) {
        		BusInfo tempBusStop = busInfoList.get(i);
                boolean flag = false;
                
        		Label busNum = new Label(tempBusStop.getBusNum()+" ��");
        		Label busTime = new Label(tempBusStop.getTimeRemaining()+" �� ��");
        		Label busStop = new Label(tempBusStop.getCurrentStop()+" ������ ��");
        		
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
                
                box.getChildren().add(hbox);
                
                if(busInfoList.get(i).getAvailability() != 0) {
                	hbox.setStyle("-fx-background-color:lightcoral");
                }
                
                hbox.setOnMouseClicked((e) -> {
    				hbox.setStyle("-fx-background-color:lightcoral");
    				busInfoList.get(click).setAvailability(1);
    				updateArrivingBusData();
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
	        
	        panelOverview.setTopAnchor(pagination, 510.0);
	        panelOverview.setRightAnchor(pagination, 80.0);
	        panelOverview.setLeftAnchor(pagination, 80.0);
	        panelOverview.getChildren().addAll(pagination);

	        controller.setMainApp(this);
	        
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//���̾�α� â�� ����.(������ ������ ����)
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
	
	//���� ���������� ��ȯ�Ѵ�
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) throws Exception {
		
		launch(args);
	}
}

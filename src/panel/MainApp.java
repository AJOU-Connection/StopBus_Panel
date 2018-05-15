package panel;

import java.awt.Panel;
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import panel.view.PanelOverviewController;
import panel.view.SettingDialogController;
import panel.model.ArrivingBus;
import panel.model.BusStop;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private BusStop busStop = new BusStop();
	
	//panel에 대한 observable 리스트
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	
	//생성자
	public MainApp() {
		addArrivingBusInfo();
		addBusStopInfo();
	}
	
	//도착하는 버스 정보 삽입
	public void addArrivingBusInfo() {
		arrivingBusData.add(new ArrivingBus("202", "1분 10초", "A"));
		arrivingBusData.add(new ArrivingBus("18", "3분 40초", "B"));
		arrivingBusData.add(new ArrivingBus("32", "2분 14초", "C"));
		arrivingBusData.add(new ArrivingBus("80", "1분 57초", "D"));
		arrivingBusData.add(new ArrivingBus("99-2", "3분 29초", "E"));
		arrivingBusData.add(new ArrivingBus("730", "4분 53초", "F"));
		arrivingBusData.add(new ArrivingBus("10", "4분 31초", "G"));
		arrivingBusData.add(new ArrivingBus("7001", "5분 56초", "H"));
	}
	
	//버스 정류장 정보 삽입
	public void addBusStopInfo() {
		//실제로 데이터 받아오는것 개발 후 그 이후에 수정할 것
		busStop.setBusStopNum(00000);
		busStop.setBusStopName("정류장 정보 없음");
		busStop.setBusStopInfo("방면 정보 없음");
	}
	
	public BusStop getBusStop() {
		return busStop;
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

	public static void main(String[] args) {
		launch(args);
	}
}

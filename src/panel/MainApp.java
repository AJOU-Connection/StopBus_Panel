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
	
	//panel�� ���� observable ����Ʈ
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	
	//������
	public MainApp() {
		addArrivingBusInfo();
		addBusStopInfo();
	}
	
	//�����ϴ� ���� ���� ����
	public void addArrivingBusInfo() {
		arrivingBusData.add(new ArrivingBus("202", "1�� 10��", "A"));
		arrivingBusData.add(new ArrivingBus("18", "3�� 40��", "B"));
		arrivingBusData.add(new ArrivingBus("32", "2�� 14��", "C"));
		arrivingBusData.add(new ArrivingBus("80", "1�� 57��", "D"));
		arrivingBusData.add(new ArrivingBus("99-2", "3�� 29��", "E"));
		arrivingBusData.add(new ArrivingBus("730", "4�� 53��", "F"));
		arrivingBusData.add(new ArrivingBus("10", "4�� 31��", "G"));
		arrivingBusData.add(new ArrivingBus("7001", "5�� 56��", "H"));
	}
	
	//���� ������ ���� ����
	public void addBusStopInfo() {
		//������ ������ �޾ƿ��°� ���� �� �� ���Ŀ� ������ ��
		busStop.setBusStopNum(999999);
		busStop.setBusStopName("������ ���� ����");
		busStop.setBusStopInfo("��� ���� ����");
	}
	
	public BusStop getBusStop() {
		return busStop;
	}
	
	
	//�����ϴ� ���� ������ ����Ʈ�κ��� ��������
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
	
	//���� ���̾ƿ� �ȿ� panel overview�� �����ش�
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
	
	//���� ���������� ��ȯ�Ѵ�
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}

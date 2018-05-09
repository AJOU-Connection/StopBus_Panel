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
import javafx.stage.Stage;
import panel.view.PanelOverviewController;
import panel.model.ArrivingBus;
import panel.model.BusStop;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	//panel�� ���� observable ����Ʈ
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	
	//������
	public MainApp() {
		arrivingBusData.add(new ArrivingBus("202", "1min", "A"));
		arrivingBusData.add(new ArrivingBus("18", "1min", "B"));
		arrivingBusData.add(new ArrivingBus("32", "2min", "C"));
		arrivingBusData.add(new ArrivingBus("80", "3min", "D"));
		arrivingBusData.add(new ArrivingBus("99-2", "4min", "E"));
		arrivingBusData.add(new ArrivingBus("730", "4min", "F"));
		arrivingBusData.add(new ArrivingBus("10", "4min", "G"));
		arrivingBusData.add(new ArrivingBus("7001", "4min", "H"));
	}
	
	public ObservableList<ArrivingBus> getPanelData(){
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
	
	//���� ���������� ��ȯ�Ѵ�
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}

package panel;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import panel.model.Model;
import panel.view.Controller;


public class MainApp extends Application {
	
	private Stage primaryStage;
	private AnchorPane rootLayout;
	
	private ObservableList<Model> busStopData = FXCollections.observableArrayList();
	
	public MainApp() {
		
	}
	
	@Override
	public void start(Stage primaryStage){
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("PanelApp");
		
		initRootLayout();
		showInfo();
	}
	
	public ObservableList<Model> getBusStopData(){
		return busStopData;
	}
	
	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayout.fxml"));
			rootLayout = (AnchorPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}	
	
	private void showInfo() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Info.fxml"));
			AnchorPane Info = (AnchorPane) loader.load();
			
			Controller controller = loader.getController();
			controller.setMainApp(this);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}
	public static void main(String[] args) {
		launch(args);
	}
}

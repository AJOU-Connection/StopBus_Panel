package panel;

import java.awt.Panel;
import java.io.IOException;
import java.io.InputStream;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import panel.view.PanelOverviewController;
import panel.view.SettingDialogController;
import panel.model.ArrivingBus;
import panel.model.BusStop;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.*;
import java.util.*;
import java.net.*;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	
	private BusStop busStop = new BusStop();
	
	private Pagination pagination;
    //String[] fonts = new String[]{};
 
    public int itemsPerPage() {
        return 10;
    }
 
    public VBox createPage(int pageIndex) {        
        VBox box = new VBox(5);
        int page = pageIndex * itemsPerPage();
        for (int i = page; i < page + itemsPerPage(); i++) {
            Label busList = new Label("Hi");
            busList.setFont(new Font("Arial", 20));
            box.getChildren().add(busList);
        }
        return box;
    }
	
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
	}
	
	//버스 정류장 정보 삽입
	public void addBusStopInfo() {
		//실제로 데이터 받아오는것 개발 후 그 이후에 수정할 것
		busStop.setBusStopNum(999999);
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
			
			
			//fonts = Font.getFamilies().toArray(fonts);
	        
	        pagination = new Pagination(43/itemsPerPage()+1, 0);
	        pagination.setStyle("-fx-background-color:white;");
	        pagination.setPageFactory(new Callback<Integer, Node>() {
	 
	            @Override
	            public Node call(Integer pageIndex) {          
	                return createPage(pageIndex);               
	            }
	        });
	        
	        panelOverview.setTopAnchor(pagination, 460.0);
	        panelOverview.setRightAnchor(pagination, 80.0);
	        panelOverview.setLeftAnchor(pagination, 80.0);
	        panelOverview.setBottomAnchor(pagination, 80.0);
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
	
	public static String excutePost(String targetURL, String urlParameters) {
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("Content-Type", 
	           "application/x-www-form-urlencoded");
				
	      connection.setRequestProperty("Content-Length", "" + 
	               Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (
	                  connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}

	public static void main(String[] args) throws Exception {
		
		//excutePost("http://stop-bus.tk/user/busArrival", urlParameters);
		
		URL url = new URL("http://stop-bus.tk/user/busArrival");
	    URLConnection conn = url.openConnection();
	    conn.setDoOutput(true);
	    OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

	    writer.write("districtCd=2&stationNumber=04237");
	    writer.flush();
	    String line;
	    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	    while ((line = reader.readLine()) != null) {
	      System.out.println(line);
	    }
	    writer.close();
	    reader.close();
	    
		launch(args);
	}
}

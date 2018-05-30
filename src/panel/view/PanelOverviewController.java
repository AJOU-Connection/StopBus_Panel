package panel.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import panel.MainApp;
import panel.model.ArrivingBus;
import panel.model.BusInfo;
import panel.model.BusStop;
import panel.util.SearchingStationUtil;
import panel.util.LanguageUtil;
import panel.util.ReservationUtil;

public class PanelOverviewController {

	@FXML
	private VBox arrivingBusBox = new VBox(5);
	
	@FXML
	private AnchorPane searchPane;

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
	private Label searchResult;
	@FXML
	private ScrollPane searchResultScroll;
	@FXML
	private VBox searchVBox;
	
	@FXML
	private VBox koreanKeyboard1;
	
	@FXML
	private VBox koreanKeyboard2;
	
	@FXML
	private VBox englishKeyboard1;
	
	@FXML
	private VBox englishKeyboard2;
	@FXML
	private BorderPane searchBorderPane;
	
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	//private List<BusStop> searchingList = new ArrayList<BusStop>();
	private MainApp mainApp;
	private boolean stop = false;
	private boolean searchFlag = false;
	private boolean languageFlag = false;
	private boolean shiftFlag = false;
	
	ReservationUtil reservationUtil = new ReservationUtil();
	
	public PanelOverviewController() {
		
	}
	
	//-----------------------------------------visibility 설정-----------------------------------------
	
	//키보드가 보이지 않게 한다 + 키보드 설정을 초기화 한다.
	@FXML
	private void setKeyboardUnvisible() {
		koreanKeyboard1.setVisible(false);
		koreanKeyboard2.setVisible(false);
		englishKeyboard1.setVisible(false);
		englishKeyboard2.setVisible(false);
		languageFlag = false;
		shiftFlag = false;
	}
	
	//키보드가 보이게 한다 + 키보드 설정을 초기화 한다.
	@FXML
	private void setKeyboardVisible() {
		koreanKeyboard1.setVisible(true);
		koreanKeyboard2.setVisible(false);
		englishKeyboard1.setVisible(false);
		englishKeyboard2.setVisible(false);
		languageFlag = false;
		shiftFlag = false;
	}
	
	@FXML
	private void setVisible() {
		searchPane.setVisible(true);
		mainApp.setPaginationUnvisible();
		searchFlag = true;
		searchPane.setVisible(true);
		searching.setStyle("-fx-background-color: #00838F");
		checking.setStyle("-fx-background-color: #00ACC1");
		setKeyboardVisible();
	}
	
	@FXML
	private void setUnvisible() {
		searchPane.setVisible(false);
		mainApp.setPaginationVisible();
		searchPane.setVisible(false);
		searchFlag = false;
		mainApp.updatePagination();
		searching.setStyle("-fx-background-color: #00ACC1");
		checking.setStyle("-fx-background-color: #00838F");
		searchResult.setText("검색어를 입력해주세요.");
		searchVBox.getChildren().clear();
		searchText.setText("");
		
	}
	//-----------------------------------------키보드 기능 구현-----------------------------------------
	//한글 키보드 기능(입력값을 초성, 중성, 종성으로 구분하여 문자로 치환)
	@FXML
	private void getKeyboardValue(ActionEvent event) {
		char input;
		char lastWord;
		String fullText = "";
		input = ((Button) event.getSource()).getText().charAt(0);
		
		LanguageUtil lan = new LanguageUtil();
		int cho = 0;
		int jung = 0;
		int jong = 0;
		String combinedWord = "";
		
		//이전에 입력된 text 값
		fullText += searchText.getText() + input;
		
		if(fullText.length()-1 > 0) {
			lastWord = fullText.charAt(fullText.length()-2);
			
			//모음이 입력되었을 때
			if((int)input >= 'ㅏ' && (int)input <= 'ㅣ') {
				//이전 문장이 자음 하나라면 자음+모음의 형태로 합친다
				if((int)lastWord >= 'ㄱ' && (int)lastWord <= 'ㅎ') {
					cho = lan.findChosung(lastWord);
					jung = lan.findJungsung(input);
					fullText = fullText.substring(0, fullText.length()-2);
					fullText += lan.combineWord(cho, jung, jong);
				}
				//직전 문자가 하나의 단어라면
				else if((int)lastWord >= 0xAC00 && (int)lastWord <= 0xD7A3) {
					
					cho = lan.getChosung(lastWord);
					jung = lan.getJungsung(lastWord);
					jong = lan.getJongsung(lastWord);
					
					//받침이 존재할 때 단어의 받침과 새로 입력된 모음을 합친다
					if(lan.getJongsung(lastWord) != 0) {
						fullText = fullText.substring(0, fullText.length()-2);
						fullText += lan.combineWord(cho, jung, 0);
						cho = lan.jong2cho(jong);
						jung = lan.findJungsung(input);
						fullText += lan.combineWord(cho, jung, 0);
					}
					//받침이 존재하지 않을 때 모음이 이중모음이 될 수 있는지 확인 후 변경한다.
					else{
						jung = lan.mergeVowels(jung, input);
						if(jung != 0) {
							fullText = fullText.substring(0, fullText.length()-2);
							fullText += lan.combineWord(cho, jung, 0);
						}
					}
				}
				else if((int)lastWord >= 'ㅏ' && (int)lastWord <= 'ㅣ') {
					jung = lan.mergeVowels(lan.findJungsung(lastWord), input);
					if(jung != 0) {
						fullText = fullText.substring(0, fullText.length()-2);
						fullText += lan.findVowelsChar(jung);
					}
				}
			}
			//자음이 입력되었다면
			else if((int)input >= 'ㄱ' && (int)input <= 'ㅎ'){
				
				//직전 문자가 하나의 단어이고 받침이 없을 대 단어의 받침이 된다.
				if((int)lastWord >= 0xAC00 && (int)lastWord <= 0xD7A3 && lan.getJongsung(lastWord) == 0) {
					cho = lan.getChosung(lastWord);
					jung = lan.getJungsung(lastWord);
					jong = lan.findJongsung(input);
					fullText = fullText.substring(0, fullText.length()-2);
					fullText += lan.combineWord(cho, jung, jong);
				}
			}
				
		}
		searchText.setText(fullText);
	}
	
	//키보드 space 기능
	@FXML
	private void getSpaceValue(ActionEvent event) {
		searchText.setText(searchText.getText() + " ");
	}
	
	//키보드 backspace 버튼 기능
	@FXML
	private void deleteKeyboardValue() {
		if(searchText.getText().length() > 0) {
			searchText.setText(searchText.getText().substring(0, searchText.getText().length()-1));
		}
	}
	
	//키보드 shift 버튼 기능
	@FXML
	private void shiftKeyboardValue() {
		if(languageFlag) {
			if(shiftFlag) {
				shiftFlag = false;
				koreanKeyboard1.setVisible(false);
				koreanKeyboard2.setVisible(false);
				englishKeyboard1.setVisible(true);
				englishKeyboard2.setVisible(false);	
			}
			else {
				shiftFlag = true;
				koreanKeyboard1.setVisible(false);
				koreanKeyboard2.setVisible(false);
				englishKeyboard1.setVisible(false);
				englishKeyboard2.setVisible(true);	
			}
			
		}
		else {
			if(shiftFlag) {
				shiftFlag = false;
				koreanKeyboard1.setVisible(true);
				koreanKeyboard2.setVisible(false);
				englishKeyboard1.setVisible(false);
				englishKeyboard2.setVisible(false);
			}
			else {
				shiftFlag = true;
				koreanKeyboard1.setVisible(false);
				koreanKeyboard2.setVisible(true);
				englishKeyboard1.setVisible(false);
				englishKeyboard2.setVisible(false);
			}
			
		}
	}
	
	//키보드 한/영 변환 버튼 기능
	@FXML
	private void changeKeyboardValue(ActionEvent event) {
		if(languageFlag == true) {
			languageFlag = false;
			shiftFlag = false;
			koreanKeyboard1.setVisible(true);
			koreanKeyboard2.setVisible(false);
			englishKeyboard1.setVisible(false);
			englishKeyboard2.setVisible(false);
		}
		else {
			languageFlag = true;
			shiftFlag = false;
			koreanKeyboard1.setVisible(false);
			koreanKeyboard2.setVisible(false);
			englishKeyboard1.setVisible(true);
			englishKeyboard2.setVisible(false);
		}
	}
	
	//-----------------------------------------searching 기능-----------------------------------------
	
	//키보드의 Enter나 검색창 옆의 검색 버튼을 눌렀을 때 검색 결과를 띄워준다.
	@FXML
	private void getSearchingText() {
		
		setKeyboardUnvisible();
		searchVBox.getChildren().clear();
		
		List<BusStop> searchingList = new ArrayList<BusStop>();
		String keyword = searchText.getText();
		String result = "";
		
		SearchingStationUtil searchingUtil = new SearchingStationUtil();
		
		//1. 사용자가 검색창에 버스 번호를 검색했을 때
		if(keyword.charAt(0) >= 48 && keyword.charAt(0) <= 57) {
			ObservableList<BusInfo> tempList = mainApp.getBusInfoListData();
			String routeID = "";
			String stationSeq = "";
			
			//사용자가 입력한 버스 번호가 현재 정류장을 지나는 버스 목록에 있는지 체크
			for(int i = 0; i < tempList.size(); i++) {
				if(tempList.get(i).getBusNum().equals(keyword)) {
					routeID += tempList.get(i).getRouteID();	//같은 번호인 차량의 routeID
					stationSeq += tempList.get(i).getStationSeq();	//같은 번호인 차량의 stationSeq
					break;
				}
			}
			
			searchingList = searchingUtil.getBusStationList(routeID, stationSeq);
			
			//1.1 사용자가 입력한 버스 번호에 대한 정보가 없을 때
			if(searchingList.size() == 0) {
				result = "검색 결과가 없습니다.";
			}
			//1.2 사용자가 입력한 버스 번호에 대한 정보가 있을 때
			else {
				result = "총 "+searchingList.size()+"개의 검색결과가 있습니다.";
				for(int i = 0; i < searchingList.size(); i++) {

					searchVBox.setSpacing(5);
					
					String stationNum = searchingList.get(i).getBusStopNum();
					String stationName = searchingList.get(i).getBusStopName();
					
					String btnText = "[" + stationNum + " ]\t" + stationName;
					
					Button resultBtn = new Button(btnText);
					resultBtn.setMinWidth(460);
					resultBtn.setMinHeight(50);
					resultBtn.setAlignment(Pos.BASELINE_LEFT);
					resultBtn.setStyle("-fx-background-color : white");
					
					resultBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
							new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent e) {
									resultBtn.setStyle("-fx-background-color: #F9F9F9");
								}
					});
					
					resultBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
							new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent e) {
									resultBtn.setStyle("-fx-background-color: white");
								}
					});
					
		    		searchVBox.getChildren().add(resultBtn);
				}
			}
		}
		
		//2. 사용자가 검색창에 정류장 이름을 검색했을 때
		else {
			searchingList= searchingUtil.searchingStation(keyword);
			
			//2.1 사용자가 입력한 정류장 이름에 대한 정보가 없을 때
			if(searchingList.size() == 0) {
				result = "검색 결과가 없습니다.";
			}
			//2.2 사용자가 입력한 정류장 이름에 대한 정보가 있을 때
			else {
				result = "총 "+searchingList.size()+"개의 검색결과가 있습니다.";
				for(int i = 0; i < searchingList.size(); i++) {
					
					final int secondSearchingSize;
					
					searchVBox.setSpacing(5);
					
					String stationNum = searchingList.get(i).getBusStopNum();
					String stationName = searchingList.get(i).getBusStopName();
					String destiStationID = searchingList.get(i).getStationID();
					
					String btnText = "[" + stationNum + " ]\t" + stationName;
					
					Button resultBtn = new Button(btnText);
					resultBtn.setMinWidth(460);
					resultBtn.setMinHeight(50);
					resultBtn.setAlignment(Pos.BASELINE_LEFT);
					resultBtn.setStyle("-fx-background-color : white");
					
					//2.2.1 정류장 목록을 클릭했을 때
					resultBtn.setOnAction((event) -> {
						
						//사용자가 입력한 정류장을 지나가는 버스가 있는지 검색 후 띄워준다
						List<BusInfo> secondSearching = searchingUtil.getIsgo(mainApp.getBusStop().getStationID(), destiStationID);
						showSecondSearchingResult(secondSearching);
					});
					
					resultBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
							new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent e) {
									resultBtn.setStyle("-fx-background-color: #F9F9F9");
								}
					});
					
					resultBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
							new EventHandler<MouseEvent>() {
								@Override
								public void handle(MouseEvent e) {
									resultBtn.setStyle("-fx-background-color: white");
								}
					});
					
		    		searchVBox.getChildren().add(resultBtn);
				}
			}
		}
		searchResult.setText(result);
	}
	
	//정류장 이름을 검색 후 검색 결과 중 하나를 클릭했을 때 2차 검색을 한다. 현재 정류장에서 클릭한 정류장까지 가는 버스를 검색하고 띄워준다. 
	private void showSecondSearchingResult(List<BusInfo> secondSearching) {
		searchVBox.getChildren().clear();
		for(int i = 0; i < secondSearching.size(); i++) {
			searchVBox.setSpacing(5);
			
			String routeID = secondSearching.get(i).getRouteID();
			String routeNumber = secondSearching.get(i).getBusNum();
			
			String btnText = "[ " + routeID + " ]\t" + routeNumber + "번";
			
			Button resultBtn = new Button(btnText);
			resultBtn.setMinWidth(460);
			resultBtn.setMinHeight(50);
			resultBtn.setAlignment(Pos.BASELINE_LEFT);
			resultBtn.setStyle("-fx-background-color : white");
			
			resultBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							resultBtn.setStyle("-fx-background-color: #F9F9F9");
						}
			});
			
			resultBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							resultBtn.setStyle("-fx-background-color: white");
						}
			});
    		searchVBox.getChildren().add(resultBtn);
		}
		if(secondSearching.size() == 0) {
			Button nullBtn = new Button("해당 정류장을 지나는 버스가 없습니다.");
			nullBtn.setMinWidth(460);
			nullBtn.setMinHeight(50);
			nullBtn.setAlignment(Pos.BASELINE_CENTER);
			nullBtn.setStyle("-fx-background-color : white");
			searchVBox.getChildren().add(nullBtn);
		}
		
	}
	
	//-----------------------------------------Arriving Bus Box 설정-----------------------------------------
	
	//초기 Arriving Bus Box를 생성한다.
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
	
	//Arriving Bus Box의 버스 목록을 클릭했을 때 클릭한 목록의 색을 변경하고 해당 버스가 예약되었음을 설정한다. 
	private void boxifyBoxes() {
		
		for(Node child : arrivingBusBox.getChildren()) {
			
			HBox hb = (HBox) child;
			
			hb.setOnMouseClicked((e) -> {
				
				int index = (int) hb.getLayoutY()/40;
				int busListIndex = getIndexInBusList(index);
				
				if(reservationUtil.postReservation(mainApp.getBusInfoListData().get(busListIndex).getRouteID(), mainApp.getBusStop().getStationID())){
					hb.setStyle("-fx-background-color:lightcoral");
					setAvailability((int) hb.getLayoutY());
					mainApp.updateBusInfoList(mainApp.getBusInfoListData());
					if(!searchFlag) {
						mainApp.updatePagination();
					}
				}
			});

		}
	}
	
	public int getIndexInBusList(int index) {
		
		int busListIndex = -1;
		String arrivingBusNum = arrivingBusData.get(index).getBusNumber();
		
		for(int i = 0; i < mainApp.getBusInfoListData().size(); i++) {
			if(arrivingBusNum.equals(mainApp.getBusInfoListData().get(i).getBusNum())) {
				busListIndex = i;
			}
		}
		
		return busListIndex;
	}
	
	//Arriving Bus Box의 버스 목록을 BusInfoList의 값을 바탕으로 업데이트 한다. 
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
				/*
				hb.addEventHandler(MouseEvent.MOUSE_ENTERED,
						new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent e) {
								hb.setStyle("-fx-background-color: #F9F9F9");
							}
				});
	    		
	    		hb.addEventHandler(MouseEvent.MOUSE_EXITED,
						new EventHandler<MouseEvent>() {
							@Override
							public void handle(MouseEvent e) {
								hb.setStyle("-fx-background-color: white");
							}
				});
				*/
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
	
	
	//-----------------------------------------mainApp에서 사용하는 setMainApp-----------------------------------------

	@FXML
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		busStopNumLabel.setText(mainApp.getBusStop().getBusStopNum());
		busStopNameLabel.setText(mainApp.getBusStop().getBusStopName());
		busStopInfoLabel.setText(mainApp.getBusStop().getBusStopInfo());
		arrivingBusData = mainApp.getArrivingBusData();
		createArrivingBusBox();
		boxifyBoxes();
		
		
		Thread thread = new Thread() {
			@Override
			public void run() {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				while(!stop) {
					String strTime = sdf.format(new Date());
					Platform.runLater(() -> {
						mainApp.updateBusInfoList(mainApp.getBusInfoListData());
						updateBoxes();
						if(!searchFlag) {
							mainApp.updatePagination();
						}
					});
					try { Thread.sleep(20000);} catch(InterruptedException e) {}
				}
			}
		};
		
		thread.setDaemon(true);
		thread.start();
		
	}

}

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
	private Button refreshBtn;
	
	private ObservableList<ArrivingBus> arrivingBusData = FXCollections.observableArrayList();
	//private List<BusStop> searchingList = new ArrayList<BusStop>();
	private MainApp mainApp;
	private boolean stop = false;
	private boolean searchFlag = false;
	private boolean languageFlag = false;
	private boolean shiftFlag = false;
	
	public PanelOverviewController() {
		
	}
	
	@FXML
	private void setKeyboardUnvisible() {
		koreanKeyboard1.setVisible(false);
		koreanKeyboard2.setVisible(false);
		englishKeyboard1.setVisible(false);
		englishKeyboard2.setVisible(false);
		languageFlag = false;
		shiftFlag = false;
	}
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
	private void getSearchingText() {
		
		setKeyboardUnvisible();
		searchVBox.getChildren().clear();
		
		List<BusStop> searchingList = new ArrayList<BusStop>();
		String keyword = searchText.getText();
		String result = "";
		SearchingStationUtil searchingUtil = new SearchingStationUtil();
		searchingList= searchingUtil.searchingText(keyword);
		//searchingList = searchingUtil.getSearchingList();
		
		if(searchingList.size() == 0) {
			result += "검색 결과가 없습니다.";
		}
		else {
			for(int i = 0; i < searchingList.size(); i++) {
				final int click = i;
				
				searchVBox.setSpacing(5);
				
				String stationNum = searchingList.get(i).getBusStopNum();
				String stationName = searchingList.get(i).getBusStopName();
				
				String btnText = "[" + stationNum + " ]\t" + stationName;
				
				Button resultBtn = new Button(btnText);
				resultBtn.setMinWidth(460);
				resultBtn.setMinHeight(50);
				resultBtn.setAlignment(Pos.BASELINE_LEFT);
				resultBtn.setStyle("-fx-background-color : white");
				
				resultBtn.setOnAction((event) -> {
					//여기를 2차 검색으로 넘어가게 잘 해봐야함...
					System.out.println(click);
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
			result += "총 "+searchingList.size()+"개의 검색결과가 있습니다.";
		}
		searchResult.setText(result);
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
		searchResult.setText("검색어를 입력해주세요");
		searchVBox.getChildren().clear();
		searchText.setText("");
		
	}
	
	@FXML
	private void initialize() {
		
	}
	
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
    		
    		hbox.addEventHandler(MouseEvent.MOUSE_ENTERED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							hbox.setStyle("-fx-background-color: #F9F9F9");
						}
			});
    		
    		hbox.addEventHandler(MouseEvent.MOUSE_EXITED,
					new EventHandler<MouseEvent>() {
						@Override
						public void handle(MouseEvent e) {
							hbox.setStyle("-fx-background-color: white");
						}
			});
            
    		arrivingBusBox.getChildren().add(hbox);
        }
	}
	
	private void boxifyBoxes() {
		
		for(Node child : arrivingBusBox.getChildren()) {
			
			HBox hb = (HBox) child;
			hb.setOnMouseClicked((e) -> {
				hb.setStyle("-fx-background-color:lightcoral");
				setAvailability((int) hb.getLayoutY());
				mainApp.updateBusInfoList(mainApp.getBusInfoListData());
				if(!searchFlag) {
					mainApp.updatePagination();
				}
			});

		}
	}
	//문제가 심각함
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
	
	@FXML
	private void getSpaceValue(ActionEvent event) {
		searchText.setText(searchText.getText() + " ");
	}
	
	@FXML
	private void deleteKeyboardValue() {
		if(searchText.getText().length() > 0) {
			searchText.setText(searchText.getText().substring(0, searchText.getText().length()-1));
		}
	}
	
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
	

	@FXML
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		busStopNumLabel.setText(mainApp.getBusStop().getBusStopNum());
		busStopNameLabel.setText(mainApp.getBusStop().getBusStopName());
		busStopInfoLabel.setText(mainApp.getBusStop().getBusStopInfo());
		arrivingBusData = mainApp.getArrivingBusData();
		createArrivingBusBox();
		boxifyBoxes();
		
		/*
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
		*/
	}

}

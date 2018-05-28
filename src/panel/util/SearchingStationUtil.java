package panel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import panel.model.BusStop;

public class SearchingStationUtil {
	
	//private List<BusStop> searchingList = new ArrayList<BusStop>();

	public BusStop searchingBusStop(String keyword){
		
		String result = "";
		BusStop tempBusStop = new BusStop();
		
		try {
			String targetURL = "http://stop-bus.tk/user/search?type=station";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입

			// 데이터
			String param = "{\"keyword\":\""+keyword+"\"}";

			// 전송
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(param);
			osw.flush();

			// 응답
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				result += line;
			}
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
			JSONArray array = (JSONArray) jsonObj.get("body");
			
			if(array != null) {
				for(int i = 0; i < array.size(); i++) {
					JSONObject tempObj = (JSONObject) array.get(i);
					tempBusStop.setBusStopNum(String.valueOf(tempObj.get("stationNumber")));
					tempBusStop.setBusStopName(String.valueOf(tempObj.get("stationName")));
					tempBusStop.setBusStopInfo(String.valueOf(tempObj.get("stationDirect")));
				}
			}
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tempBusStop;
	}
	
	public List<BusStop> searchingText(String keyword){
		
		String result = "";
		List<BusStop> searchingResult = new ArrayList<BusStop>();
		
		try {
			String targetURL = "http://stop-bus.tk/user/search?type=station";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입

			// 데이터
			String param = "{\"keyword\":\""+keyword+"\"}";

			// 전송
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(param);
			osw.flush();

			// 응답
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				result += line;
			}
			
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
			JSONArray array = (JSONArray) jsonObj.get("body");
			
			if(array != null) {
				for(int i = 0; i < array.size(); i++) {
					JSONObject tempObj = (JSONObject) array.get(i);
					BusStop tempBusStop = new BusStop();
					tempBusStop.setBusStopNum(String.valueOf(tempObj.get("stationNumber")));
					tempBusStop.setBusStopName(String.valueOf(tempObj.get("stationName")));
					tempBusStop.setBusStopInfo(String.valueOf(tempObj.get("stationDirect")));
					searchingResult.add(tempBusStop);
				}
			}
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return searchingResult;
	}
	/*
	public BusStop getBusStop() {
		busStop.setBusStopNum(searchingList.get(0).getBusStopNum());
		busStop.setBusStopName(searchingList.get(0).getBusStopName());
		busStop.setBusStopInfo(searchingList.get(0).getBusStopInfo());
		return busStop;
	}
	*/
}

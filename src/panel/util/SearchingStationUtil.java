package panel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import panel.model.BusInfo;
import panel.model.BusStop;

public class SearchingStationUtil {

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
			
			for(int i = 0; i < array.size(); i++) {
				JSONObject tempObj = (JSONObject) array.get(i);
				tempBusStop.setBusStopNum(String.valueOf(tempObj.get("stationNumber")));
				tempBusStop.setBusStopName(String.valueOf(tempObj.get("stationName")));
				tempBusStop.setBusStopInfo(String.valueOf(tempObj.get("stationDirect")));
				tempBusStop.setStationID(String.valueOf(tempObj.get("stationID")));
			}

			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return tempBusStop;
	}
	
	public List<BusStop> searchingStation(String keyword){
		
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
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
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
					tempBusStop.setStationID(String.valueOf(tempObj.get("stationID")));
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
	
	public List<BusStop> getBusStationList(String routeID, String stationSeq){
		
		String result = "";
		
		List<BusStop> searchingResult = new ArrayList<BusStop>();
		
		try {
			String targetURL = "http://stop-bus.tk/user/busStationList";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입
			
			// 데이터
			String param = "{\"routeID\":\""+routeID+"\"}";
			
			
			// 전송
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
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
					
					if(Integer.parseInt(String.valueOf(tempObj.get("stationSeq"))) >= Integer.parseInt(stationSeq)) {
						tempBusStop.setBusStopNum(String.valueOf(tempObj.get("stationNumber")));
						tempBusStop.setBusStopName(String.valueOf(tempObj.get("stationName")));
						
						searchingResult.add(tempBusStop);
					}
				}
			}
			
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return searchingResult;
	}
	
	public List<BusInfo> getIsgo(String sourceStationID, String destiStationID){
		
		String result = "";
		
		List<BusInfo> searchingResult = new ArrayList<BusInfo>();
		
		try {
			String targetURL = "http://stop-bus.tk/user/isgo";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입
			
			// 데이터
			String param = "{\"sourceStationID\":\""+sourceStationID+"\", \"destiStationID\":\""+ destiStationID +"\"}";
			
			
			// 전송
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
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
					BusInfo tempBusInfo = new BusInfo();
					
					tempBusInfo.setBusNum(String.valueOf(tempObj.get("routeNumber")));
					tempBusInfo.setRouteID(String.valueOf(tempObj.get("routeID")));
					
					searchingResult.add(tempBusInfo);
				}
			}
			
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return searchingResult;
	}
}

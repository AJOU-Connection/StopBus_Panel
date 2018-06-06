package panel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import panel.model.BusInfo;

public class BusInfoUtil {

	//private List<BusInfo> busInfoList = new ArrayList<BusInfo>();
	
	private ObservableList<BusInfo> busInfoList = FXCollections.observableArrayList();
	
	public String setBusInfo(String stationID){
		String result = "";
		
		try {
			String targetURL = "http://stop-bus.tk/user/busArrival";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입

			// 데이터
			String param = "{\"stationID\":\"" + stationID + "\"}";

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
			
			for(int i=0; i<array.size(); i++) {
				JSONObject tempObj = (JSONObject) array.get(i);
				BusInfo tempBusInfo = new BusInfo();
				tempBusInfo.setBusNum(String.valueOf(tempObj.get("routeNumber")));
				tempBusInfo.setTimeRemaining(String.valueOf(tempObj.get("predictTime1")));
				tempBusInfo.setCurrentStop(String.valueOf(tempObj.get("locationNo1")));
				tempBusInfo.setPlateNum(String.valueOf(tempObj.get("plateNo1")));
				tempBusInfo.setStationSeq(String.valueOf(tempObj.get("stationSeq")));
				tempBusInfo.setRouteID(String.valueOf(tempObj.get("routeID")));
				if(Integer.parseInt(String.valueOf(tempObj.get("predictTime1"))) < 0) {
					tempBusInfo.setTimeRemaining("9999");
					tempBusInfo.setCurrentStop("9999");
				}
				busInfoList.add(tempBusInfo);
			}

			// 닫기
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public String updateBusInfo(String stationID, ObservableList<BusInfo> updateBusList){
		String result = "";
		
		try {
			String targetURL = "http://stop-bus.tk/user/busArrival";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입
			
			// 데이터
			String param = "{\"stationID\":\"" + stationID + "\"}";

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
			
			for(int i=0; i<array.size(); i++) {
				JSONObject tempObj = (JSONObject) array.get(i);
				BusInfo tempBusInfo = new BusInfo();
				for(int j = 0; j < updateBusList.size(); j++) {
					if(updateBusList.get(j).getBusNum().equals(String.valueOf(tempObj.get("routeNumber")))) {
						
						if(updateBusList.get(j).getPlateNum() == null || !updateBusList.get(j).getPlateNum().equals(String.valueOf(tempObj.get("plateNo1")))) {
							updateBusList.get(j).setAvailability(0);
						}
						if(Integer.parseInt(String.valueOf(tempObj.get("predictTime1"))) < 0) {
							updateBusList.get(j).setTimeRemaining("9999");
							updateBusList.get(j).setCurrentStop("9999");
							updateBusList.get(j).setPlateNum(null);
						}
						else {
							updateBusList.get(j).setTimeRemaining(String.valueOf(tempObj.get("predictTime1")));
							updateBusList.get(j).setCurrentStop(String.valueOf(tempObj.get("locationNo1")));
							updateBusList.get(j).setPlateNum(String.valueOf(tempObj.get("plateNo1")));
						}
						
					}
				}
				
			}
			// 닫기
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public ObservableList<BusInfo> getBusInfoList(){
		return busInfoList;
	}
}

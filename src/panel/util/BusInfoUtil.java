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

import panel.model.BusInfo;

public class BusInfoUtil {

	private List<BusInfo> busStopList = new ArrayList<BusInfo>();
	
	public String getArrivalTimes(String stationNumber){
		String result = "";
		
		try {
			String targetURL = "http://stop-bus.tk/user/busArrival";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // 보내는 타입

			// 데이터
			String param = "{\"districtCd\":2, \"stationNumber\":\"" + stationNumber + "\"}";

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
				//System.out.println("routeNumber");
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
				busStopList.add(tempBusInfo);
			}
			//System.out.println(result);
			// 닫기
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public List<BusInfo> getBusInfoList(){
		return busStopList;
	}
}

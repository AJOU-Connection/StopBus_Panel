package panel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import panel.model.BusStop;

public class BusStopUtil {
	
	BusStop busStop = new BusStop();

	public String setBusStop(String keyword){
		String result = "";
		
		
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
			
			for(int i=0; i<array.size(); i++) {
				JSONObject tempObj = (JSONObject) array.get(i);
				busStop.setBusStopNum(String.valueOf(tempObj.get("stationNumber")));
				busStop.setBusStopName(String.valueOf(tempObj.get("stationName")));
				busStop.setBusStopInfo(String.valueOf(tempObj.get("stationDirect")));
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
	public BusStop getBusStop() {
		return busStop;
	}
}

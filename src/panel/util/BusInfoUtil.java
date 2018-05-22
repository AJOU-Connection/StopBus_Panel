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
import panel.model.BusInfo;

public class BusInfoUtil {

	//private List<BusInfo> busInfoList = new ArrayList<BusInfo>();
	
	private ObservableList<BusInfo> busInfoList = FXCollections.observableArrayList();
	
	public String setBusInfo(String stationNumber){
		String result = "";
		
		try {
			String targetURL = "http://stop-bus.tk/user/busArrival";
			URL url = new URL(targetURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setDoOutput(true);
			conn.setRequestMethod("POST"); // ������ Ÿ��

			// ������
			String param = "{\"districtCd\":2, \"stationNumber\":\"" + stationNumber + "\"}";

			// ����
			OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
			osw.write(param);
			osw.flush();

			// ����
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
				busInfoList.add(tempBusInfo);
			}

			// �ݱ�
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

package panel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetDataExample {
	public String getArrivalTimes(String stationNumber){
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

			System.out.println(result);
			// �ݱ�
			osw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
}

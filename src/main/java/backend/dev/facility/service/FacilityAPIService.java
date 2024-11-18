package backend.dev.facility.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FacilityAPIService {

    private static final String API_BASE_URL = "http://openapi.seoul.go.kr:8088";
    private static final String API_KEY = "64777143706d617239394359504d52"; // 실제 API 키로 교체하세요

    public String fetchSportFacilityData(String facilityName) {
        try {
            StringBuilder urlBuilder = new StringBuilder(API_BASE_URL);
            urlBuilder.append("/").append(URLEncoder.encode(API_KEY, "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("ListPublicReservationSport", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("400", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode(facilityName, "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            return sb.toString(); // JSON 형식으로 반환

        } catch (IOException e) {
            e.printStackTrace();
            return "API 요청 중 오류 발생";
        }
    }
}
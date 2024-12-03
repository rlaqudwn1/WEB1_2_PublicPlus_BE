package backend.dev.facility.service;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${FACILITY_URL}")
    private String API_BASE_URL; // static 제거

    @Value("${FACILITY_API_KEY}")
    private String API_KEY; // static 제거

    // API 호출 메서드
    public String fetchSportFacilityData(String facilityName) {
        try {
            StringBuilder urlBuilder = new StringBuilder(API_BASE_URL);
            urlBuilder.append("/").append(URLEncoder.encode(API_KEY, "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("json", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("ListPublicReservationSport", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("1", "UTF-8"));
            urlBuilder.append("/").append(URLEncoder.encode("312", "UTF-8"));
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

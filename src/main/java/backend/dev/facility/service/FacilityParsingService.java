package backend.dev.facility.service;

import backend.dev.facility.entity.Facility;
import backend.dev.facility.entity.FacilityCategory;
import backend.dev.facility.entity.FacilityDetails;
import backend.dev.facility.entity.Point;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class FacilityParsingService {
    private final ObjectMapper objectMapper;

    public Facility parseFacility(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityNode = rootNode.path("ListPublicReservationSport").path("row").get(0);

            return Facility.builder()
                    .facilityId(facilityNode.path("SVCID").asText())    //아이디
                    .facilityCategory(FacilityCategory.fromName(facilityNode.path("MINCLASSNM").asText())) // 카테고리 파싱
                    .area(facilityNode.path("AREANM").asText()) // 지역명 ex 강남구
                    .location(new Point(facilityNode.path("X").asDouble(), facilityNode.path("Y").asDouble())) // 시설 좌표
                    .facilityName(facilityNode.path("SVCNM").asText()) // 시설이름
                    .facilityImage(facilityNode.path("IMGURL").asText()) // 이미지 URL
                    .priceType(facilityNode.path("PAYATNM").asText().equals("유료")) //결제 방식 유료/무료
                    .reservationStartDate(dateParser(facilityNode.path("RCPTBGNDT").asText()))// 접수 시작일
                    .reservationEndDate(dateParser(facilityNode.path("RCPTENDDT").asText())) // 접수 마감일
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
    public FacilityDetails parseFacilityDetails(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityNode = rootNode.path("ListPublicReservationSport").path("row").get(0);

            return FacilityDetails.builder()
                    .facilityId(facilityNode.path("SVCID").asText())    //아이디
                    .facilityCategory(FacilityCategory.fromName(facilityNode.path("MINCLASSNM").asText())) // 카테고리 파싱
                    .area(facilityNode.path("AREANM").asText()) // 지역명 ex 강남구
                    .location(new Point(facilityNode.path("X").asDouble(), facilityNode.path("Y").asDouble())) // 시설 좌표
                    .facilityDescription(facilityNode.path("DTLCONT").asText())
                    .facilityName(facilityNode.path("SVCNM").asText()) // 시설이름
                    .facilityImage(facilityNode.path("IMGURL").asText()) // 이미지 URL
                    .priceType(facilityNode.path("PAYATNM").asText().equals("유료")) //결제 방식 유료/무료
                    .reservationStartDate(dateParser(facilityNode.path("RCPTBGNDT").asText()))// 접수 시작일
                    .reservationEndDate(dateParser(facilityNode.path("RCPTENDDT").asText())) // 접수 마감일
                    .facilityNumber(facilityNode.path("TELNO").asText())
                    .reservationURL(facilityNode.path("SVCURL").asText())
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }
    public LocalDateTime dateParser(String jsonResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime date = LocalDateTime.parse(jsonResponse, formatter);
        return date;
    }


}

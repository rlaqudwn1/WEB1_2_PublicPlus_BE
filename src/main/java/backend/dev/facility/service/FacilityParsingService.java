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
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityParsingService {
    private final ObjectMapper objectMapper;


    // 공통 빌드 메서드
    private Facility.FacilityBuilder buildFacilityCommon(JsonNode facilityNode) {
        return Facility.builder()
                .facilityId(facilityNode.path("SVCID").asText())    // 아이디
                .facilityCategory(FacilityCategory.fromName(facilityNode.path("MINCLASSNM").asText())) // 카테고리
                .area(facilityNode.path("AREANM").asText()) // 지역명
                .location(new Point(facilityNode.path("X").asDouble(), facilityNode.path("Y").asDouble())) // 좌표
                .facilityName(facilityNode.path("SVCNM").asText()) // 시설 이름
                .facilityImage(facilityNode.path("IMGURL").asText()) // 이미지 URL
                .priceType(facilityNode.path("PAYATNM").asText().equals("유료")) // 결제 방식
                .reservationStartDate(dateParser(facilityNode.path("RCPTBGNDT").asText())) // 시작일
                .reservationEndDate(dateParser(facilityNode.path("RCPTENDDT").asText())); // 마감일
    }

    private FacilityDetails.FacilityDetailsBuilder buildFacilityDetailsCommon(JsonNode facilityNode) {
        return FacilityDetails.builder()
                .facilityId(facilityNode.path("SVCID").asText())    // 아이디
                .facilityCategory(FacilityCategory.fromName(facilityNode.path("MINCLASSNM").asText())) // 카테고리
                .area(facilityNode.path("AREANM").asText()) // 지역명
                .location(new Point(facilityNode.path("X").asDouble(), facilityNode.path("Y").asDouble())) // 좌표
                .facilityLocation(facilityNode.path("PLACENM").asText())
                .facilityDescription(facilityNode.path("DTLCONT").asText()) // 설명
                .facilityName(facilityNode.path("SVCNM").asText()) // 시설 이름
                .facilityImage(facilityNode.path("IMGURL").asText()) // 이미지 URL
                .priceType(facilityNode.path("PAYATNM").asText().equals("유료")) // 결제 방식
                .reservationStartDate(dateParser(facilityNode.path("RCPTBGNDT").asText())) // 시작일
                .reservationEndDate(dateParser(facilityNode.path("RCPTENDDT").asText())) // 마감일
                .facilityNumber(facilityNode.path("TELNO").asText()) // 전화번호
                .reservationURL(facilityNode.path("SVCURL").asText()); // 예약 URL
    }

    public Facility parseFacility(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityNode = rootNode.path("ListPublicReservationSport").path("row").get(0); // 첫 번째 데이터만 처리

            return buildFacilityCommon(facilityNode).build(); // 공통 빌더 사용
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    public List<Facility> parseFacilities(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityArrayNode = rootNode.path("ListPublicReservationSport").path("row");

            List<Facility> facilities = new ArrayList<>();
            if (facilityArrayNode.isArray()) {
                for (JsonNode facilityNode : facilityArrayNode) {
                    facilities.add(buildFacilityCommon(facilityNode).build()); // 공통 빌더 사용
                }
            }
            return facilities;
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    public FacilityDetails parseFacilityDetails(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityNode = rootNode.path("ListPublicReservationSport").path("row").get(0); // 첫 번째 데이터만 처리

            return buildFacilityDetailsCommon(facilityNode).build(); // 공통 빌더 사용
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    public List<FacilityDetails> parseFacilityDetailsList(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityArrayNode = rootNode.path("ListPublicReservationSport").path("row");

            List<FacilityDetails> facilityDetailsList = new ArrayList<>();
            if (facilityArrayNode.isArray()) {
                for (JsonNode facilityNode : facilityArrayNode) {
                    facilityDetailsList.add(buildFacilityDetailsCommon(facilityNode).build()); // 공통 빌더 사용
                }
            }
            return facilityDetailsList;
        } catch (Exception e) {
            throw new RuntimeException("JSON 파싱 실패", e);
        }
    }

    public LocalDateTime dateParser(String jsonDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        return LocalDateTime.parse(jsonDate, formatter);
    }
}

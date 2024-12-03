package backend.dev.facility.service;
import backend.dev.facility.entity.*;
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
    private FacilityDetails buildFacilityDetailsCommon(JsonNode facilityNode) {

        return FacilityDetails.builder()  // 자동으로 빌더를 사용할 수 있습니다
                .facilityId(facilityNode.path("SVCID").asText())
                .facilityCategory(FacilityCategory.fromName(facilityNode.path("MINCLASSNM").asText()))
                .area(facilityNode.path("AREANM").asText())
                .longitude((facilityNode.path("X").asDouble()))
                .latitude((facilityNode.path("Y").asDouble()))
                .facilityLocation(facilityNode.path("PLACENM").asText())
                .facilityDescription(facilityNode.path("DTLCONT").asText())
                .facilityName(facilityNode.path("SVCNM").asText())
                .facilityImage(facilityNode.path("IMGURL").asText())
                .priceType(facilityNode.path("PAYATNM").asText().equals("유료"))
                .reservationStartDate(dateParser(facilityNode.path("RCPTBGNDT").asText()))
                .reservationEndDate(dateParser(facilityNode.path("RCPTENDDT").asText()))
                .serviceStartDate((facilityNode.path("V_MIN").asText()))
                .serviceEndDate((facilityNode.path("V_MAX").asText()))
                .facilityNumber(facilityNode.path("TELNO").asText())
                .reservationURL(facilityNode.path("SVCURL").asText())
                .build();  // .build()를 호출해 빌더 객체를 반환합니다
    }

    public FacilityDetails parseFacilityDetails(String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode facilityNode = rootNode.path("ListPublicReservationSport").path("row").get(0); // 첫 번째 데이터만 처리

            return buildFacilityDetailsCommon(facilityNode); // 공통 빌더 사용
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
                    facilityDetailsList.add(buildFacilityDetailsCommon(facilityNode)); // 공통 빌더 사용
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
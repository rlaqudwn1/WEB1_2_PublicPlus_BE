package backend.dev.googlecalendar.service;

import backend.dev.activity.dto.ActivityCreateDTO;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.dto.ActivityUpdateDTO;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import backend.dev.user.repository.UserRepository;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EventService {
    private final GoogleCalendarService googleCalendarService;
    private final UserRepository userRepository;
    private String timeZone = "Asia/Seoul";

    public void getEvent(String eventId) {
        try {
            Calendar service = googleCalendarService.getCalendarService();
            Event event = service.events().get("d053961b19cc4adcd817f8e8603e40704d06a54669cadc3c7ba0134b566064d7@group.calendar.google.com", eventId).execute();
            Event.ExtendedProperties extendedProperties = event.getExtendedProperties();
            Object maxAttendees = extendedProperties.get("maxAttendees");
            System.out.println(extendedProperties.get("maxAttendees"));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ActivityCreateDTO createEvent(ActivityCreateDTO activityCreateDTO) {
        try {
            Calendar service = googleCalendarService.getCalendarService();

            Event event = new Event()
                    .setSummary(activityCreateDTO.getTitle())
                    .setDescription(activityCreateDTO.getDescription())
                    .setStart(new EventDateTime()
                            .setDateTime(new DateTime(activityCreateDTO.getStartTime()))
                            .setTimeZone(timeZone))
                    .setEnd(new EventDateTime()
                            .setDateTime(new DateTime(activityCreateDTO.getEndTime()))
                            .setTimeZone(timeZone))
                    .setLocation(activityCreateDTO.getLocation());
            // Extended Properties에 최대 참석자 수 및 참석자 목록 추가
            event.setExtendedProperties(new Event.ExtendedProperties().setPrivate(
                    Map.of(
                            "maxAttendees", (String.valueOf(activityCreateDTO.getMaxAttendees()))
//                            "attendees", String.join(",", activityCreateDTO.getAttendees())
                    )
            ));
            Event createdEvent = service.events().insert(activityCreateDTO.getGoogleCalenderId(), event).execute();
            activityCreateDTO.setEventId(createdEvent.getId());
            return activityCreateDTO;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public String updateEvent(ActivityUpdateDTO updateDTO) {
        try{
            Calendar service = googleCalendarService.getCalendarService();
            userRepository.findByEvent

            // 기존 이벤트 가져오기
            Event event = service.events().get(updateDTO.getGoogleCalenderId(), updateDTO.getEventId()).execute();

            // 이벤트 제목 (summary) 업데이트
            event.setSummary(updateDTO.getTitle());
            event.setLocation(updateDTO.getLocation());
            event.setDescription(updateDTO.getDescription());
            event.setStart(new EventDateTime()
                    .setDateTime(new DateTime(updateDTO.getStartTime()))
                    .setTimeZone(timeZone));
            event.setEnd(new EventDateTime()
                    .setDateTime(new DateTime(updateDTO.getEndTime()))
                    .setTimeZone(timeZone));

            // 최대 참석 인원수 추가
            Event.ExtendedProperties extendedProperties = event.getExtendedProperties();

            // 기존 extended properties가 없을 경우 새로 생성
            if (extendedProperties == null) {
                extendedProperties = new Event.ExtendedProperties();
                event.setExtendedProperties(extendedProperties);
            }

            // private 속성 가져오기 없으면 생성
            Map<String, String> privateProperties = extendedProperties.getPrivate();
            if (privateProperties == null) {
                privateProperties = new java.util.HashMap<>();
            }

            // maxAttendees 속성 추가 또는 수정
            privateProperties.put("maxAttendees", String.valueOf(updateDTO.getMaxAttendees()));
            extendedProperties.setPrivate(privateProperties);

            // Google Calendar에 업데이트된 이벤트 저장
            Event updatedEvent = service.events().update(updateDTO.getGoogleCalenderId(), event.getId(), event).execute();

            // 업데이트된 시간 출력 및 반환
            System.out.println(updatedEvent.getUpdated());
            return String.valueOf(updatedEvent.getUpdated());
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteEvent(String eventId) {
        try {
            Calendar service = googleCalendarService.getCalendarService();
            service.events().delete("c6ea861598541c968d8506a1b5169d1204436c0da251870bbe8594792ec1b6e3@group.calendar.google.com","434dhpcu6oe48fv0snc3le5uqg").execute();// 유저 정보에서 캘린더를 받아오고 eventId를 선택
            return "Event deleted";
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

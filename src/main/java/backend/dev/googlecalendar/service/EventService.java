package backend.dev.googlecalendar.service;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.mapper.ActivityMapper;
import backend.dev.googlecalendar.setting.GoogleCalendarService;
import backend.dev.user.repository.UserRepository;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
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

    public String createEvent(ActivityRequestDTO dto, String googleCalendarId) {
        try {
            Calendar service = googleCalendarService.getCalendarService();

            Event event = new Event()
                    .setSummary(dto.title())
                    .setDescription(dto.description())
                    .setStart(ActivityMapper.parseLocalDateTime(dto.startTime()))
                    .setEnd(ActivityMapper.parseLocalDateTime(dto.endTime()))
                    .setLocation(dto.location());
            // Extended Properties에 최대 참석자 수 및 참석자 목록 추가
            event.setExtendedProperties(new Event.ExtendedProperties().setPrivate(
                    Map.of(
                            "maxParticipants", (String.valueOf(dto.maxParticipants()))
//                            "attendees", String.join(",", dto.getAttendees())
                    )
            ));
            Event createdEvent = service.events().insert(googleCalendarId, event).execute();
            return createdEvent.getId();
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public String updateEvent(ActivityRequestDTO updateDTO, String eventId, String googleCalendarId) {
        try {
            log.info("업데이트 이벤트 확인: " + updateDTO.title());
            Calendar service = googleCalendarService.getCalendarService();

            // 기존 이벤트 가져오기
            Event event = service.events().get(googleCalendarId, eventId).execute();

            // 이벤트 업데이트
            event.setSummary(updateDTO.title());
            event.setLocation(updateDTO.location());
            event.setDescription(updateDTO.description());
            event.setStart(ActivityMapper.parseLocalDateTime(updateDTO.startTime()));
            event.setEnd(ActivityMapper.parseLocalDateTime(updateDTO.endTime())
                    .setTimeZone(timeZone));

            // 최대 참석 인원수 추가 (extendedProperties)
            Event.ExtendedProperties extendedProperties = event.getExtendedProperties();
            if (extendedProperties == null) {
                extendedProperties = new Event.ExtendedProperties();
            }
            Map<String, String> privateProperties = extendedProperties.getPrivate();
            if (privateProperties == null) {
                privateProperties = new java.util.HashMap<>();
            }
            privateProperties.put("maxParticipants", String.valueOf(updateDTO.maxParticipants()));
            extendedProperties.setPrivate(privateProperties);
            event.setExtendedProperties(extendedProperties);

            log.info("Extended properties: " + privateProperties);

            // Google Calendar에 업데이트된 이벤트 저장
            Event updatedEvent = service.events().update(googleCalendarId, event.getId(), event).execute();

            // 업데이트된 시간 반환
            log.info("이벤트 업데이트 성공: " + updatedEvent.getUpdated());
            Event.ExtendedProperties extendedProperties1 = updatedEvent.getExtendedProperties();
            if (extendedProperties1 != null && extendedProperties1.getPrivate() != null) {
                String maxAttendees = extendedProperties1.getPrivate().get("maxAttendees");
                log.info("maxAttendees: " + maxAttendees);
            } else {
                log.warn("Extended properties or private properties are null");
            }
            return updatedEvent.getUpdated().toString();
        } catch (Exception e) {
            log.error("이벤트 업데이트 실패", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Event update failed", e);
        }
    }


    public String deleteEvent(String eventId, String googleCalendarId) {
        try {
            Calendar service = googleCalendarService.getCalendarService();
            service.events().delete(googleCalendarId,eventId).execute();// 유저 정보에서 캘린더를 받아오고 eventId를 선택
            return "Event deleted";
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public String listEvents(String googleCalendarId) {
        try {
            Calendar calendarService = googleCalendarService.getCalendarService();
            Events events = calendarService.events().list(googleCalendarId).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                System.out.println(event.getSummary());
            }
            return "Str";
        }catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

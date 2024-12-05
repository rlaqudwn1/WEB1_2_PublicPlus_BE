package backend.dev.activity.mapper;

import backend.dev.activity.dto.ActivityRequestDTO;
import backend.dev.activity.entity.Activity;
import backend.dev.activity.dto.ActivityResponseDTO;
import backend.dev.activity.entity.ActivityParticipants;
import backend.dev.activity.entity.ParticipantsRole;
import backend.dev.user.entity.User;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class ActivityMapper {

    // ActivityRequestDTO -> Activity 엔티티로 변환
    public static Activity toActivity(ActivityRequestDTO requestDTO) {
        return Activity.builder()
                .title(requestDTO.title())
                .description(requestDTO.description())
                .location(requestDTO.location())
                .startTime(requestDTO.startTime()) // 시간을 파싱하는 메서드 호출
                .endTime(requestDTO.endTime()) // 시간을 파싱하는 메서드 호출
                .maxParticipants(requestDTO.maxParticipants())
                .build();
    }

    // Activity -> ActivityResponseDTO로 변환
    public static ActivityResponseDTO toActivityResponseDTO(Activity activity) {
        return new ActivityResponseDTO(
                activity.getTitle(),
                activity.getDescription(),
                activity.getLocation(),
                activity.getStartTime().toString(),
                activity.getEndTime().toString(),
                activity.getMaxParticipants(),
                activity.getCurrentParticipants()
        );
    }
    public static ActivityParticipants toActivityParticipantsAdmin(Activity activity, User user) {
        activity.changeCurrentParticipants(activity.getCurrentParticipants()+1);
        return ActivityParticipants.builder()
                .user(user)
                .activity(activity)
                .role(ParticipantsRole.ADMIN).build();
    }
    public static ActivityParticipants toActivityParticipantsUser(Activity activity, User user) {
        activity.changeCurrentParticipants(activity.getCurrentParticipants()+1);
        return ActivityParticipants.builder()
                .user(user)
                .activity(activity)
                .role(ParticipantsRole.USER).build();
    }

    // ZonedDateTime을 LocalDateTime으로 파싱하는 메서드
    public static LocalDateTime parseDateTime(String dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("입력된 dateTime 값이 null입니다.");
        }

        // 여러 포맷에 대응할 수 있는 파싱 로직
        try {
            // 1. ISO_DATE_TIME 포맷 (예: 2024-11-02T15:00:00Z)
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
            return zonedDateTime.toLocalDateTime();
        } catch (Exception e1) {
            try {
                // 2. LocalDateTime 포맷 (예: 2024-11-02T15:00:00)
                LocalDateTime localDateTime = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                return localDateTime;
            } catch (Exception e2) {
                throw new IllegalArgumentException("지원되지 않는 날짜 형식입니다: " + dateTime);
            }
        }
    }
    public static EventDateTime parseLocalDateTime(LocalDateTime localDateTime) {
        return new EventDateTime().setDateTime( new com.google.api.client.util.DateTime(localDateTime.atOffset(ZoneOffset.UTC).format(DateTimeFormatter.ISO_DATE_TIME)));
    }
}

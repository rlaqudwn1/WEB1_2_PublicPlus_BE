package backend.dev.activity.dto;

import lombok.Data;


public record ActivityRequestDTO(
        String title,
        String description,
        String location,
        String startTime,
        String endTime,
        int maxParticipants
) {
}

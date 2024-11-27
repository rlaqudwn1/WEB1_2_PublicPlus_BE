package backend.dev.activity.dto;

public record ActivityResponseDTO(
        String title,
        String description,
        String location,
        String startTime,
        String endTime,
        int maxParticipants,
        int currentParticipants,
        String eventId
) {
}

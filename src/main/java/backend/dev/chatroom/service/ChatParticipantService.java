package backend.dev.chatroom.service;

import backend.dev.chatroom.dto.response.ChatParticipantResponseDTO;
import backend.dev.chatroom.entity.ChatParticipant;
import backend.dev.chatroom.repository.ChatParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class ChatParticipantService {

    private final ChatParticipantRepository chatParticipantRepository;

    public ChatParticipantService(ChatParticipantRepository chatParticipantRepository) {
        this.chatParticipantRepository = chatParticipantRepository;
    }

    public ChatParticipantResponseDTO getParticipant(Long chatRoomId) {
        ChatParticipant participant = chatParticipantRepository.findFirstByChatRoom_ChatRoomId(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("참여자를 찾을 수 없습니다."));
        return new ChatParticipantResponseDTO(participant.getParticipantId(), participant.getUser().getUserId());
    }
}

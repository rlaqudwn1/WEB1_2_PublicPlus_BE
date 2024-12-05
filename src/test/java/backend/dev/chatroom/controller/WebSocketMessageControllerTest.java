package backend.dev.chatroom.controller;

import backend.dev.chatroom.dto.request.MessageRequestDTO;
import backend.dev.chatroom.dto.response.MessageResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketMessageControllerTest {

    @LocalServerPort
    private int port;

    private WebSocketStompClient stompClient;

    @BeforeEach
    public void setup() {
        // SockJS를 지원하는 WebSocket 클라이언트 설정
        List<Transport> transports = Arrays.asList(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient sockJsClient = new SockJsClient(transports);

        // WebSocketStompClient를 SockJS 클라이언트로 설정
        this.stompClient = new WebSocketStompClient(sockJsClient);
        this.stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void testSendMessageViaWebSocket() throws Exception {
        // WebSocket 세션 연결
        StompSession session = stompClient.connect(
                "ws://localhost:" + port + "/ws", new StompSessionHandlerAdapter() {}
        ).get(1, TimeUnit.SECONDS);

        // 채팅방 토픽 구독
        session.subscribe("/topic/chatroom/1", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                // 수신 메시지의 타입을 지정
                return MessageResponseDTO.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                // 수신한 메시지를 검증
                MessageResponseDTO message = (MessageResponseDTO) payload;
                assertEquals("Hello WebSocket", message.getContent());
            }
        });

        // 메시지 요청 DTO 생성
        MessageRequestDTO request = new MessageRequestDTO();
        request.setChatRoomId(1L);
        request.setParticipantId(202L);
        request.setContent("Hello WebSocket");

        // 메시지 전송
        session.send("/app/chat/send/1", request);

        // 테스트 실행 대기 (메시지가 처리될 시간을 확보)
        Thread.sleep(1000);
    }
}
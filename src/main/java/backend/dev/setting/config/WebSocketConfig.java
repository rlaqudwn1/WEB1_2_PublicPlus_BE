package backend.dev.setting.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트에게 보내는 메시지 경로
        System.out.println("WebSocket 연결 설정 초기화");
        config.enableSimpleBroker("/topic", "/queue");  // Simple 메시지 브로커 설정
        config.setApplicationDestinationPrefixes("/app");  // 클라이언트가 메시지를 보낼 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 WebSocket 연결을 시작할 엔드포인트
        registry.addEndpoint("/ws") // WebSocket 연결 엔드포인트
                .setAllowedOriginPatterns("*")// CORS 허용
                .withSockJS(); // SockJS 활성화;
        System.out.println("WebSocket 엔드포인트 /ws 등록 완료");
    }
}
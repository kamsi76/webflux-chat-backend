package com.company.config.websocket.handler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;

import com.company.chat.service.ChatMessageService;
import com.company.common.database.document.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;
import reactor.core.publisher.Sinks.EmitResult;

/**
 * WebSocket을 통해 들어오는 메시지를 브로드캐스트(모든 클라이언트에게 전송)하는 핸들러
 * - 클라이언트가 메시지를 보내면 Sinks.Many를 통해 모든 연결된 세션에 해당 메시지 전송
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    // 채팅방별 메시지 브로드캐스트 sink를 저장하는 맵
    private final Map<String, Sinks.Many<String>> roomSinkMap = new ConcurrentHashMap<>();

    private final ChatMessageService chatMessageService;

    private final ObjectMapper objectMapper = new ObjectMapper(); // JSON 파싱

    /**
     * 클라이언트와 WebSocket 연결이 수립되면 호출되는 메서드
     *
     * @param session 현재 연결된 WebSocket 세션 객체
     * @return Mono<Void> 비동기적으로 연결을 처리
     */
    @Override
    public Mono<Void> handle(WebSocketSession session) {

        String roomId = extractRoomId(session);

        // 해당 채팅방 sink가 없으면 생성
        Sinks.Many<String> roomSink = roomSinkMap.computeIfAbsent(
                                                    roomId,
                                                    key -> Sinks.many().replay().limit(1) // 마지막 메시지 1개까지 리플레이
                                                );

        // 서버가 클라이언트로 전송할 메시지 스트림
        Flux<WebSocketMessage> outgoing = roomSink.asFlux() // Sink로부터 메시지 Flux 스트림 획득
                                                    .map(session::textMessage); // 문자열을 WebSocket 텍스트 메시지로 변환

        // 클라이언트로부터 수신되는 메시지 스트림
        Flux<String> incoming = session.receive()
                                        .map(WebSocketMessage::getPayloadAsText).doOnNext(json -> {
                                            try {

                                                /*
                                                 * json 형태로 전송된 메시지 내용을 Object형태로 변환한다.
                                                 */
                                                ChatMessage msg = objectMapper.readValue(json, ChatMessage.class);
                                                msg.setTimestamp(LocalDateTime.now());
                                                msg.setRoomId(Long.parseLong(roomId));

                                                // MongoDB 저장
                                                chatMessageService.save(msg).subscribe();

                                                // 브로드캐스트
                                                EmitResult result = roomSink.tryEmitNext(json);
                                                log.info("Emit result: {}", result.name()); // 디버깅을 위해

                                            } catch (Exception e) {
                                                log.error("메시지 파싱 실패: {}", e.getMessage());
                                            }
                                        });

        // 서버 → 클라이언트 메시지 전송
        return session.send(outgoing).and(incoming.then());
    }

    /**
     * 세션 경로에서 채팅방 ID 추출 (예: /ws/chat/12345에서 12345 추출)
     *
     * @param session
     * @return
     */
    private String extractRoomId(WebSocketSession session) {
        String path = session.getHandshakeInfo().getUri().getPath(); // ex) /ws/chat/1
        return path.substring(path.lastIndexOf("/") + 1);
    }

}

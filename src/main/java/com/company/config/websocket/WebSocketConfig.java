package com.company.config.websocket;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import com.company.config.websocket.handler.ChatWebSocketHandler;

/**
 * WebSocket 핸들러를 등록하고, Spring WebFlux 환경에서 WebSocket 요청을 처리할 수 있도록
 * 필요한 설정을 구성하는 Configuration
 *
 * 주요 역할:
 * - WebSocket 요청 경로("/ws/chat")에 대한 핸들러(ChatWebSocketHandler) 등록
 * - WebSocket 요청 처리를 위한 핸들러 어댑터(WebSocketHandlerAdapter) 등록
 */
@Configuration
public class WebSocketConfig {

	/**
	 * WebSocket 요청 경로에 따라 핸들러 매핑 설정
	 * 클라이언트가 "/ws/chat" 경로로 websocket 연결 요청을 보내면
	 * 해당 요청은 ChatWebSocketHandler에서 처리하도록 한다.
	 *
	 * @param handler
	 * @return
	 */
	@Bean HandlerMapping handlerMapping(ChatWebSocketHandler handler) {
		Map<String, WebSocketHandler> map = new HashMap<>();
		map.put("/ws/chat/{roomId}", handler);	// 동적 경로 지원은 불가 → 접두어만 등록

		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(map);
		mapping.setOrder(-1);
		return mapping;
	}

	@Bean WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}

}

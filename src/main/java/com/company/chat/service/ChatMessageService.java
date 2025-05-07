package com.company.chat.service;

import org.springframework.stereotype.Service;

import com.company.chat.repository.ChatMessageRepository;
import com.company.common.database.document.ChatMessage;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 채팅 메시지 저장/조회 서비스
 */
@Service
@RequiredArgsConstructor
public class ChatMessageService {

	private final ChatMessageRepository messageRepository;

    // 메시지 저장
    public Mono<ChatMessage> save(ChatMessage message) {
        return messageRepository.save(message);
    }

    // 메시지 조회 (방 ID 기준)
    public Flux<ChatMessage> findByRoomId(Long roomId) {
        return messageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}

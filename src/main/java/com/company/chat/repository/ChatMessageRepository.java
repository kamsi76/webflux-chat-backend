package com.company.chat.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.company.common.database.document.ChatMessage;

import reactor.core.publisher.Flux;

public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

    // 특정 방의 메시지 리스트 (시간순)
    Flux<ChatMessage> findByRoomIdOrderByTimestampAsc(Long roomId);
}
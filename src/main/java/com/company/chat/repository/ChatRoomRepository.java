package com.company.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.company.common.database.entity.ChatRoom;

import reactor.core.publisher.Flux;

public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, Long> {
    // 모든 채팅방 목록 조회
    Flux<ChatRoom> findAll();
}
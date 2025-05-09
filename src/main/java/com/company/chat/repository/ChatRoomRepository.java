package com.company.chat.repository;

import java.util.Collection;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.company.common.database.entity.ChatRoom;

import reactor.core.publisher.Flux;

/**
 * 채팅방 관리 Repository
 */
public interface ChatRoomRepository extends ReactiveCrudRepository<ChatRoom, Long> {
    
    /**
     * 생성되어 있는 모든 채팅방 목록을 조회한다.
     */
    Flux<ChatRoom> findAll();

    /**
     * 참여한 채팅방 id에 해당하는 채팅방 목록을 조회 한다.
     * @param ids
     * @return
     */
    @Query("SELECT * FROM chatrooms WHERE id IN (:ids)")
    Flux<ChatRoom> findByIdIn(Collection<Long> ids);
}
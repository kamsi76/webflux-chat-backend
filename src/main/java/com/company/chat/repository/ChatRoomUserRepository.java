package com.company.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.company.common.database.entity.ChatRoomUser;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 채팅방 참여자 관리 Repository
 */
public interface ChatRoomUserRepository extends ReactiveCrudRepository<ChatRoomUser, Long> {

    /**
     * 특정 채팅방에 해당하는 사용자 목록을 조회한다.
     * 
     * @param roomId
     * @return Flux<ChatRoomUser>
     */
    Flux<ChatRoomUser> findByChatroomId(Long roomId);

    /**
     * 채팅방에 사용자가 있는지 여부를 확인한다.
     * 
     * @param roomId
     * @param userId
     * @return
     */
    Mono<Boolean> existsByChatroomIdAndUserId(Long roomId, Long userId);

    /**
     * 체팅방에서 퇴장 처리한다.
     * 
     * @param roomId
     * @param userId
     * @return
     */
    Mono<Void> deleteByChatroomIdAndUserId(Long roomId, Long userId);

    /**
     * 사용자가 등록되어 있는 채팅방 목록 정보를 조회한다.
     * 
     * @param userId
     * @return
     */
    Flux<ChatRoomUser> findByUserId(Long userId);
}
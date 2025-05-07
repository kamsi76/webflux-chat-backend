package com.company.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.chat.service.ChatRoomService;
import com.company.common.database.entity.ChatRoom;
import com.company.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 채팅방 생성/조회 API
 */
@RestController
@RequestMapping("/api/v1/chat/chatroom")
@RequiredArgsConstructor
public class ChatRoomController {

	private final ChatRoomService chatRoomService;

	// 채팅방 생성 요청
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<ChatRoom>>> createRoom(@RequestParam String name) {
        return chatRoomService.createRoom(name)
                .map(room -> ResponseEntity.ok(new ApiResponse<>(true, "채팅방 생성 성공", room)))
                .onErrorResume(e ->
                        Mono.just(ResponseEntity.badRequest().body(new ApiResponse<>(false, e.getMessage(), null))));
    }

    // 채팅방 목록 조회
    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<ChatRoom>>>> getAllRooms() {
        return chatRoomService.getAllRooms()
        						.collectList()
        						.map(rooms -> ResponseEntity.ok(new ApiResponse<>(true, "채팅방 목록", rooms)) );
    }
}

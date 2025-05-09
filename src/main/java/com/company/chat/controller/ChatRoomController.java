package com.company.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.chat.service.ChatRoomParticipantService;
import com.company.chat.service.ChatRoomService;
import com.company.common.database.entity.ChatRoom;
import com.company.common.database.entity.User;
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
	private final ChatRoomParticipantService chatRoomParticipantService;


	/**
	 * 채팅방을 생성한다.
	 * @param name
	 * @return
	 */
    @PostMapping
    public Mono<ResponseEntity<ApiResponse<ChatRoom>>> createRoom(@RequestParam String name, ServerHttpRequest request) {

        User user = (User) request.getAttributes().get("user"); // 필터에서 저장한 사용자 정보

        return chatRoomService.createRoom(name, user.getId())
                                        .map(room -> ResponseEntity.ok(new ApiResponse<>(true, "채팅방 생성 성공", room)));

    }

    /**
     * 채팅방 목록을 조회 한다.
     * - 채팅방 목록은 로그인한 사용자만 볼수 있도록 한다.
     * @return
     */
    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<ChatRoom>>>> selectAllRooms(ServerHttpRequest request) {

        return chatRoomService.selectAllRooms()
                                .collectList()
                                .map(rooms -> ResponseEntity.ok(new ApiResponse<>(true, "채팅방 목록", rooms)) );
    }

    /**
     * 내가 참여한 채팅방 목록 조회
     * @param request
     * @return
     */
    @GetMapping("/my")
    public Mono<ResponseEntity<ApiResponse<List<ChatRoom>>>> selectMyChateRooms(ServerHttpRequest request) {

        User user = (User) request.getAttributes().get("user"); // 필터에서 저장한 사용자 정보

        return chatRoomService.selectMyChateRooms(user.getId())
                                .collectList()
                                .map(rooms -> ResponseEntity.ok(new ApiResponse<>(true, "나의채팅방 목록", rooms)) );
    }

    /**
     * 채팅방에 포함되어 있는 참여자 정보를 조회한다.
     * @param roomId
     * @return
     */
    @GetMapping("/{roomId}/participants")
    public Mono<ResponseEntity<ApiResponse<List<User>>>> selectParticipants(@PathVariable Long roomId) {
    	return chatRoomParticipantService.findParticipantsByRoomId(roomId)
    			.map(users -> ResponseEntity.ok(new ApiResponse<>(true, "참여자 목록", users)));
    }


    /**
     * 채팅방 입장 처리 한다.
     * @param roomId
     * @param token
     * @return
     */
    @PostMapping("/{roomId}/enter")
    public Mono<ResponseEntity<ApiResponse<Void>>> enterChatRoom(@PathVariable Long roomId, ServerHttpRequest request) {

        User user = (User) request.getAttributes().get("user"); // 필터에서 저장한 사용자 정보

        return chatRoomParticipantService.registerParticipant(roomId, user.getId())
				.thenReturn(ResponseEntity.ok(new ApiResponse<>(true, "입장 처리 완료!!!", null)));

    }

    /**
     * 채팅방에서 퇴장 처리 한다.
     * @param roomId
     * @param token
     * @return
     */
    @DeleteMapping("/{roomId}/exit")
    public Mono<ResponseEntity<ApiResponse<Void>>> exitChatRoom(@PathVariable Long roomId, ServerHttpRequest request) {

        User user = (User) request.getAttributes().get("user"); // 필터에서 저장한 사용자 정보

    	return chatRoomParticipantService.removeParticipant(roomId, user.getId())
    					.thenReturn(ResponseEntity.ok(new ApiResponse<>(true, "퇴장처리 완료", null)));
    }

}

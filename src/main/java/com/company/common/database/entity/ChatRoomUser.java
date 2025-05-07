package com.company.common.database.entity;

import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 채팅방 엔티티 (PostgreSQL)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("chatroom_user")
public class ChatRoomUser {
	private Long chatroomId;
	private Long userId;
}

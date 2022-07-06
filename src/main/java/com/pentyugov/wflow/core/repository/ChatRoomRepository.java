package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRoomRepository extends BaseRepository<ChatRoom> {

    @Transactional(readOnly = true)
    @Query("select cr from workflow$ChatRoom cr where cr.sender.id = ?1 and cr.recipient.id = ?2")
    Optional<ChatRoom> findBySenderAndRecipient(UUID senderId, UUID recipientId);

    @Transactional(readOnly = true)
    @Query("select cr from workflow$ChatRoom cr where cr.sender.id = ?1")
    List<ChatRoom> findRoomsForUser(UUID id);
}

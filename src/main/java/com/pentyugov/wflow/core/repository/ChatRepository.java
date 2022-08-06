package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ChatRepository extends BaseRepository<ChatMessage> {

    @Transactional(readOnly = true)
    @Query("select cm from workflow$ChatMessage cm where cm.chatId = ?1 order by cm.createDate")
    List<ChatMessage> findByChatId(String chatId);

    @Transactional(readOnly = true)
    Page<ChatMessage> findByChatId(String chatId, Pageable pageable);

    @Transactional(readOnly = true)
    @Query("select count(cm) from workflow$ChatMessage cm where cm.recipient.id = ?1 and cm.status <> ?2")
    Integer findNewMessagesCountForUser(UUID userId, int status);

    @Transactional(readOnly = true)
    @Query("select cm from workflow$ChatMessage cm where cm.recipient.id = ?1 and cm.status = ?2")
    List<ChatMessage> findMessagesForUserByStatus(UUID userId, int status);

    @Transactional(readOnly = true)
    @Query("select cm from workflow$ChatMessage cm where cm.recipient.id = ?1 and cm.status <> ?2")
    List<ChatMessage> findNewMessagesForUser(UUID userId, int status);

}

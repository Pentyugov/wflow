package com.pentyugov.wflow.core.repository;


import com.pentyugov.wflow.core.domain.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends BaseRepository<Notification> {

    @Transactional(readOnly = true)
    @Query("select n from workflow$Notification n where n.receiver.id = ?1 and n.read <> TRUE")
    List<Notification> findByReceiverId(UUID receiverId);

    @Transactional(readOnly = true)
    Page<Notification> findAllByReceiverId(Pageable pageable, UUID receiverId);

    @Query("delete from workflow$Notification n where n.id = ?1")
    @Transactional
    @Modifying
    void delete(UUID id);

}

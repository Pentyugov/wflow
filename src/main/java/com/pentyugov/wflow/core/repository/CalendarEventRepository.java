package com.pentyugov.wflow.core.repository;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CalendarEventRepository extends BaseRepository<CalendarEvent> {

    @Transactional(readOnly = true)
    List<CalendarEvent> getAllByUserId(UUID userId);

    @Transactional
    List<CalendarEvent> findAllByCardId(UUID cardId);
}

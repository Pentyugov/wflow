package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.dto.CalendarEventDto;

import java.util.List;
import java.util.UUID;

public interface CalendarEventService {

    String NAME = "wflow$CalendarEventService";

    List<CalendarEvent> getAllForCurrentUser();

    CalendarEvent getById(UUID id);

    CalendarEvent add(CalendarEvent calendarEvent);

    CalendarEvent createEventForCard(Card card);

    CalendarEvent update(CalendarEvent calendarEventDto);

    CalendarEvent convert(CalendarEventDto calendarEventDto);

    CalendarEventDto convert(CalendarEvent calendarEvent);

    void delete(UUID id);

    void deleteByCard(Card card);
}

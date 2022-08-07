package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.dto.CalendarEventDto;

import java.util.List;
import java.util.UUID;

public interface CalendarEventService {

    String NAME = "wflow$CalendarEventService";

    List<CalendarEventDto> getAllForCurrentUser();

    CalendarEventDto getById(UUID id);

    CalendarEventDto add(CalendarEventDto calendarEventDto);

    void addForCard(Card card);

    CalendarEventDto update(CalendarEventDto calendarEventDto);

    CalendarEvent convert(CalendarEventDto calendarEventDto);

    CalendarEventDto convert(CalendarEvent calendarEvent);

    void delete(UUID id);

    void deleteByCard(Card card);
}

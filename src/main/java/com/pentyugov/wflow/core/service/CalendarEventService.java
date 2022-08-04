package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.dto.CalendarEventDto;

import java.util.List;
import java.util.UUID;

public interface CalendarEventService {

    String NAME = "wflow$CalendarEventService";

    List<CalendarEventDto> getAllForCurrentUser();

    CalendarEventDto getCalendarEventById(UUID id);

    CalendarEventDto addCalendarEvent(CalendarEventDto calendarEventDto);

    void addCalendarEventForCard(Card card);

    CalendarEventDto updateCalendarEvent(CalendarEventDto calendarEventDto);

    CalendarEvent createEventFromDto(CalendarEventDto calendarEventDto);

    CalendarEventDto createDtoFromEvent(CalendarEvent calendarEvent);

    void deleteCalendarEventByCard(Card card);

    void deleteCalendarEvent(UUID id);
}

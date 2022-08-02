package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.web.exception.UserNotFoundException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

public interface CalendarEventService {

    String NAME = "wflow$CalendarEventService";

    List<CalendarEventDto> getAllForCurrentUser(Principal principal) throws UserNotFoundException;

    CalendarEventDto getCalendarEventById(UUID id);

    CalendarEventDto addCalendarEvent(CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException;

    void addCalendarEventForCard(Card card);

    CalendarEventDto updateCalendarEvent(CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException;

    CalendarEvent createEventFromDto(CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException;

    CalendarEventDto createDtoFromEvent(CalendarEvent calendarEvent);

    void deleteCalendarEventByCard(Card card);

    void deleteCalendarEvent(UUID id);
}

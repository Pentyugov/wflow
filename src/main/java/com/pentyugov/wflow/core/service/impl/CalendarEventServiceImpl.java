package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.core.repository.CalendarEventRepository;
import com.pentyugov.wflow.core.service.CalendarEventService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(CalendarEventService.NAME)
public class CalendarEventServiceImpl implements CalendarEventService {

    private final UserService userService;
    private final CalendarEventRepository calendarEventRepository;

    public CalendarEventServiceImpl(UserService userService, CalendarEventRepository calendarEventRepository) {
        this.userService = userService;
        this.calendarEventRepository = calendarEventRepository;
    }

    @Override
    public List<CalendarEventDto> getAllForCurrentUser(Principal principal) throws UserNotFoundException {
        return calendarEventRepository.getAllByUserId(userService.getUserByPrincipal(principal).getId())
                .stream().map(this::createDtoFromEvent).collect(Collectors.toList());
    }

    @Override
    public CalendarEventDto getCalendarEventById(UUID id) {
        return createDtoFromEvent(calendarEventRepository.getById(id));
    }

    public CalendarEventDto addCalendarEvent(CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException {
        CalendarEvent calendarEvent = createEventFromDto(calendarEventDto, principal);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return createDtoFromEvent(calendarEvent);
    }

    @Override
    public CalendarEventDto addCalendarEventForCard(Card card) {
        CalendarEvent calendarEvent = createCalendarEventTemplateForCard();
        calendarEvent.setTitle(card.getNumber());
        calendarEvent.setDescription(card.getDescription());
        calendarEvent.setUser(card.getIssue().getExecutor());
        calendarEvent.setCard(card);
        if (card instanceof Task) {
            Task task = (Task) card;
            calendarEvent.setType(CalendarEvent.TYPE_TASK);
            calendarEvent.setStartDate(task.getExecutionDatePlan());
            calendarEvent.setEndDate(task.getExecutionDatePlan());
        }

        calendarEvent = calendarEventRepository.save(calendarEvent);
        return createDtoFromEvent(calendarEvent);
    }

    @Override
    public CalendarEventDto updateCalendarEvent(CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException {
        CalendarEvent calendarEvent = createEventFromDto(calendarEventDto, principal);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return createDtoFromEvent(calendarEvent);
    }

    @Override
    public CalendarEvent createEventFromDto(CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException {
        CalendarEvent calendarEvent;
        if (calendarEventDto.getId() != null) {
            calendarEvent = calendarEventRepository.getById(calendarEventDto.getId());
        } else {
            calendarEvent = new CalendarEvent();
            calendarEvent.setUser(userService.getUserByPrincipal(principal));
        }

        calendarEvent.setType(calendarEventDto.getType());
        calendarEvent.setTitle(calendarEventDto.getTitle());
        calendarEvent.setDescription(calendarEventDto.getDescription());
        calendarEvent.setStartDate(calendarEventDto.getStart());
        calendarEvent.setEndDate(calendarEventDto.getEnd());
        calendarEvent.setAllDay(calendarEventDto.getAllDay());
        calendarEvent.setDraggable(calendarEventDto.getDraggable());
        if (calendarEventDto.getColor() != null) {
            calendarEvent.setColorPrimary(calendarEventDto.getColor().getPrimary());
            calendarEvent.setColorSecondary(calendarEventDto.getColor().getSecondary());
        }

        if (calendarEventDto.getType() == CalendarEvent.TYPE_CUSTOM) {
            calendarEvent.setResizableBeforeStart(Boolean.TRUE);
            calendarEvent.setResizableAfterEnd(Boolean.TRUE);
        }

        if (calendarEventDto.getResizable() != null) {
            calendarEvent.setResizableBeforeStart(calendarEventDto.getResizable().getBeforeStart());
            calendarEvent.setResizableAfterEnd(calendarEventDto.getResizable().getAfterEnd());
        }

        return calendarEvent;
    }

    @Override
    public CalendarEventDto createDtoFromEvent(CalendarEvent calendarEvent) {
        CalendarEventDto calendarEventDto = new CalendarEventDto();
        calendarEventDto.setId(calendarEvent.getId());
        calendarEventDto.setType(calendarEvent.getType());
        calendarEventDto.setTitle(calendarEvent.getTitle());
        calendarEventDto.setDescription(calendarEvent.getDescription());
        calendarEventDto.setStart(calendarEvent.getStartDate());
        calendarEventDto.setEnd(calendarEvent.getEndDate());
        calendarEventDto.setAllDay(calendarEvent.getAllDay());
        calendarEventDto.setDraggable(calendarEvent.getDraggable());
        calendarEventDto.setColor(new CalendarEventDto.Color(calendarEvent.getColorPrimary(), calendarEvent.getColorSecondary()));
        calendarEventDto.setResizable(new CalendarEventDto.Resizable(calendarEvent.getResizableBeforeStart(), calendarEvent.getResizableAfterEnd()));
        return calendarEventDto;
    }

    @Override
    public void deleteCalendarEventByCard(Card card) {
        List<CalendarEvent> events = calendarEventRepository.findAllByCardId(card.getId());
        if (!CollectionUtils.isEmpty(events)) {
            calendarEventRepository.deleteAll(events);
        }
    }

    @Override
    public void deleteCalendarEvent(UUID id) {
        this.calendarEventRepository.delete(id);
    }

    private CalendarEvent createCalendarEventTemplateForCard() {
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setDraggable(Boolean.FALSE);
        calendarEvent.setAllDay(Boolean.FALSE);
        calendarEvent.setResizableAfterEnd(Boolean.FALSE);
        calendarEvent.setResizableBeforeStart(Boolean.FALSE);
        calendarEvent.setColorPrimary(CalendarEvent.COLOR_RED_PRIMARY);
        calendarEvent.setColorSecondary(CalendarEvent.COLOR_RED_SECONDARY);
        return calendarEvent;
    }
}

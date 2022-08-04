package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.core.repository.CalendarEventRepository;
import com.pentyugov.wflow.core.service.CalendarEventService;
import com.pentyugov.wflow.core.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(CalendarEventService.NAME)
@RequiredArgsConstructor
public class CalendarEventServiceImpl implements CalendarEventService {

    private final CalendarEventRepository calendarEventRepository;
    private final UserSessionService userSessionService;

    @Override
    public List<CalendarEventDto> getAllForCurrentUser() {
        return calendarEventRepository.getAllByUserId(userSessionService.getCurrentUser().getId())
                .stream().map(this::createDtoFromEvent).collect(Collectors.toList());
    }

    @Override
    public CalendarEventDto getCalendarEventById(UUID id) {
        return createDtoFromEvent(calendarEventRepository.getById(id));
    }

    @Override
    public CalendarEventDto addCalendarEvent(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = createEventFromDto(calendarEventDto);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return createDtoFromEvent(calendarEvent);
    }

    @Override
    public void addCalendarEventForCard(Card card) {
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
        createDtoFromEvent(calendarEvent);
    }

    @Override
    public CalendarEventDto updateCalendarEvent(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = createEventFromDto(calendarEventDto);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return createDtoFromEvent(calendarEvent);
    }

    @Override
    public CalendarEvent createEventFromDto(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent;
        if (calendarEventDto.getId() != null) {
            calendarEvent = calendarEventRepository.getById(calendarEventDto.getId());
        } else {
            calendarEvent = new CalendarEvent();
            calendarEvent.setUser(userSessionService.getCurrentUser());
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

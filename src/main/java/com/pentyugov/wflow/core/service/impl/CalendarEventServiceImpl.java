package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.domain.entity.Card;
import com.pentyugov.wflow.core.domain.entity.Task;
import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.core.repository.CalendarEventRepository;
import com.pentyugov.wflow.core.service.CalendarEventService;
import com.pentyugov.wflow.core.service.UserSessionService;
import com.pentyugov.wflow.web.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(CalendarEventService.NAME)
@RequiredArgsConstructor
public class CalendarEventServiceImpl extends AbstractService implements CalendarEventService {

    @Value("${source.service.auth}")
    private String sourcePath;

    private final CalendarEventRepository calendarEventRepository;
    private final UserSessionService userSessionService;

    @Override
    public List<CalendarEventDto> getAllForCurrentUser() {
        return calendarEventRepository.getAllByUserId(userSessionService.getCurrentUser().getId())
                .stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public CalendarEventDto getById(UUID id) {
        return convert(calendarEventRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(getMessage(sourcePath, "exception.entity.with.id.not.found", id.toString()))));
    }

    @Override
    public CalendarEventDto add(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = convert(calendarEventDto);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return convert(calendarEvent);
    }

    @Override
    public void addForCard(Card card) {
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
        convert(calendarEvent);
    }

    @Override
    public CalendarEventDto update(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = convert(calendarEventDto);
        calendarEvent = calendarEventRepository.save(calendarEvent);
        return convert(calendarEvent);
    }

    @Override
    public CalendarEvent convert(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = calendarEventRepository.findById(calendarEventDto.getId()).orElse(new CalendarEvent());
        calendarEvent.setUser(userSessionService.getCurrentUser());
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
    public CalendarEventDto convert(CalendarEvent calendarEvent) {
        return CalendarEventDto.builder()
                .id(calendarEvent.getId())
                .type(calendarEvent.getType())
                .title(calendarEvent.getTitle())
                .description(calendarEvent.getDescription())
                .start(calendarEvent.getStartDate())
                .end(calendarEvent.getEndDate())
                .allDay(calendarEvent.getAllDay())
                .draggable(calendarEvent.getDraggable())
                .color(new CalendarEventDto.Color(
                        calendarEvent.getColorPrimary(), calendarEvent.getColorSecondary()
                ))
                .resizable(new CalendarEventDto.Resizable(
                        calendarEvent.getResizableBeforeStart(), calendarEvent.getResizableAfterEnd()
                ))
                .build();
    }

    @Override
    public void deleteByCard(Card card) {
        List<CalendarEvent> events = calendarEventRepository.findAllByCardId(card.getId());
        if (!CollectionUtils.isEmpty(events)) {
            calendarEventRepository.deleteAll(events);
        }
    }

    @Override
    public void delete(UUID id) {
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

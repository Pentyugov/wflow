package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.AbstractTest;
import com.pentyugov.wflow.core.domain.entity.*;
import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.core.repository.CalendarEventRepository;
import com.pentyugov.wflow.core.repository.UserRepository;
import com.pentyugov.wflow.web.exception.EntityNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CalendarEventServiceTest extends AbstractTest {

    @Autowired
    private CalendarEventService calendarEventService;
    @Autowired
    private UserRepository userRepository;
    @MockBean
    private UserSessionService userSessionService;
    @MockBean
    private CalendarEventRepository calendarEventRepository;

    private User user;


    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(UUID.randomUUID());
    }

    @AfterEach
    void tearDown() {
        userRepository.removeUserByUsername(user.getUsername());
    }

    @Test
    void getAllForCurrentUser() {
        UUID id = UUID.randomUUID();
        CalendarEvent savedCalendarEvent = CalendarEvent.builder()
                .id(id)
                .title("Test event title")
                .description("Test event description")
                .draggable(Boolean.TRUE)
                .type(CalendarEvent.TYPE_CUSTOM)
                .allDay(Boolean.FALSE)
                .endDate(new Date())
                .startDate(new Date())
                .resizableAfterEnd(Boolean.TRUE)
                .resizableBeforeStart(Boolean.TRUE)
                .user(user)
                .colorPrimary(CalendarEvent.COLOR_RED_PRIMARY)
                .colorSecondary(CalendarEvent.COLOR_RED_SECONDARY)
                .build();

        List<CalendarEvent> savedCalendarEvents = List.of(savedCalendarEvent);
        when(userSessionService.getCurrentUser()).thenReturn(user);
        when(calendarEventRepository.getAllByUserId(user.getId())).thenReturn(savedCalendarEvents);

        List<CalendarEvent> foundedEvents = calendarEventService.getAllForCurrentUser();

        assertIterableEquals(savedCalendarEvents, foundedEvents);

    }

    @Test
    void getById() {
        UUID id = UUID.randomUUID();
        CalendarEvent savedCalendarEvent = CalendarEvent.builder()
                .id(id)
                .title("Test event title")
                .description("Test event description")
                .draggable(Boolean.TRUE)
                .type(CalendarEvent.TYPE_CUSTOM)
                .allDay(Boolean.FALSE)
                .endDate(new Date())
                .startDate(new Date())
                .resizableAfterEnd(Boolean.TRUE)
                .resizableBeforeStart(Boolean.TRUE)
                .user(user)
                .colorPrimary(CalendarEvent.COLOR_RED_PRIMARY)
                .colorSecondary(CalendarEvent.COLOR_RED_SECONDARY)
                .build();

        when(calendarEventRepository.findById(id)).thenReturn(Optional.of(savedCalendarEvent));

        CalendarEvent event = calendarEventService.getById(id);

        assertEquals(event, savedCalendarEvent);

    }

    @Test
    void getByIdIfNotFound() {
        UUID id = UUID.randomUUID();
        when(calendarEventRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> calendarEventService.getById(id));
    }

    @Test
    void add() {
        CalendarEvent eventToSave = CalendarEvent.builder()
                .title("Test event title")
                .description("Test event description")
                .draggable(Boolean.TRUE)
                .type(CalendarEvent.TYPE_CUSTOM)
                .allDay(Boolean.FALSE)
                .endDate(new Date())
                .startDate(new Date())
                .resizableAfterEnd(Boolean.TRUE)
                .resizableBeforeStart(Boolean.TRUE)
                .user(user)
                .colorPrimary(CalendarEvent.COLOR_RED_PRIMARY)
                .colorSecondary(CalendarEvent.COLOR_RED_SECONDARY)
                .build();
        calendarEventService.add(eventToSave);

        verify(calendarEventRepository).save(eventToSave);
    }

    @Test
    void update() {
        UUID id = UUID.randomUUID();
        CalendarEvent eventToSave = CalendarEvent.builder()
                .id(id)
                .title("Test event title")
                .description("Test event description")
                .draggable(Boolean.TRUE)
                .type(CalendarEvent.TYPE_CUSTOM)
                .allDay(Boolean.FALSE)
                .endDate(new Date())
                .startDate(new Date())
                .resizableAfterEnd(Boolean.TRUE)
                .resizableBeforeStart(Boolean.TRUE)
                .user(user)
                .colorPrimary(CalendarEvent.COLOR_RED_PRIMARY)
                .colorSecondary(CalendarEvent.COLOR_RED_SECONDARY)
                .build();
        calendarEventService.update(eventToSave);

        verify(calendarEventRepository).save(eventToSave);
    }

    @Test
    void addForCard() {
        Issue issue = Issue.builder()
                .executor(user)
                .build();
        Task task = Task.builder()
                .number("#1")
                .description("test description")
                .executionDatePlan(new Date())
                .issue(issue)
                .build();
        issue.setCard(task);

        CalendarEvent saved = calendarEventService.createEventForCard(task);

        assertAll(
                () -> assertEquals(saved.getCard(), task),
                () -> assertEquals(saved.getUser(), user),
                () -> assertEquals(saved.getTitle(), task.getNumber()),
                () -> assertEquals(saved.getDescription(), task.getDescription()),
                () -> assertEquals(saved.getStartDate(), task.getExecutionDatePlan()),
                () -> assertEquals(saved.getEndDate(), task.getExecutionDatePlan())
        );
    }

    @Test
    void convertToDto() {
        UUID id = UUID.randomUUID();
        CalendarEvent calendarEvent = CalendarEvent.builder()
                .id(id)
                .title("Test event title")
                .description("Test event description")
                .draggable(Boolean.TRUE)
                .type(CalendarEvent.TYPE_CUSTOM)
                .allDay(Boolean.FALSE)
                .endDate(new Date())
                .startDate(new Date())
                .resizableAfterEnd(Boolean.TRUE)
                .resizableBeforeStart(Boolean.TRUE)
                .user(user)
                .colorPrimary(CalendarEvent.COLOR_RED_PRIMARY)
                .colorSecondary(CalendarEvent.COLOR_RED_SECONDARY)
                .build();

        CalendarEventDto calendarEventDto = calendarEventService.convert(calendarEvent);

        assertAll(
                () -> assertEquals(calendarEventDto.getId(), calendarEvent.getId()),
                () -> assertEquals(calendarEventDto.getTitle(), calendarEvent.getTitle()),
                () -> assertEquals(calendarEventDto.getDescription(), calendarEvent.getDescription()),
                () -> assertEquals(calendarEventDto.getColor().getPrimary(), calendarEvent.getColorPrimary()),
                () -> assertEquals(calendarEventDto.getColor().getSecondary(), calendarEvent.getColorSecondary()),
                () -> assertEquals(calendarEventDto.getStart(), calendarEvent.getStartDate()),
                () -> assertEquals(calendarEventDto.getEnd(), calendarEvent.getEndDate()),
                () -> assertEquals(calendarEventDto.getResizable().getAfterEnd(), calendarEvent.getResizableAfterEnd()),
                () -> assertEquals(calendarEventDto.getResizable().getBeforeStart(), calendarEvent.getResizableBeforeStart()),
                () -> assertEquals(calendarEventDto.getAllDay(), calendarEvent.getAllDay()),
                () -> assertEquals(calendarEventDto.getType(), calendarEvent.getType())
        );
    }

    @Test
    void convertFromDto() {
        CalendarEventDto calendarEventDto = CalendarEventDto.builder()
                .title("Test event title")
                .description("Test event description")
                .draggable(Boolean.TRUE)
                .type(CalendarEvent.TYPE_CUSTOM)
                .allDay(Boolean.FALSE)
                .end(new Date())
                .start(new Date())
                .resizable(new CalendarEventDto.Resizable(Boolean.TRUE, Boolean.TRUE))
                .color(new CalendarEventDto.Color("red", "red"))
                .build();

        CalendarEvent calendarEvent = calendarEventService.convert(calendarEventDto);

        assertAll(
                () -> assertEquals(calendarEventDto.getTitle(), calendarEvent.getTitle()),
                () -> assertEquals(calendarEventDto.getDescription(), calendarEvent.getDescription()),
                () -> assertEquals(calendarEventDto.getColor().getPrimary(), calendarEvent.getColorPrimary()),
                () -> assertEquals(calendarEventDto.getColor().getSecondary(), calendarEvent.getColorSecondary()),
                () -> assertEquals(calendarEventDto.getStart(), calendarEvent.getStartDate()),
                () -> assertEquals(calendarEventDto.getEnd(), calendarEvent.getEndDate()),
                () -> assertEquals(calendarEventDto.getResizable().getAfterEnd(), calendarEvent.getResizableAfterEnd()),
                () -> assertEquals(calendarEventDto.getResizable().getBeforeStart(), calendarEvent.getResizableBeforeStart()),
                () -> assertEquals(calendarEventDto.getAllDay(), calendarEvent.getAllDay()),
                () -> assertEquals(calendarEventDto.getType(), calendarEvent.getType())
        );

    }

    @Test
    void delete() {
        UUID id = UUID.randomUUID();
        calendarEventService.delete(id);
        verify(calendarEventRepository).delete(id);
    }

    @Test
    void deleteByCard() {
        UUID cardId = UUID.randomUUID();
        Card card = Card.builder()
                .id(cardId)
                .build();
        List<CalendarEvent> events = List.of(new CalendarEvent());

        when(calendarEventRepository.findAllByCardId(cardId)).thenReturn(events);

        calendarEventService.deleteByCard(card);

        verify(calendarEventRepository).deleteAll(events);
    }
}
package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.domain.entity.CalendarEvent;
import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.core.service.CalendarEventService;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendar-events")
public class CalendarController extends AbstractController {


    private final CalendarEventService calendarEventService;

    public CalendarController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllForCurrentUser() {
        return new ResponseEntity<>(
                calendarEventService.getAllForCurrentUser()
                .stream()
                .map(calendarEventService::convert)
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        CalendarEvent calendarEvent = calendarEventService.getById(UUID.fromString(id));
        return new ResponseEntity<>(calendarEventService.convert(calendarEvent), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = calendarEventService.convert(calendarEventDto);
        calendarEvent = calendarEventService.add(calendarEvent);
        return new ResponseEntity<>(calendarEventService.convert(calendarEvent), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = calendarEventService.convert(calendarEventDto);
        calendarEvent = calendarEventService.update(calendarEvent);
        return new ResponseEntity<>(calendarEventService.convert(calendarEvent), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        calendarEventService.delete(UUID.fromString(id));
        String message = String.format("Calendar with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }
}

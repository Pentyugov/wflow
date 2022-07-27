package com.pentyugov.wflow.web.rest;

import com.pentyugov.wflow.core.dto.CalendarEventDto;
import com.pentyugov.wflow.core.service.CalendarEventService;
import com.pentyugov.wflow.web.exception.UserNotFoundException;
import com.pentyugov.wflow.web.http.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/calendar")
public class CalendarController extends AbstractController {


    private final CalendarEventService calendarEventService;

    public CalendarController(CalendarEventService calendarEventService) {
        this.calendarEventService = calendarEventService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllForCurrentUser(Principal principal) throws UserNotFoundException {
        return new ResponseEntity<>(calendarEventService.getAllForCurrentUser(principal), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable String id) {
        return new ResponseEntity<>(calendarEventService.getCalendarEventById(UUID.fromString(id)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException {
        CalendarEventDto result = calendarEventService.addCalendarEvent(calendarEventDto, principal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody CalendarEventDto calendarEventDto, Principal principal) throws UserNotFoundException {
        CalendarEventDto result = calendarEventService.updateCalendarEvent(calendarEventDto, principal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpResponse> delete(@PathVariable String id) {
        calendarEventService.deleteCalendarEvent(UUID.fromString(id));
        String message = String.format("Calendar with id: %s was deleted", id);
        return response(HttpStatus.OK, message);
    }
}

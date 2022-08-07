package com.pentyugov.wflow.web.rest;


import com.pentyugov.wflow.core.domain.entity.Ticket;
import com.pentyugov.wflow.core.service.TicketService;
import com.pentyugov.wflow.core.service.UserService;
import com.pentyugov.wflow.web.exception.ExceptionHandling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController extends ExceptionHandling {

    private final TicketService ticketService;
    private final UserService userService;

    @Autowired
    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }
}

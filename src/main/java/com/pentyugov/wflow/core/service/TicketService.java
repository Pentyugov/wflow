package com.pentyugov.wflow.core.service;

import com.pentyugov.wflow.core.domain.entity.Ticket;
import com.pentyugov.wflow.core.dto.TicketDto;

import java.util.List;

public interface TicketService {

    String NAME = "wflow$TicketService";

    List<Ticket> getAllTickets();
}

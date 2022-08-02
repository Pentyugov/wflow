package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Ticket;
import com.pentyugov.wflow.core.repository.TicketRepository;
import com.pentyugov.wflow.core.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(TicketService.NAME)
@RequiredArgsConstructor
public class TicketServiceImpl  extends AbstractService implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

}

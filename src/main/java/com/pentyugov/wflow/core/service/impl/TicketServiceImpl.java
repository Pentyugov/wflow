package com.pentyugov.wflow.core.service.impl;

import com.pentyugov.wflow.core.domain.entity.Ticket;
import com.pentyugov.wflow.core.dto.TicketDto;
import com.pentyugov.wflow.core.repository.TicketRepository;
import com.pentyugov.wflow.core.service.ProjectService;
import com.pentyugov.wflow.core.service.TicketService;
import com.pentyugov.wflow.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(TicketService.NAME)
public class TicketServiceImpl  extends AbstractService implements TicketService {

    private final TicketRepository ticketRepository;
    private final ProjectService projectService;
    private final UserService userService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, ProjectService projectService, UserService userService) {
        this.ticketRepository = ticketRepository;
        this.projectService = projectService;
        this.userService = userService;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> getAllTicketsForUser() {
        return null;
    }

    public TicketDto createTicketDto(Ticket ticket, boolean withProject) {
        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(ticket.getId());
        ticketDto.setTitle(ticket.getTitle());
        ticketDto.setDescription(ticket.getDescription());
        ticketDto.setCreator(userService.createUserDtoFromUser(ticket.getCreator()));
        ticketDto.setAssignee(userService.createUserDtoFromUser(ticket.getAssignee()));
        ticketDto.setStatus(ticket.getStatus());
        if (withProject)
            ticketDto.setProject(projectService.createProjectDto(ticket.getProject()));
        return ticketDto;
    }
}

package com.eventwisp.app.service;

import com.eventwisp.app.dto.TicketUpdateDto;
import com.eventwisp.app.dto.response.TicketUpdateResponse;
import com.eventwisp.app.entity.Ticket;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TicketService {

    //get all ticket types
    List<Ticket> getAllTickets();

    //find a ticket type by id
    Ticket findTicketById(Long id);

    //update the ticket details
    List<TicketUpdateResponse> updateTicket(List<TicketUpdateDto> ticketData);

    //delete an existing ticket type
    Boolean deleteTicket(Long id);

}

package com.eventwisp.app.controller;

import com.eventwisp.app.dto.TicketUpdateDto;
import com.eventwisp.app.dto.response.TicketUpdateResponse;
import com.eventwisp.app.entity.Ticket;
import com.eventwisp.app.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TicketController {

    //Create an instance of 'TicketService'
    private TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService){
        this.ticketService=ticketService;
    }

    @GetMapping("/tickets")
    public ResponseEntity<?> findAllTickets(){
        try{
            List<Ticket> tickets=ticketService.getAllTickets();

            if(tickets.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tickets were found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(tickets);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //find an existing ticket type
    @GetMapping("/tickets/{id}")
    public ResponseEntity<?> findTicketById(@PathVariable Long id){
        try{
            Ticket existingTicket= ticketService.findTicketById(id);

            if(existingTicket==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ticket type found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(existingTicket);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Update tickets
    @PutMapping("/tickets")
    public ResponseEntity<?> updateTicket(@RequestBody List<TicketUpdateDto> ticketUpdateDtoList){
        List<TicketUpdateResponse> ticketUpdateResponses=ticketService.updateTicket(ticketUpdateDtoList);
        return ResponseEntity.status(HttpStatus.OK).body(ticketUpdateResponses);
    }

    //Delete ticket
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id){
        try{
            //check if ticket deleted
            boolean isDeleted= ticketService.deleteTicket(id);

            //Return a message if not
            if(!isDeleted){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No ticket type found");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Ticket type deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

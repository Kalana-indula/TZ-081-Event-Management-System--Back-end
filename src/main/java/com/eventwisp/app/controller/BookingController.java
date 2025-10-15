package com.eventwisp.app.controller;

import com.eventwisp.app.dto.BookingDto;
import com.eventwisp.app.dto.booking.BookingDetailsDto;
import com.eventwisp.app.dto.response.CreateBookingResponse;
import com.eventwisp.app.dto.response.general.MultipleEntityResponse;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.dto.ticket.TicketIssueDto;
import com.eventwisp.app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class BookingController {

    //create an instance of services
    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService){
        this.bookingService=bookingService;
    }

    //create a new booking
    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingDto bookingDto){
        try{
            CreateBookingResponse response= bookingService.createBooking(bookingDto);

            if(response.getBookingDetails()==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getMessage());
            }

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //find all bookings
    @GetMapping("/bookings")
    public ResponseEntity<?> findAllBookings(){
        try {
            MultipleEntityResponse<BookingDetailsDto> bookings=bookingService.findAllBookings();

            if(bookings.getEntityList().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bookings found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(bookings);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // find bookings by event id
    @GetMapping("/events/{eventId}/bookings")
    public ResponseEntity<?> findBookingsByEvent(@PathVariable Long eventId) {
        try {
            // get bookings
            MultipleEntityResponse<BookingDetailsDto> response = bookingService.findBookingsByEvent(eventId);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/events/{bookingId}/issue-tickets")
    public ResponseEntity<?> issueTickets(@PathVariable String bookingId) {
        try {

            SingleEntityResponse<TicketIssueDto> response=bookingService.issueTickets(bookingId);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

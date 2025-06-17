package com.eventwisp.app.controller;

import com.eventwisp.app.dto.BookingDto;
import com.eventwisp.app.dto.response.CreateBookingResponse;
import com.eventwisp.app.dto.response.FindBookingsByEventResponse;
import com.eventwisp.app.entity.Booking;
import com.eventwisp.app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

            if(response.getBooking()==null){
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
            List<Booking> bookings=bookingService.findAllBookings();

            if(bookings.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No bookings found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(bookings);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //find bookings by id
    @GetMapping("/events/{eventId}/bookings")
    public ResponseEntity<?> findBookingsByEvent(@PathVariable Long eventId){
        try {
            //get bookings
            FindBookingsByEventResponse response =bookingService.findBookingsByEvent(eventId);

            //check if the response has an empty booking list
            if(response.getBookings().isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response.getMessage());
            }

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

package com.eventwisp.app.service;

import com.eventwisp.app.dto.BookingDto;
import com.eventwisp.app.dto.response.CreateBookingResponse;
import com.eventwisp.app.dto.response.FindBookingsByEventResponse;
import com.eventwisp.app.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    //create a booking
    CreateBookingResponse createBooking(BookingDto bookingDto);

    //Find all bookings
    List<Booking> findAllBookings();

    //Find all bookings by an event
    FindBookingsByEventResponse findBookingsByEvent(Long eventId);
}

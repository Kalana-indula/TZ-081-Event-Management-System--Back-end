package com.eventwisp.app.repository;

import com.eventwisp.app.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    //custom method for find all bookings by id
    @Query("SELECT b FROM Booking b WHERE b.event.id = :eventId")
    List<Booking> bookingsByEvent(Long eventId);

    //custom method for find last booking id
    @Query("SELECT b.id FROM Booking b ORDER BY b.id DESC LIMIT 1")
    Long findLastId();
}

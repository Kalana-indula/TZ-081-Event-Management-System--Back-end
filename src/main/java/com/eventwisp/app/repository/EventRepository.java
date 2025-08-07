package com.eventwisp.app.repository;

import com.eventwisp.app.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {

    //custom method for find events by organizer
    @Query("SELECT e FROM Event e WHERE e.organizer.id = :organizerId")
    List<Event> eventsByOrganizer(Long organizerId);

    //find all ongoing events
    @Query("SELECT e FROM Event e WHERE e.isCompleted = false")
    List<Event> findAllOnGoingEvents();
}

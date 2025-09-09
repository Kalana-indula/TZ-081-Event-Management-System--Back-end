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

    @Query("SELECT e FROM Event e WHERE e.eventStatus.id = :statusId")
    List<Event> findEventByStatus(Integer statusId);

    @Query("SELECT e FROM Event e WHERE e.organizer.id = :organizerId AND e.eventStatus.id = :statusId")
    List<Event> findOrganizerEventsByStatus(Long organizerId, Integer statusId);

    //find events by category
    List<Event> findByEventCategoryCategory(String categoryName);

}

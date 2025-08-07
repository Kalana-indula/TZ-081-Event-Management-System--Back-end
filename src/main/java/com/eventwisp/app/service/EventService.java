package com.eventwisp.app.service;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.dto.response.EventCreateResponse;
import com.eventwisp.app.dto.response.FindEventByOrganizerResponse;
import com.eventwisp.app.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    EventCreateResponse createEvent(EventDto eventDto);

    List<Event> getAllEvents();

    //event details list for manager dashboard
    List<Event> getManagerEventList();

    Integer getAllOnGoingEventsCount();

    Event getEventById(Long id);

    //get events by organizer
    FindEventByOrganizerResponse findEventByOrganizer(Long organizerId);

    Event updateEvent(Long id, EventUpdateDto eventUpdateDto);

    Boolean deleteEvent(Long id);
}

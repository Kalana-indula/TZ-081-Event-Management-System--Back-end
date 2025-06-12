package com.eventwisp.app.service;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    Event createEvent(EventDto eventDto);

    List<Event> getAllEvents();

    Event getEventById(Long id);

    Event updateEvent(Long id, EventUpdateDto eventUpdateDto);

    Boolean deleteEvent(Long id);
}

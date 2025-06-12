package com.eventwisp.app.service;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.EventCategory;
import com.eventwisp.app.entity.Ticket;
import com.eventwisp.app.repository.EventCategoryRepository;
import com.eventwisp.app.repository.EventRepository;
import com.eventwisp.app.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    //Create an instances of eventRepository and eventCategoryRepository
    private EventRepository eventRepository;

    private EventCategoryRepository eventCategoryRepository;

    private TicketRepository ticketRepository;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository, EventCategoryRepository eventCategoryRepository, TicketRepository ticketRepository) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.ticketRepository = ticketRepository;
    }

    //Create a new event
    @Override
    public Event createEvent(EventDto eventDto) {
        //find event category
        EventCategory category = eventCategoryRepository.findById(eventDto.getEventCategoryId()).orElse(null);

        if (category == null) {
            return null;
        }

        //Create new 'Event' object
        Event event = new Event();

        event.setEventName(eventDto.getEventName());
        event.setStartingDate(eventDto.getStartingDate());
        event.setEventCategory(category);
        event.setCoverImageLink(eventDto.getCoverImageLink());
        event.setDescription(eventDto.getDescription());
        event.setIsCompleted(false);

        //Get ticket types of event
        List<Ticket> ticketTypes = eventDto.getTickets();

        //Create a new list of ticket types
        List<Ticket> ticketTypesList = new ArrayList<>();

        //Iterate through all json objects in DTO
        for (Ticket ticketType : ticketTypes) {
            //Save each ticket object to the database and get a new ticket object
            ticketType.setEvent(event);
            ticketTypesList.add(ticketType);
        }

        //Add ticket types in the new event object
        event.setTickets(ticketTypesList);

        return eventRepository.save(event);
    }

    //Find all events
    @Override
    public List<Event> getAllEvents() {

        return eventRepository.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    //Update event
    @Override
    public Event updateEvent(Long id, EventUpdateDto eventUpdateDto) {
        //Find existing event
        Event existingEvent = eventRepository.findById(id).orElse(null);

        //check if existing event is null
        if (existingEvent == null) {
            return null;
        }

        //Get current values
        LocalDate currentStartingDate = existingEvent.getStartingDate();
        String currentImageLink = existingEvent.getCoverImageLink();
        String currentDescription = existingEvent.getDescription();

        //Check if dto has a new starting date
        if (eventUpdateDto.getStartingDate() == null) {
            existingEvent.setStartingDate(currentStartingDate);
        } else {
            existingEvent.setStartingDate(eventUpdateDto.getStartingDate());
        }

        //Check if dto has a new image link
        if (eventUpdateDto.getCoverImageLink() == null) {
            existingEvent.setCoverImageLink(currentImageLink);
        } else {
            existingEvent.setCoverImageLink(eventUpdateDto.getCoverImageLink());
        }

        //Check if dto has a new description
        if (eventUpdateDto.getDescription() == null) {
            existingEvent.setDescription(currentDescription);
        } else {
            existingEvent.setDescription(eventUpdateDto.getDescription());
        }

        return eventRepository.save(existingEvent);
    }

    //Delete an event
    @Override
    public Boolean deleteEvent(Long id) {
        //Check if an event exists
        boolean isExist = eventRepository.existsById(id);

        if (isExist) {
            eventRepository.deleteById(id);

            return true;
        }
        return false;
    }


}

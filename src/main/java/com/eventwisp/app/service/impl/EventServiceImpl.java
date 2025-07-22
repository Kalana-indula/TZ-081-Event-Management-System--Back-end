package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.dto.response.EventCreateResponse;
import com.eventwisp.app.dto.response.FindEventByOrganizerResponse;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.EventCategory;
import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.entity.Ticket;
import com.eventwisp.app.repository.EventCategoryRepository;
import com.eventwisp.app.repository.EventRepository;
import com.eventwisp.app.repository.OrganizerRepository;
import com.eventwisp.app.repository.TicketRepository;
import com.eventwisp.app.service.EventService;
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

    private OrganizerRepository organizerRepository;


    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            EventCategoryRepository eventCategoryRepository,
                            TicketRepository ticketRepository,
                            OrganizerRepository organizerRepository) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.ticketRepository = ticketRepository;
        this.organizerRepository=organizerRepository;
    }

    //Create a new event
    @Override
    public EventCreateResponse createEvent(EventDto eventDto) {

        EventCreateResponse response=new EventCreateResponse();
        //find event category
        EventCategory category = eventCategoryRepository.findById(eventDto.getEventCategoryId()).orElse(null);

        //Find organizer
        Organizer organizer=organizerRepository.findById(eventDto.getOrganizerId()).orElse(null);

        if(category==null){
            response.setMessage("Invalid Category ");
            return response;
        }

        if(organizer==null){
            response.setMessage("Invalid organizer");
            return response;
        }

        //Create new 'Event' object
        Event event = new Event();

        event.setEventName(eventDto.getEventName());
        event.setStartingDate(eventDto.getStartingDate());
        event.setEventCategory(category);
        event.setCoverImageLink(eventDto.getCoverImageLink());
        event.setDescription(eventDto.getDescription());
        event.setOrganizer(organizer);
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
        eventRepository.save(event);

        response.setMessage("Event created successfully");
        response.setEvent(event);

        return response;
    }

    //Find all events
    @Override
    public List<Event> getAllEvents() {

        return eventRepository.findAll();
    }

    @Override
    public Integer getAllOnGoingEventsCount() {
        return eventRepository.findAllOnGoingEvents().size();
    }


    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    //Find event by organizer
    @Override
    public FindEventByOrganizerResponse findEventByOrganizer(Long organizerId) {

        FindEventByOrganizerResponse response=new FindEventByOrganizerResponse();

        //Check if organizer exists
        boolean isExist=organizerRepository.existsById(organizerId);

        if(!isExist){
            response.setMessage("No organizer found for entered id");
            return response;
        }

        //Find events by organizer
        List<Event> eventList=eventRepository.eventsByOrganizer(organizerId);

        if(eventList.isEmpty()){
            response.setMessage("No events found for the organizer");
            return response;
        }

        response.setMessage("Events List");
        response.setEventsList(eventList);

        return response;
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

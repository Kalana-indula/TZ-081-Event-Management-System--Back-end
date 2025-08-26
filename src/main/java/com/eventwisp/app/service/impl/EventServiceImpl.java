package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.dto.ManagersEventDto;
import com.eventwisp.app.dto.event.EventStatusDto;
import com.eventwisp.app.dto.response.EventCreateResponse;
import com.eventwisp.app.dto.response.FindEventByOrganizerResponse;
import com.eventwisp.app.dto.response.ManagersEventsResponse;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.dto.response.general.UpdateResponse;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.EventCategory;
import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.entity.Ticket;
import com.eventwisp.app.repository.*;
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

    private EventStatusRepository eventStatusRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository,
                            EventCategoryRepository eventCategoryRepository,
                            TicketRepository ticketRepository,
                            OrganizerRepository organizerRepository,
                            EventStatusRepository eventStatusRepository) {
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.ticketRepository = ticketRepository;
        this.organizerRepository = organizerRepository;
        this.eventStatusRepository = eventStatusRepository;
    }

    //Create a new event
    @Override
    public EventCreateResponse createEvent(EventDto eventDto) {

        EventCreateResponse response = new EventCreateResponse();
        //find event category
        EventCategory category = eventCategoryRepository.findById(eventDto.getEventCategoryId()).orElse(null);

        //Find organizer
        Organizer organizer = organizerRepository.findById(eventDto.getOrganizerId()).orElse(null);

        if (category == null) {
            response.setMessage("Invalid Category ");
            return response;
        }

        if (organizer == null) {
            response.setMessage("Invalid organizer");
            return response;
        }

        //Create new 'Event' object
        Event event = new Event();

        event.setEventName(eventDto.getEventName());
        event.setStartingDate(eventDto.getStartingDate());
        event.setDateAdded(LocalDate.now());
        event.setEventCategory(category);
        event.setEventStatus(eventStatusRepository.findById(1L).orElse(null));
        event.setCoverImageLink(eventDto.getCoverImageLink());
        event.setDescription(eventDto.getDescription());
        event.setOrganizer(organizer);

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

    //event details list for manager dashboard
    @Override
    public ManagersEventsResponse getManagerEventList() {

        //create a response
        ManagersEventsResponse response = new ManagersEventsResponse();

        //get all events
        List<Event> eventsList = eventRepository.findAll();

        if (eventsList.isEmpty()) {
            response.setMessage("No events found");
            return response;
        }

        List<ManagersEventDto> managerSideEvents = new ArrayList<>();

        for (Event event : eventsList) {
            ManagersEventDto eventDetails = new ManagersEventDto();

            eventDetails.setEventId(event.getId());
            eventDetails.setEventName(event.getEventName());
            eventDetails.setEventType(event.getEventCategory().getCategory());
            eventDetails.setOrganizer(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName());
            eventDetails.setDateAdded(event.getDateAdded());
            eventDetails.setStatus(event.getEventStatus().getStatusName());

            //check event status
//            if(event.getIsCompleted()){
//                eventDetails.setStatus("Completed");
//            }else if(event.getIsStarted()){
//                eventDetails.setStatus("On Going");
//            }else if(event.getIsDisapproved()){
//                eventDetails.setStatus("Disapproved");
//            }else if(event.getIsApproved()){
//                    eventDetails.setStatus("Approved");
//            }else{
//                eventDetails.setStatus("Pending Approval");
//            }

            managerSideEvents.add(eventDetails);
        }

        response.setEventDetails(managerSideEvents);
        response.setMessage("Event details fetched successfully");

        return response;
    }

    @Override
    public Integer getAllOnGoingEventsCount() {
        return eventRepository.findAllOnGoingEvents().size();
    }


    @Override
    public Event getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public ManagersEventsResponse getEventDetailsById(Long eventId) {
        // Create a response object
        ManagersEventsResponse response = new ManagersEventsResponse();

        // Retrieve event by ID
        Event event = eventRepository.findById(eventId).orElse(null);

        // Check if event exists
        if (event == null) {
            response.setMessage("Event not found");
            return response;
        }

        ManagersEventDto eventDetails = new ManagersEventDto();
        eventDetails.setEventId(event.getId());
        eventDetails.setEventName(event.getEventName());
        eventDetails.setEventType(event.getEventCategory().getCategory());
        eventDetails.setOrganizer(
                event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName()
        );
        eventDetails.setDateAdded(event.getDateAdded());
        eventDetails.setStatus(event.getEventStatus().getStatusName());

        // Wrap the single event in a list
        List<ManagersEventDto> eventDetailsList = new ArrayList<>();
        eventDetailsList.add(eventDetails);

        // Populate response
        response.setEventDetails(eventDetailsList);
        response.setMessage("Event details fetched successfully");

        return response;
    }

    //get a single event details
    @Override
    public SingleEntityResponse<ManagersEventDto> getSingleEventById(Long eventId) {

        SingleEntityResponse<ManagersEventDto> response= new SingleEntityResponse<>();

        Event existingEvent = eventRepository.findById(eventId).orElse(null);

        if(existingEvent == null) {
            response.setMessage("Event not found");
            return response;
        }

        ManagersEventDto eventDetails = new ManagersEventDto();

        eventDetails.setEventId(existingEvent.getId());
        eventDetails.setEventName(existingEvent.getEventName());
        eventDetails.setEventType(existingEvent.getEventCategory().getCategory());
        eventDetails.setOrganizer(existingEvent.getOrganizer().getFirstName()+ " " + existingEvent.getOrganizer().getLastName());
        eventDetails.setOrganizerId(existingEvent.getOrganizer().getId());
        eventDetails.setDateAdded(existingEvent.getDateAdded());
        eventDetails.setStartingDate(existingEvent.getStartingDate());
        eventDetails.setCoverImageLink(existingEvent.getCoverImageLink());
        eventDetails.setEventDescription(existingEvent.getDescription());
        eventDetails.setIsApproved(existingEvent.getIsApproved());
        eventDetails.setIsStarted(existingEvent.getIsStarted());
        eventDetails.setIsCompleted(existingEvent.getIsCompleted());
        eventDetails.setIsDisapproved(existingEvent.getIsDisapproved());
        eventDetails.setStatus(existingEvent.getEventStatus().getStatusName());

        response.setMessage("Event details fetched successfully");
        response.setEntityData(eventDetails);

        return response;
    }


    @Override
    public ManagersEventsResponse getEventsByStatus(Long statusId) {

        //response
        ManagersEventsResponse response = new ManagersEventsResponse();

        List<Event> eventsList= eventRepository.findEventByStatus(statusId);

        if(eventsList.isEmpty()){
            response.setMessage("No events found");
            return response;
        }

        List<ManagersEventDto> managerSideEvents = new ArrayList<>();

        for (Event event : eventsList) {
            ManagersEventDto eventDetails = new ManagersEventDto();

            eventDetails.setEventId(event.getId());
            eventDetails.setEventName(event.getEventName());
            eventDetails.setEventType(event.getEventCategory().getCategory());
            eventDetails.setOrganizer(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName());
            eventDetails.setDateAdded(event.getDateAdded());
            eventDetails.setStatus(event.getEventStatus().getStatusName());

            managerSideEvents.add(eventDetails);
        }

        response.setEventDetails(managerSideEvents);
        response.setMessage("Event details fetched successfully");

        return response;
    }

    //Find event by organizer
    @Override
    public FindEventByOrganizerResponse findEventByOrganizer(Long organizerId) {

        FindEventByOrganizerResponse response = new FindEventByOrganizerResponse();

        //Check if organizer exists
        boolean isExist = organizerRepository.existsById(organizerId);

        if (!isExist) {
            response.setMessage("No organizer found for entered id");
            return response;
        }

        //Find events by organizer
        List<Event> eventList = eventRepository.eventsByOrganizer(organizerId);

        if (eventList.isEmpty()) {
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

    @Override
    public UpdateResponse<Event> updateEventStatus(Long id, EventStatusDto eventStatusDto) {

        UpdateResponse<Event> response = new UpdateResponse<>();

        //check event if exists
        Event existingEvent = eventRepository.findById(id).orElse(null);

        if (existingEvent == null) {
            response.setMessage("No event found for entered id");
            return response;
        }

        existingEvent.setIsApproved(eventStatusDto.getIsApproved());
        existingEvent.setIsDisapproved(eventStatusDto.getIsDisapproved());
        existingEvent.setIsStarted(eventStatusDto.getIsStarted());
        existingEvent.setIsCompleted(eventStatusDto.getIsCompleted());

        if (existingEvent.getIsCompleted()) {
            existingEvent.setEventStatus(eventStatusRepository.findById(4L).orElse(null));
        } else if (existingEvent.getIsStarted()) {
            existingEvent.setEventStatus(eventStatusRepository.findById(3L).orElse(null));
        } else if (existingEvent.getIsDisapproved()) {
            existingEvent.setEventStatus(eventStatusRepository.findById(2L).orElse(null));
        } else if (existingEvent.getIsApproved()) {
            existingEvent.setEventStatus(eventStatusRepository.findById(5L).orElse(null));
        } else {
            existingEvent.setEventStatus(eventStatusRepository.findById(1L).orElse(null));
        }

        eventRepository.save(existingEvent);

        response.setMessage("Event updated successfully");
        response.setUpdatedData(existingEvent);

        return response;
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

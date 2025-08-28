package com.eventwisp.app.service;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.dto.EventDetailsDto;
import com.eventwisp.app.dto.event.EventCounts;
import com.eventwisp.app.dto.event.EventStatusDto;
import com.eventwisp.app.dto.response.EventCreateResponse;
import com.eventwisp.app.dto.response.FindEventByOrganizerResponse;
import com.eventwisp.app.dto.response.ManagersEventsResponse;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.dto.response.general.UpdateResponse;
import com.eventwisp.app.entity.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {

    EventCreateResponse createEvent(EventDto eventDto);

    List<Event> getAllEvents();

    //event details list for manager dashboard
    ManagersEventsResponse getManagerEventList();

    Integer getAllOnGoingEventsCount();

    EventCounts getEventCounts(Long organizerId);

    Event getEventById(Long id);

    ManagersEventsResponse getEventDetailsById(Long eventId);

    SingleEntityResponse<EventDetailsDto> getSingleEventById(Long eventId);

    ManagersEventsResponse getEventsByStatus(Integer statusId);

    //get events by organizer
    FindEventByOrganizerResponse findEventByOrganizer(Long organizerId);

    Event updateEvent(Long id, EventUpdateDto eventUpdateDto);

    //update event status
    UpdateResponse<Event> updateEventStatus(Long id, EventStatusDto eventStatusDto);

    Boolean deleteEvent(Long id);
}

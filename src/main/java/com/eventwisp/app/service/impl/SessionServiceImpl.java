package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.response.FindSessionByEventResponse;
import com.eventwisp.app.dto.response.general.MultipleEntityResponse;
import com.eventwisp.app.dto.sessionDto.CreateSessionDto;
import com.eventwisp.app.dto.sessionDto.SessionCardDto;
import com.eventwisp.app.dto.sessionDto.SessionDetailsDto;
import com.eventwisp.app.dto.sessionDto.SessionUpdateDto;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.EventCategory;
import com.eventwisp.app.entity.Session;
import com.eventwisp.app.repository.EventCategoryRepository;
import com.eventwisp.app.repository.EventRepository;
import com.eventwisp.app.repository.SessionRepository;
import com.eventwisp.app.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SessionServiceImpl implements SessionService {

    //Create an instance of sessionRepository and eventRepository
    private SessionRepository sessionRepository;

    private EventRepository eventRepository;

    private EventCategoryRepository eventCategoryRepository;

    //Constructor inject repositories
    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,
                              EventRepository eventRepository,
                              EventCategoryRepository eventCategoryRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
        this.eventCategoryRepository = eventCategoryRepository;
    }

    //create new session
    @Override
    @Transactional
    public Session createSession(CreateSessionDto createSessionDto) {

        Event existingEvent = eventRepository.findById(createSessionDto.getEventId()).orElse(null);

        //Check if an event exists by id
        if (existingEvent == null) {
            return null;
        }

        //get the available sessions from that event
        int sessionCount = sessionRepository.findSessionsByEvent(createSessionDto.getEventId()).size();

        int nextSessionNumber=sessionCount+1;

        //create a new session
        Session session = new Session();

        session.setSessionNumber("Session "+nextSessionNumber);
        session.setVenue(createSessionDto.getVenue());
        session.setDate(createSessionDto.getDate());
        session.setStartTime(createSessionDto.getStartTime());
        session.setEndTime(createSessionDto.getEndTime());
        session.setEvent(existingEvent);

        return sessionRepository.save(session);
    }

    //find all existing sessions
    @Override
    public MultipleEntityResponse<SessionCardDto> findAllSessions() {

        MultipleEntityResponse<SessionCardDto> response = new MultipleEntityResponse<>();

        //find sessions list
        List<Session> sessionList=sessionRepository.findSessionsByDateAddedDesc();

        if(sessionList.isEmpty()){
            response.setMessage("No sessions found for the date");
            return response;
        }

        //add sessions to the dto
        List<SessionCardDto> sessionDetails=sessionList.stream()
                .map(this::mapToSessionDto)
                .toList();

        response.setEntityList(sessionDetails);
        response.setRemarks("Latest sessions for : "+sessionList.size());
        response.setMessage("Sessions arranged by descending order");

        return response;
    }

    //Find all sessions relevant to an event
    @Override
    public FindSessionByEventResponse findSessionsByEvent(Long eventId) {
        //Create new response object
        FindSessionByEventResponse response = new FindSessionByEventResponse();

        //Check if there is an event for relevant id
        boolean isExist = eventRepository.existsById(eventId);

        if (!isExist) {
            response.setMessage("No relevant event found for entered id");
            return response;
        }

        //Get a session list
        List<Session> existingSessions = sessionRepository.findSessionsByEvent(eventId);

        if (existingSessions.isEmpty()) {
            response.setMessage("No sessions found for the event");
            return response;
        }

        List<SessionDetailsDto> sessionDetails = new ArrayList<>();

        for (Session session : existingSessions) {

            SessionDetailsDto sessionData = new SessionDetailsDto();

            sessionData.setId(session.getId());
            sessionData.setSessionNumber(session.getSessionNumber());
            sessionData.setVenue(session.getVenue());
            sessionData.setDate(session.getDate());
            sessionData.setStartTime(session.getStartTime());
            sessionData.setEndTime(session.getEndTime());
            sessionData.setAttendees(session.getAttendees());
            sessionData.setRevenue(session.getRevenue());
            sessionData.setProfit(session.getProfit());
            sessionData.setAttendees(session.getAttendees());
            sessionData.setRevenue(session.getRevenue());
            sessionData.setProfit(session.getProfit());

            sessionDetails.add(sessionData);
        }

        response.setMessage("Sessions list");
        response.setSessionList(sessionDetails);

        return response;
    }

    //update an existing sessions
    @Override
    @Transactional
    public Session updateSession(Long id, SessionUpdateDto sessionUpdateDto) {

        //find existing session
        Session existingSession = sessionRepository.findById(id).orElse(null);

        //check if there is an existing session
        if (existingSession == null) {
            return null;
        }

        existingSession.setVenue(sessionUpdateDto.getVenue());
        existingSession.setDate(sessionUpdateDto.getDate());
        existingSession.setStartTime(sessionUpdateDto.getStartTime());
        existingSession.setEndTime(sessionUpdateDto.getEndTime());

        return sessionRepository.save(existingSession);
    }

    //delete a session
    @Override
    public Boolean deleteSession(Long id) {

        //check if there is existing session
        boolean isExist = sessionRepository.existsById(id);

        if (!isExist) {
            return false;
        }

        sessionRepository.deleteById(id);

        return true;
    }

    @Override
    public MultipleEntityResponse<SessionCardDto> findUpcomingSessions(String categoryName) {

        MultipleEntityResponse<SessionCardDto> response = new MultipleEntityResponse<>();

        //check if the categoryExists
        boolean categoryExists = eventCategoryRepository.existsByCategory(categoryName);

        if (!categoryExists) {
            response.setMessage("Category not found: " + categoryName);
            return response;
        }

        //find category details
        EventCategory existingCategory=eventCategoryRepository.findByCategory(categoryName);

        //fetch sessions
        List<Session> sessionList=sessionRepository.findUpcomingSessionsByEventCategory(categoryName);

        //check if the sessions were found
        if(sessionList.isEmpty()){
            response.setMessage("No sessions found for the category: " + categoryName);
            response.setRemarks(existingCategory.getCategory());
            return response;
        }

        //Convert session entities to sessionCardDto object using the helper method
        List<SessionCardDto> sessionDetails=sessionList.stream()
                .map(this::mapToSessionDto)
                .toList();

        response.setEntityList(sessionDetails);
        response.setRemarks(existingCategory.getCategory());
        response.setMessage("Sessions List for : "+categoryName);

        return response;
    }

    //find the sessions list descending ordered by date added
    @Override
    public MultipleEntityResponse<SessionCardDto> findLatestSessions() {

        MultipleEntityResponse<SessionCardDto> response = new MultipleEntityResponse<>();

        //find sessions list
        List<Session> sessionList=sessionRepository.findSessionsByDateAddedDesc();

        if(sessionList.isEmpty()){
            response.setMessage("No sessions found for the date");
            return response;
        }

        //add sessions to the dto
        List<SessionCardDto> sessionDetails=sessionList.stream()
                .limit(8)//limit the maximum elements of the list to be 8
                .map(this::mapToSessionDto)
                .toList();

        response.setEntityList(sessionDetails);
        response.setRemarks("Latest sessions for : "+sessionDetails.size());
        response.setMessage("Sessions arranged by descending order");

        return response;
    }


    //create a helper method to convert sessions-> session details dto
    private SessionCardDto mapToSessionDto(Session session) {
        SessionCardDto dto = new SessionCardDto();

        dto.setId(session.getId());
        dto.setEventName(session.getEvent().getEventName());
        dto.setEventId(session.getEvent().getId());
        dto.setStartingDate(session.getDate());
        dto.setEventAddedDate(session.getEvent().getDateAdded());
        dto.setStartingTime(session.getStartTime());
        dto.setCoverImageLink(session.getEvent().getCoverImageLink());
        dto.setLocation(session.getVenue());
        dto.setCategoryName(session.getEvent().getEventCategory().getCategory());

        // Calculate minimum ticket price
        if (session.getEvent().getTickets() != null && !session.getEvent().getTickets().isEmpty()) {
            Double minPrice = session.getEvent().getTickets().stream()
                    .mapToDouble(ticket -> ticket.getPrice())
                    .min()
                    .orElse(0.0);
            dto.setMinTicketPrice(minPrice);
        } else {
            dto.setMinTicketPrice(0.0);
        }

        return dto;
    }
}

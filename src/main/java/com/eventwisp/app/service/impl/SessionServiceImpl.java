package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.response.FindSessionByEventResponse;
import com.eventwisp.app.dto.sessionDto.CreateSessionDto;
import com.eventwisp.app.dto.sessionDto.SessionDetailsDto;
import com.eventwisp.app.dto.sessionDto.SessionUpdateDto;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.Session;
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

    //Constructor inject repositories
    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository, EventRepository eventRepository) {
        this.sessionRepository = sessionRepository;
        this.eventRepository = eventRepository;
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
    public List<Session> findAllSessions() {
        return sessionRepository.findAll();
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
}

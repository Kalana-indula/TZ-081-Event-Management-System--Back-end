package com.eventwisp.app.service;

import com.eventwisp.app.dto.SessionDto;
import com.eventwisp.app.dto.SessionUpdateDto;
import com.eventwisp.app.dto.response.FindSessionByEventResponse;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.Session;
import com.eventwisp.app.repository.EventRepository;
import com.eventwisp.app.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SessionServiceImpl implements SessionService{

    //Create an instance of sessionRepository and eventRepository
    private SessionRepository sessionRepository;

    private EventRepository eventRepository;

    //Constructor inject repositories
    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository,EventRepository eventRepository){
        this.sessionRepository=sessionRepository;
        this.eventRepository=eventRepository;
    }

    //create new session
    @Override
    @Transactional
    public Session createSession(SessionDto sessionDto) {

        Event existingEvent=eventRepository.findById(sessionDto.getEventId()).orElse(null);

        //Check if a session exists by id
        if(existingEvent==null){
            return null;
        }

        //create a new session
        Session session=new Session();

        session.setVenue(sessionDto.getVenue());
        session.setDate(sessionDto.getDate());
        session.setStartTime(sessionDto.getStartTime());
        session.setEndTime(sessionDto.getEndTime());
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
        FindSessionByEventResponse response=new FindSessionByEventResponse();

        //Check if there is an event for relevant id
        boolean isExist=eventRepository.existsById(eventId);

        if(!isExist){
            response.setMessage("No relevant event found for entered id");
            return response;
        }

        //Get a session list
        List<Session> sessions=sessionRepository.findSessionsByEvent(eventId);

        if(sessions.isEmpty()){
            response.setMessage("No sessions found for the event");
            return response;
        }

        response.setMessage("Sessions list");
        response.setSessionList(sessions);

        return response;
    }

    //update an existing sessions
    @Override
    @Transactional
    public Session updateSession(Long id, SessionUpdateDto sessionUpdateDto) {

        //find existing session
        Session existingSession=sessionRepository.findById(id).orElse(null);

        //check if there is an existing session
        if(existingSession==null){
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
        boolean isExist=sessionRepository.existsById(id);

        if(!isExist){
            return false;
        }

        sessionRepository.deleteById(id);

        return true;
    }
}

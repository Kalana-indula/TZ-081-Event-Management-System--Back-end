package com.eventwisp.app.service;

import com.eventwisp.app.dto.sessionDto.CreateSessionDto;
import com.eventwisp.app.dto.sessionDto.SessionUpdateDto;
import com.eventwisp.app.dto.response.FindSessionByEventResponse;
import com.eventwisp.app.entity.Session;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionService {

    Session createSession(CreateSessionDto createSessionDto);

    List<Session> findAllSessions();

    FindSessionByEventResponse findSessionsByEvent(Long eventId);

    Session updateSession(Long id,SessionUpdateDto sessionUpdateDto);

    Boolean deleteSession(Long id);
}

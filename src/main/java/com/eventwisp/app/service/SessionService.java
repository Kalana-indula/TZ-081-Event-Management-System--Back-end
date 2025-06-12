package com.eventwisp.app.service;

import com.eventwisp.app.dto.SessionDto;
import com.eventwisp.app.dto.SessionUpdateDto;
import com.eventwisp.app.entity.Session;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SessionService {

    Session createSession(SessionDto sessionDto);

    List<Session> findAllSessions();

    List<Session> findSessionByEvent(Long eventId);

    Session updateSession(Long id,SessionUpdateDto sessionUpdateDto);

    Boolean deleteSession(Long id);
}

package com.eventwisp.app.repository;

import com.eventwisp.app.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session,Long> {

    //Custom method for find all Sessions by 'Event'
//    Here "JPQL" is used due to simplicity
    @Query("SELECT s FROM Session s WHERE s.event.id =:eventId")
    List<Session> findSessionsByEvent(Long eventId);
}

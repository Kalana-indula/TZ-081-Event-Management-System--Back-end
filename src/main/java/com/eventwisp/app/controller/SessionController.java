package com.eventwisp.app.controller;

import com.eventwisp.app.dto.SessionDto;
import com.eventwisp.app.dto.SessionUpdateDto;
import com.eventwisp.app.entity.Session;
import com.eventwisp.app.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class SessionController {

    //Instance of sessionService
    private SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService){
        this.sessionService=sessionService;
    }

    //create a new session
    @PostMapping("/sessions")
    public ResponseEntity<?> createSession(@RequestBody SessionDto sessionDto){
        try{
            Session session=sessionService.createSession(sessionDto);

            if(session==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(session);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find all existing sessions
    @GetMapping("/sessions")
    public ResponseEntity<?> findAllSessions(){
        try {
            List<Session> sessionList=sessionService.findAllSessions();

            if(sessionList.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No sessions found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(sessionList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Update session
    @PutMapping("/sessions/{id}")
    public ResponseEntity<?> updateSession(@PathVariable Long id,@RequestBody SessionUpdateDto sessionUpdateDto){
        try {
            Session updatedSession= sessionService.updateSession(id,sessionUpdateDto);

            if(updatedSession==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No session found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(updatedSession);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //delete a session
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id){
        try {
            boolean isDeleted= sessionService.deleteSession(id);

            if(!isDeleted){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Session not found");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Session deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

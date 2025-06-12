package com.eventwisp.app.controller;

import com.eventwisp.app.dto.EventDto;
import com.eventwisp.app.dto.EventUpdateDto;
import com.eventwisp.app.entity.Event;
import com.eventwisp.app.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class EventController {

    //Create an instance of "EventService"
    private EventService eventService;

    //Injecting an instance of "EventService"
    @Autowired
    public EventController(EventService eventService){
        this.eventService=eventService;
    }

    //Create a new event
    @PostMapping("/events")
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto){
        try{
            //create a new event
            Event newEvent= eventService.createEvent(eventDto);

            //Check if new event is null
            if(newEvent==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No valid event category was found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(eventService);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find all events
    @GetMapping("/events")
    public ResponseEntity<?> findAllEvents(){
        try{
            List<Event> events=eventService.getAllEvents();

            //Check if there are any events
            if(events.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No events found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(events);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find an event by id
    @GetMapping("/events/{id}")
    public ResponseEntity<?> findEventById(@PathVariable Long id){
        try{
            Event existingEvent= eventService.getEventById(id);

            //Check if event exists
            if(existingEvent==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No event found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(existingEvent);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Update an event
    @PutMapping("events/{id}")
    public ResponseEntity<?> updateEvent(@PathVariable Long id,@RequestBody EventUpdateDto eventUpdateDto){
        try{
            Event updatedEvent= eventService.updateEvent(id,eventUpdateDto);

            //Check if event exists
            if(updatedEvent==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No event found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(updatedEvent);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Delete an event
    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id){
        try {
            boolean isDeleted= eventService.deleteEvent(id);

            if(!isDeleted){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No event found");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Event deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}

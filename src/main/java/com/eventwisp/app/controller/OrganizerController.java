package com.eventwisp.app.controller;

import com.eventwisp.app.dto.OrganizerUpdateDto;
import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class OrganizerController {

    //Create an instance of "OrganizerService"
    private OrganizerService organizerService;

    //Inject an instance of "OrgnizerSerivce"
    @Autowired
    public OrganizerController(OrganizerService organizerService){
        this.organizerService=organizerService;
    }

    @PostMapping("/organizers")
    public ResponseEntity<?> addOrganizer(@RequestBody Organizer organizer){
        try {
            Organizer newOrganizer=organizerService.addOrganizer(organizer);
            return ResponseEntity.status(HttpStatus.OK).body(newOrganizer);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find all organizers
    @GetMapping("/organizers")
    public ResponseEntity<?> findAllOrganizers(){
        try {
            List<Organizer> allOrganizers=organizerService.getAllOrganizers();

            //Check if there any organizers available
            if(allOrganizers.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organizers found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(allOrganizers);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find organizer by id
    @GetMapping("/organizers/{id}")
    public ResponseEntity<?> findOrganizerById(@PathVariable Long id){
        try {
            Organizer organizer=organizerService.getOrganizerById(id);

            //Check if the 'organizer' is null
            if(organizer==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organizer found for the corresponding id");
            }
            return ResponseEntity.status(HttpStatus.OK).body(organizer);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Update organizer
    @PutMapping("/organizers/{id}")
    public ResponseEntity<?> updateOrganizer(@PathVariable Long id,@RequestBody OrganizerUpdateDto organizerUpdateDto){
        try{
            Organizer updatedOrganizer= organizerService.updateOrganizer(id,organizerUpdateDto);

            //Check if updatedOrganizer is null
            if(updatedOrganizer==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No organizer found for the corresponding id");
            }

            return ResponseEntity.status(HttpStatus.OK).body(updatedOrganizer);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/organizers/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        try{
            boolean isDeleted=organizerService.deleteOrganizer(id);

            if(isDeleted){
                return ResponseEntity.status(HttpStatus.OK).body("Organizer deleted successfully");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Organizer not found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

package com.eventwisp.app.controller;

import com.eventwisp.app.dto.ManagerUpdateDto;
import com.eventwisp.app.entity.Manager;
import com.eventwisp.app.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ManagerController {

    private ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService){
        this.managerService=managerService;
    }

    @PostMapping("/managers")
    public ResponseEntity<?> createManager(@RequestBody Manager manager){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(managerService.createManager(manager));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find all managers
    @GetMapping("/managers")
    public ResponseEntity<?> findAllManagers(){
        try {
            List<Manager> allManagers=managerService.findAllManagers();

            //Check if there any managers
            if(allManagers.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No manager found");
            }

            return ResponseEntity.status(HttpStatus.OK).body(allManagers);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Find managers by id
    @GetMapping("/managers/{id}")
    public ResponseEntity<?> findManagerById(@PathVariable Long id){
        try {
            Manager existingManager= managerService.findManagerById(id);

            //Check if there is a manger
            if(existingManager==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No manager found for entered id");
            }

            return ResponseEntity.status(HttpStatus.OK).body(existingManager);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Update a manager
    @PutMapping("/managers/{id}")
    public ResponseEntity<?> updateManager(@PathVariable Long id,@RequestBody ManagerUpdateDto managerUpdateDto){
        try {
            Manager updatedManager= managerService.updateManager(id,managerUpdateDto);

            //Check if the manager exists
            if(updatedManager==null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No manager found for entered id");
            }

            return ResponseEntity.status(HttpStatus.OK).body(updatedManager);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Delete a manager
    @DeleteMapping("/managers/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable Long id){
        try {
            boolean isDeleted= managerService.deleteManager(id);

            //Check if deleted
            if(!isDeleted){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No manager found for entered id");
            }

            return ResponseEntity.status(HttpStatus.OK).body("Manager deleted successfully");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

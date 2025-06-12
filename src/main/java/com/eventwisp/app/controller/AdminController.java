package com.eventwisp.app.controller;

import com.eventwisp.app.dto.AdminUpdateDto;
import com.eventwisp.app.entity.Admin;
import com.eventwisp.app.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class AdminController {


    //create an instance of 'AdminService'
    private AdminService adminService;

    //Inject an 'AdminService'
    @Autowired
    public AdminController(AdminService adminService){
        this.adminService=adminService;
    }

    @PostMapping("/admins")
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(adminService.createAdmin(admin));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/admins")
    public ResponseEntity<?> findAllAdmins(){
        try {
            //Get all admins
            List<Admin> allAdmins=adminService.getAllAdmins();

            //Check if the admins list is empty
            if(!allAdmins.isEmpty()){
                //If true, return all available admins
                return ResponseEntity.status(HttpStatus.OK).body(allAdmins);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admins found");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/admins/{id}")
    public ResponseEntity<?> findAdminById(@PathVariable Long id){
        try {

            Admin existingAdmin= adminService.findAdminById(id);
            //Check if the existing admin is null
            if(existingAdmin!=null){
                return ResponseEntity.status(HttpStatus.OK).body(existingAdmin);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admin found for entered id");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/admins/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id,@RequestBody AdminUpdateDto adminUpdateDto){
        try {
            Admin updatedAdmin= adminService.updateAdmin(id,adminUpdateDto);
            //Check if the admin available for entered id
            if(updatedAdmin!=null){
                return ResponseEntity.status(HttpStatus.OK).body(updatedAdmin);
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admin found for entered id");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/admins/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id){
        try {
            Boolean isDeleted=adminService.deleteAdmin(id);
            //Check if the admins is deleted
            if(isDeleted){
                return ResponseEntity.status(HttpStatus.OK).body("Admin deleted successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admin found for entered id");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}

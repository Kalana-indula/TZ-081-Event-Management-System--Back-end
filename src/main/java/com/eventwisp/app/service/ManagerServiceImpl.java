package com.eventwisp.app.service;

import com.eventwisp.app.dto.ManagerUpdateDto;
import com.eventwisp.app.entity.Manager;
import com.eventwisp.app.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService{

    //creating an instance of manager repository
    private ManagerRepository managerRepository;

    //injecting 'ManagerRepository'
    @Autowired
    public ManagerServiceImpl(ManagerRepository managerRepository){
        this.managerRepository=managerRepository;
    }

    //Create a new manager
    @Override
    public Manager createManager(Manager manager) {
        return managerRepository.save(manager);
    }

    //Find all available managers
    @Override
    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    //Find manager by id
    @Override
    public Manager findManagerById(Long id) {
        return managerRepository.findById(id).orElse(null);
    }

    //Update an existing 'Manager'
    @Override
    public Manager updateManager(Long id, ManagerUpdateDto managerUpdateDto) {

        //Find an existing 'Manager'
        Manager existingManager=managerRepository.findById(id).orElse(null);

        //Check if the 'Manager' exists
        if(existingManager==null){
            return null;
        }

        //Get current values
        String currentPhone= existingManager.getPhone();
        String currentEmail= existingManager.getEmail();
        String currentPassword= existingManager.getPassword();
        boolean currentStatus=existingManager.getIsAssigned();

        //Check if dto has a new phone number
        if(managerUpdateDto.getPhone()==null || managerUpdateDto.getPhone().isEmpty()){
            //If false set current phone number
            existingManager.setPhone(currentPhone);
        }else {
            //If true set new phone number
            existingManager.setPhone(managerUpdateDto.getPhone());
        }

        //Check if dto has a new email
        if(managerUpdateDto.getEmail()==null || managerUpdateDto.getEmail().isEmpty()){
            //If false set current email
            existingManager.setEmail(currentEmail);
        }else{
            //If true set new email
            existingManager.setEmail(managerUpdateDto.getEmail());
        }

        //Check if dto has a new password
        if(managerUpdateDto.getPassword()==null || managerUpdateDto.getPassword().isEmpty()){
            //If false set current password
            existingManager.setPassword(currentPassword);
        }else {
            //If true set new password
            existingManager.setPassword(managerUpdateDto.getPassword());
        }

        //Set status
        existingManager.setIsAssigned(managerUpdateDto.getIsAssigned());

        return managerRepository.save(existingManager);
    }

    //Delete an existing manager
    @Override
    public Boolean deleteManager(Long id) {

        //Check if a 'Manager' exists
        boolean isExist=managerRepository.existsById(id);

        if(isExist){
            managerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

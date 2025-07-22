package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.ManagerUpdateDto;
import com.eventwisp.app.dto.response.general.UpdateResponse;
import com.eventwisp.app.entity.Manager;
import com.eventwisp.app.repository.ManagerRepository;
import com.eventwisp.app.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ManagerServiceImpl implements ManagerService {

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

    //find currently assigned manager
    @Override
    public Manager findAssignedManager() {
        return managerRepository.findAssignedManager();
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


        //Check if dto has a new phone number
        if(managerUpdateDto.getPhone()==null || managerUpdateDto.getPhone().isEmpty()){
            //If true set current phone number
            existingManager.setPhone(currentPhone);
        }else {
            //If false set new phone number
            existingManager.setPhone(managerUpdateDto.getPhone());
        }

        //Check if dto has a new email
        if(managerUpdateDto.getEmail()==null || managerUpdateDto.getEmail().isEmpty()){
            //If true set current email
            existingManager.setEmail(currentEmail);
        }else{
            //If false set new email
            existingManager.setEmail(managerUpdateDto.getEmail());
        }

        //Check if dto has a new password
        if(managerUpdateDto.getPassword()==null || managerUpdateDto.getPassword().isEmpty()){
            //If true set current password
            existingManager.setPassword(currentPassword);
        }else {
            //If false set new password
            existingManager.setPassword(managerUpdateDto.getPassword());
        }

        return managerRepository.save(existingManager);
    }

    @Override
    public UpdateResponse<Manager> setManagerStatus(Long id) {

        UpdateResponse<Manager> managerUpdateResponse=new UpdateResponse<>();

        //find existing manager
        Manager existingManager=managerRepository.findById(id).orElse(null);

        if(existingManager==null){
            managerUpdateResponse.setMessage("Manager not found");

            return managerUpdateResponse;
        }

        //find currently assigned manager
        Manager assignedManager=managerRepository.findAssignedManager();

        if((assignedManager!=null) && (Objects.equals(assignedManager.getId(), id))){
            assignedManager.setIsAssigned(false);
            managerRepository.save(assignedManager);

            managerUpdateResponse.setMessage("Manager is un-assigned");
            managerUpdateResponse.setUpdatedData(assignedManager);

            return managerUpdateResponse;
        }

        if(assignedManager==null){
            existingManager.setIsAssigned(true);
            managerRepository.save(existingManager);

            managerUpdateResponse.setMessage("Manager is assigned");
            managerUpdateResponse.setUpdatedData(existingManager);

            return managerUpdateResponse;
        }

        assignedManager.setIsAssigned(false);

        //un-assigned currently assigned manager
        managerRepository.save(assignedManager);

        //assign new manager
        existingManager.setIsAssigned(true);

        managerRepository.save(existingManager);

        managerUpdateResponse.setMessage("Manager is assigned");
        managerUpdateResponse.setUpdatedData(existingManager);

        return managerUpdateResponse;

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

package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.OrganizerUpdateDto;
import com.eventwisp.app.dto.organizer.OrganizerDetailsDto;
import com.eventwisp.app.dto.organizer.OrganizerStatusDto;
import com.eventwisp.app.dto.response.general.MultipleEntityResponse;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.dto.response.general.UpdateResponse;
import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.repository.OrganizerRepository;
import com.eventwisp.app.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizerServiceImpl implements OrganizerService {

    //Create a 'OrganizerRepository' instance
    private OrganizerRepository organizerRepository;

    //Inject an instance of 'OrganizerRepository'
    @Autowired
    public OrganizerServiceImpl(OrganizerRepository organizerRepository){
        this.organizerRepository=organizerRepository;
    }

    //create a new organizer
    @Override
    public Organizer addOrganizer(Organizer organizer) {
        return organizerRepository.save(organizer);
    }

    //find all organizers
    @Override
    public List<Organizer> getAllOrganizers() {

        return organizerRepository.findAll();
    }

    @Override
    public MultipleEntityResponse<OrganizerDetailsDto> getAllOrganizersDetails() {

        //existing organizers
        List<Organizer> existingOrganizers=organizerRepository.findAll();

        //response
        MultipleEntityResponse<OrganizerDetailsDto> response=new MultipleEntityResponse<>();

        if(existingOrganizers.isEmpty()){
            response.setMessage("No organizer found");
            return response;
        }

        //organizer details
        List<OrganizerDetailsDto> organizersDetails=new ArrayList<>();

        for(Organizer organizer:existingOrganizers){
            OrganizerDetailsDto details=new OrganizerDetailsDto();

            details.setId(organizer.getId());
            details.setName(organizer.getFirstName()+" "+organizer.getLastName());
            details.setNic(organizer.getNic());
            details.setEmail(organizer.getEmail());
            details.setPhone(organizer.getPhone());
            details.setPendingApproval(organizer.getPendingApproval());
            details.setIsApproved(organizer.getIsApproved());
            details.setIsDisapproved(organizer.getIsDisapproved());

            organizersDetails.add(details);
        }

        response.setEntityList(organizersDetails);
        response.setMessage("Organizer details found");

        return response;
    }

    //get organizer count
    @Override
    public Integer getOrganizerCount() {

        List<Organizer> organizerList=organizerRepository.findAll();


        return organizerList.size();
    }

    //Find an organizer by id
    @Override
    public SingleEntityResponse<OrganizerDetailsDto> getOrganizerDetailsById(Long id) {
        SingleEntityResponse<OrganizerDetailsDto> response = new SingleEntityResponse<>();

        // Find the organizer by ID
        Organizer organizer = organizerRepository.findById(id).orElse(null);

        if (organizer == null) {
            response.setMessage("Organizer not found with ID: " + id);
            return response;
        }

        // Create the details DTO
        OrganizerDetailsDto details = new OrganizerDetailsDto();
        details.setId(organizer.getId());
        details.setName(organizer.getFirstName() + " " + organizer.getLastName());
        details.setNic(organizer.getNic());
        details.setCompanyName(organizer.getCompanyName());
        details.setEmail(organizer.getEmail());
        details.setPhone(organizer.getPhone());
        details.setPendingApproval(organizer.getPendingApproval());
        details.setIsApproved(organizer.getIsApproved());
        details.setIsDisapproved(organizer.getIsDisapproved());

        // Set the response
        response.setMessage("Organizer details found");
        response.setEntityData(details);

        return response;
    }

    //find pending organizer accounts
    @Override
    public MultipleEntityResponse<OrganizerDetailsDto> getPendingOrganizers() {

        MultipleEntityResponse<OrganizerDetailsDto> response=new MultipleEntityResponse<>();

        List<Organizer> pendingAccounts=organizerRepository.pendingOrganizers();

        if(pendingAccounts.isEmpty()){
            response.setMessage("No pending organizer found");
            return response;
        }

        List<OrganizerDetailsDto> organizersDetails=new ArrayList<>();

        for(Organizer organizer:pendingAccounts){
            OrganizerDetailsDto details=new OrganizerDetailsDto();

            details.setId(organizer.getId());
            details.setName(organizer.getFirstName()+" "+organizer.getLastName());
            details.setNic(organizer.getNic());
            details.setEmail(organizer.getEmail());
            details.setPhone(organizer.getPhone());
            details.setPendingApproval(organizer.getPendingApproval());
            details.setIsApproved(organizer.getIsApproved());
            details.setIsDisapproved(organizer.getIsDisapproved());
            
            organizersDetails.add(details);
        }
        
        response.setEntityList(organizersDetails);
        response.setMessage("Organizer details found");

        return response;
    }

    //find approved organizer accounts
    @Override
    public MultipleEntityResponse<OrganizerDetailsDto> getApprovedOrganizers() {
        MultipleEntityResponse<OrganizerDetailsDto> response = new MultipleEntityResponse<>();

        List<Organizer> approvedAccounts = organizerRepository.approvedOrganizers();

        if(approvedAccounts.isEmpty()) {
            response.setMessage("No approved organizers found");
            return response;
        }

        List<OrganizerDetailsDto> organizersDetails = new ArrayList<>();

        for(Organizer organizer : approvedAccounts) {
            OrganizerDetailsDto details = new OrganizerDetailsDto();

            details.setId(organizer.getId());
            details.setName(organizer.getFirstName() + " " + organizer.getLastName());
            details.setNic(organizer.getNic());
            details.setEmail(organizer.getEmail());
            details.setPhone(organizer.getPhone());
            details.setPendingApproval(organizer.getPendingApproval());
            details.setIsApproved(organizer.getIsApproved());
            details.setIsDisapproved(organizer.getIsDisapproved());

            organizersDetails.add(details);
        }

        response.setEntityList(organizersDetails);
        response.setMessage("Approved organizer details found");

        return response;
    }

    //find disapproved organizer accounts
    @Override
    public MultipleEntityResponse<OrganizerDetailsDto> getDisapprovedOrganizers() {
        MultipleEntityResponse<OrganizerDetailsDto> response = new MultipleEntityResponse<>();

        List<Organizer> disapprovedAccounts = organizerRepository.disapprovedOrganizers();

        if(disapprovedAccounts.isEmpty()) {
            response.setMessage("No disapproved organizers found");
            return response;
        }

        List<OrganizerDetailsDto> organizersDetails = new ArrayList<>();

        for(Organizer organizer : disapprovedAccounts) {
            OrganizerDetailsDto details = new OrganizerDetailsDto();

            details.setId(organizer.getId());
            details.setName(organizer.getFirstName() + " " + organizer.getLastName());
            details.setNic(organizer.getNic());
            details.setEmail(organizer.getEmail());
            details.setPhone(organizer.getPhone());
            details.setPendingApproval(organizer.getPendingApproval());
            details.setIsApproved(organizer.getIsApproved());
            details.setIsDisapproved(organizer.getIsDisapproved());

            organizersDetails.add(details);
        }

        response.setEntityList(organizersDetails);
        response.setMessage("Disapproved organizer details found");

        return response;
    }

    //Update an existing organizer
    @Override
    public Organizer updateOrganizer(Long id, OrganizerUpdateDto organizerUpdateDto) {
        //Find the existing organizer
        Organizer existingOrganizer=organizerRepository.findById(id).orElse(null);

        if(existingOrganizer==null){
            return null;
        }

        //Add new values
        existingOrganizer.setCompanyName(organizerUpdateDto.getCompanyName());
        existingOrganizer.setPhone(organizerUpdateDto.getPhone());
        existingOrganizer.setEmail(organizerUpdateDto.getEmail());
        existingOrganizer.setPassword(organizerUpdateDto.getPassword());

        return organizerRepository.save(existingOrganizer);
    }

    //update organizer status
    @Override
    public UpdateResponse<OrganizerDetailsDto> updateOrganizerStatus(Long organizerId, OrganizerStatusDto organizerStatusDto) {

        UpdateResponse<OrganizerDetailsDto> response=new UpdateResponse<>();

        //find the organizer
        Organizer existingOrganizer=organizerRepository.findById(organizerId).orElse(null);

        if(existingOrganizer==null){
            response.setMessage("Organizer not found");
            return response;
        }

        existingOrganizer.setPendingApproval(organizerStatusDto.getPendingApproval());
        existingOrganizer.setIsApproved(organizerStatusDto.getIsApproved());
        existingOrganizer.setIsDisapproved(organizerStatusDto.getIsDisapproved());
        organizerRepository.save(existingOrganizer);

        OrganizerDetailsDto updatedDetails=new OrganizerDetailsDto();

        updatedDetails.setId(existingOrganizer.getId());
        updatedDetails.setName(existingOrganizer.getFirstName()+" "+existingOrganizer.getLastName());
        updatedDetails.setNic(existingOrganizer.getNic());
        updatedDetails.setEmail(existingOrganizer.getEmail());
        updatedDetails.setPhone(existingOrganizer.getPhone());
        updatedDetails.setPendingApproval(existingOrganizer.getPendingApproval());
        updatedDetails.setIsApproved(existingOrganizer.getIsApproved());
        updatedDetails.setIsDisapproved(existingOrganizer.getIsDisapproved());

        response.setMessage("Organizer status updated");
        response.setUpdatedData(updatedDetails);

        return response;
    }

    //Update
    @Override
    public Boolean deleteOrganizer(Long id) {
        //Check if the organizer exists
        boolean isExist=organizerRepository.existsById(id);

        if(isExist){
            organizerRepository.deleteById(id);

            return true;
        }
        return false;
    }
}

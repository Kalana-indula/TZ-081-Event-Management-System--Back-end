package com.eventwisp.app.service;

import com.eventwisp.app.dto.OrganizerUpdateDto;
import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizerServiceImpl implements OrganizerService{

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

    //Find an organizer by id
    @Override
    public Organizer getOrganizerById(Long id) {
        return organizerRepository.findById(id).orElse(null);
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

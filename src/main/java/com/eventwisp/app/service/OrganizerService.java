package com.eventwisp.app.service;

import com.eventwisp.app.dto.OrganizerUpdateDto;
import com.eventwisp.app.entity.Organizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizerService {
    Organizer addOrganizer(Organizer organizer);

    List<Organizer> getAllOrganizers();

    Organizer getOrganizerById(Long id);

    Organizer updateOrganizer(Long id, OrganizerUpdateDto organizerUpdateDto);

    Boolean deleteOrganizer(Long id);
}

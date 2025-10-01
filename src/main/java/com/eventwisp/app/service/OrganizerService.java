package com.eventwisp.app.service;

import com.eventwisp.app.dto.OrganizerUpdateDto;
import com.eventwisp.app.dto.organizer.EarningDetails;
import com.eventwisp.app.dto.organizer.OrganizerDetailsDto;
import com.eventwisp.app.dto.organizer.OrganizerStatusDto;
import com.eventwisp.app.dto.response.general.MultipleEntityResponse;
import com.eventwisp.app.dto.response.general.SingleEntityResponse;
import com.eventwisp.app.dto.response.general.UpdateResponse;
import com.eventwisp.app.entity.Organizer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrganizerService {
    Organizer addOrganizer(Organizer organizer);

    List<Organizer> getAllOrganizers();

    MultipleEntityResponse<OrganizerDetailsDto> getAllOrganizersDetails();

    Integer getOrganizerCount();

    SingleEntityResponse<OrganizerDetailsDto> getOrganizerDetailsById(Long id);

    //find all pending organizer accounts
    MultipleEntityResponse<OrganizerDetailsDto> getPendingOrganizers();

    //find all approved organizer accounts
    MultipleEntityResponse<OrganizerDetailsDto> getApprovedOrganizers();

    //find all disapproved organizer accounts
    MultipleEntityResponse<OrganizerDetailsDto> getDisapprovedOrganizers();

    //get earnings by organizer
    SingleEntityResponse<EarningDetails> getEarningsByOrganizer(Long organizerId);

    //get earnings by all organizers
    MultipleEntityResponse<EarningDetails> getEarningsByAllOrganizers();

    Organizer updateOrganizer(Long id, OrganizerUpdateDto organizerUpdateDto);

    UpdateResponse<OrganizerDetailsDto> updateOrganizerStatus(Long id, OrganizerStatusDto organizerStatusDto);

    Boolean deleteOrganizer(Long id);
}

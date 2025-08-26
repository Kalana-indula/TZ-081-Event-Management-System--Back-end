package com.eventwisp.app.repository;

import com.eventwisp.app.entity.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizerRepository extends JpaRepository<Organizer,Long> {

    //find pending approval organizer accounts
    @Query("SELECT o FROM Organizer o WHERE o.pendingApproval = true")
    List<Organizer> pendingOrganizers();

    //find approved organizers
    @Query("SELECT o FROM Organizer o WHERE o.isApproved = true")
    List<Organizer> approvedOrganizers();

    //find disapprove organizers
    @Query("SELECT o FROM Organizer o WHERE o.isDisapproved = true")
    List<Organizer> disapprovedOrganizers();
}

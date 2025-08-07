package com.eventwisp.app.repository;

import com.eventwisp.app.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    //fetch manager of whom 'isAssigned = true'
    @Query("SELECT m FROM Manager m WHERE m.isAssigned = true")
    Manager findAssignedManager();
}

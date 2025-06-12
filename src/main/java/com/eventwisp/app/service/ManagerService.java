package com.eventwisp.app.service;

import com.eventwisp.app.dto.ManagerUpdateDto;
import com.eventwisp.app.entity.Manager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ManagerService {

    Manager createManager(Manager manager);

    List<Manager> findAllManagers();

    Manager findManagerById(Long id);

    Manager updateManager(Long id, ManagerUpdateDto managerUpdateDto);

    Boolean deleteManager(Long id);
}

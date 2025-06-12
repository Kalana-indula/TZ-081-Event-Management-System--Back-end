package com.eventwisp.app.service;

import com.eventwisp.app.dto.AdminUpdateDto;
import com.eventwisp.app.entity.Admin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    Admin createAdmin(Admin admin);

    List<Admin> getAllAdmins();

    Admin findAdminById(Long id);

    Admin updateAdmin(Long id, AdminUpdateDto adminUpdateDto);

    Boolean deleteAdmin(Long id);
}

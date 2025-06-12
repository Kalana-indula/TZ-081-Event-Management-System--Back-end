package com.eventwisp.app.service;

import com.eventwisp.app.dto.AdminUpdateDto;
import com.eventwisp.app.entity.Admin;
import com.eventwisp.app.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService{

    //get an instance of 'AdminRepository'
    private AdminRepository adminRepository;

    //inject 'AdminRepository'
    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository){
        this.adminRepository=adminRepository;
    }

    //create a new 'Admin'
    @Override
    public Admin createAdmin(Admin admin){
        return adminRepository.save(admin);
    }

    //find all available admins
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    //Find an 'Admin' by id
    @Override
    public Admin findAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }

//    Update an existing 'Admin'
    @Override
    public Admin updateAdmin(Long id, AdminUpdateDto adminUpdateDto) {

        //Find existing 'Admin'
        Admin existingAdmin=adminRepository.findById(id).orElse(null);

        if(existingAdmin==null){
            return null;
        }

        //get current details
        String currentPhone=existingAdmin.getPhone();
        String currentEmail=existingAdmin.getEmail();
        String currentPassword=existingAdmin.getPassword();

        //check if the update dto has a new phone number
        if(adminUpdateDto.getPhone()!=null){
            //If true, set the new phone number
            existingAdmin.setPhone(adminUpdateDto.getPhone());
        }else{
            //If false, set the existing number
            existingAdmin.setPhone(currentPhone);
        }

        //Check if the update dto has a new email
        if(adminUpdateDto.getEmail()!=null){
            //If true, set the new email
            existingAdmin.setEmail(adminUpdateDto.getEmail());
        }else{
            //If false, set existing email
            existingAdmin.setEmail(currentEmail);
        }

        //Check if the update dto has a new password
        if(adminUpdateDto.getPassword()!=null){
            //If true, set the new password
            existingAdmin.setPassword(adminUpdateDto.getPassword());
        }else{
            //If false, set the existing password
            existingAdmin.setPassword(currentPassword);
        }

        return adminRepository.save(existingAdmin);
    }

    //delete an existing admin
    @Override
    public Boolean deleteAdmin(Long id) {
        //Check if the admin exist by id
        if(adminRepository.existsById(id)){
            //If true delete
            adminRepository.deleteById(id);

            return true;
        }

        return false;
    }
}

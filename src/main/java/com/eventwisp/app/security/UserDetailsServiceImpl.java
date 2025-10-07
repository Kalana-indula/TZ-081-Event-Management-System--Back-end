package com.eventwisp.app.security;

import com.eventwisp.app.entity.Organizer;
import com.eventwisp.app.repository.OrganizerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//fetch user details and build a user
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private OrganizerRepository organizerRepository;

    @Autowired
    public UserDetailsServiceImpl(OrganizerRepository organizerRepository){
        this.organizerRepository = organizerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){

        Organizer organizer = organizerRepository.findByEmail(email).orElse(null);

        //build UserDetails object if organizer available
        if(organizer == null){
            throw new UsernameNotFoundException("User not found for email: " + email);
        }

        //create a user object from user details
        return org.springframework.security.core.userdetails.User.builder()
                .username(organizer.getEmail()) //in here 'email' is taken as username
                .password(organizer.getPassword())
                .build();
    }
}

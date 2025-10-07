package com.eventwisp.app.controller.auth;

import com.eventwisp.app.dto.organizer.CreateOrganizerDto;
import com.eventwisp.app.dto.organizer.OrganizerLoginDto;
import com.eventwisp.app.repository.OrganizerRepository;
import com.eventwisp.app.security.jwt.JwtUtils;
import com.eventwisp.app.service.OrganizerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class OrganizerAuthController {

    private OrganizerRepository organizerRepository;

    private OrganizerService organizerService;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    @Autowired
    public OrganizerAuthController(OrganizerRepository organizerRepository,
                                   OrganizerService organizerService,
                                   PasswordEncoder passwordEncoder,
                                   AuthenticationManager authenticationManager,
                                   JwtUtils jwtUtils){
        this.organizerRepository = organizerRepository;
        this.organizerService = organizerService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    //register organizer
    @PostMapping("/organizers")
    public ResponseEntity<?> registerOrganizer(@RequestBody CreateOrganizerDto createOrganizerDto){

        if (organizerRepository.existsByEmail(createOrganizerDto.getEmail())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email Already Exists");
        }

        //encode the password
        createOrganizerDto.setPassword(passwordEncoder.encode(createOrganizerDto.getPassword()));

        return ResponseEntity.status(HttpStatus.OK).body(organizerService.addOrganizer(createOrganizerDto));
    }

    //login as organizer
    @PostMapping("/organizers/login")
    public ResponseEntity<?> loginAsOrganizer(@RequestBody OrganizerLoginDto organizerLoginDto){
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(organizerLoginDto.getEmail(), organizerLoginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.status(HttpStatus.OK).body(jwt);
    }

}

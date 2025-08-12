package com.eventwisp.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "organizer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nic")
    private String nic;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "is_approved")
    private Boolean isApproved=false;

    @Column(name = "is_disapproved")
    private Boolean isDisapproved=false;

    //Events of organizer
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "organizer")
    private List<Event> eventsList;
}

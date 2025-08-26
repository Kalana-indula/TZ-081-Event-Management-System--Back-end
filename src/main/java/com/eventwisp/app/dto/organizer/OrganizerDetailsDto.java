package com.eventwisp.app.dto.organizer;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrganizerDetailsDto {
    private Long id;
    private String name;
    private String nic;
    private String companyName;
    private String email;
    private String phone;
    private Boolean pendingApproval;
    private Boolean isApproved;
    private Boolean isDisapproved;
}

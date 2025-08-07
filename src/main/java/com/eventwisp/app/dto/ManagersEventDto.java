package com.eventwisp.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class ManagersEventDto {

    private Long eventId;
    private String eventName;
    private String eventType;
    private String organizer;
    private LocalDate dateAdded;
    private String status;
}

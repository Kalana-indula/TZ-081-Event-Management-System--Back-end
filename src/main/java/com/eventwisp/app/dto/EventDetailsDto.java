package com.eventwisp.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class EventDetailsDto {

    private Long eventId;
    private String eventName;
    private String eventType;
    private String organizer;
    private Long organizerId;
    private LocalDate dateAdded;
    private LocalDate startingDate;
    private String coverImageLink;
    private String eventDescription;
    private Boolean isApproved;
    private Boolean isStarted;
    private Boolean isCompleted;
    private Boolean isDisapproved;
    private String status;
}

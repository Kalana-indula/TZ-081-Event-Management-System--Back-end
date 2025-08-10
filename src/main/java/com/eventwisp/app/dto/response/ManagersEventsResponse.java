package com.eventwisp.app.dto.response;

import com.eventwisp.app.dto.ManagersEventDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ManagersEventsResponse {
    private String message;
    private List<ManagersEventDto> eventDetails;
}

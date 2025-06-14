package com.eventwisp.app.dto.response;

import com.eventwisp.app.entity.Event;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class FindEventByOrganizerResponse {
    private String message;
    private List<Event> eventsList= Collections.emptyList();
}

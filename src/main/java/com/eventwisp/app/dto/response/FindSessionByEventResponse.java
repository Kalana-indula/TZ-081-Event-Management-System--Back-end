package com.eventwisp.app.dto.response;

import com.eventwisp.app.entity.Session;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class FindSessionByEventResponse {
    private String message;
    private List<Session> sessionList= Collections.emptyList();
}

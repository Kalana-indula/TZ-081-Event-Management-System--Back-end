package com.eventwisp.app.dto.response.general;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class MultipleEntityResponse<T> {
    private String message;
    private List<T> entityList;
}

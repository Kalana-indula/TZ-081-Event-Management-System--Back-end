package com.eventwisp.app.dto.response;

import com.eventwisp.app.entity.Booking;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateBookingResponse {
    private String message;
    private Booking booking;
}

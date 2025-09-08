package com.eventwisp.app.dto.booking;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BookingDetailsDto {
    private String bookingId;
    private String name;
    private String email;
    private String phone;
    private String nic;

}

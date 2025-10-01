package com.eventwisp.app.dto.organizer;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class EarningDetails {

    private Long organizerId;
    private String organizerName;
    private BigDecimal totalEarnings;
    private BigDecimal totalWithdrawals;
    private BigDecimal currentBalance;
}

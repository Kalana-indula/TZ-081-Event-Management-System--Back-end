package com.eventwisp.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "organizer")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Organizer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nic")
    private String nic;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "pending_approval")
    private Boolean pendingApproval=true;

    @Column(name = "is_approved")
    private Boolean isApproved=false;

    @Column(name = "is_disapproved")
    private Boolean isDisapproved=false;

    @Column(name = "total_earnings")
    private BigDecimal totalEarnings = BigDecimal.ZERO;

    @Column(name = "total_withdrawals")
    private BigDecimal totalWithdrawals = BigDecimal.ZERO;

    @Column(name = "current_balance")
    private BigDecimal currentBalance= BigDecimal.ZERO;

    //Events of organizer
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "organizer")
    private List<Event> eventsList;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,mappedBy = "organizer")
    private List<Transaction> transactionsList;
}

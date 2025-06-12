package com.eventwisp.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ticket")
@Setter
@Getter
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "price")
    private Double price;

    @Column(name = "ticket_count")
    private Integer ticketCount;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}

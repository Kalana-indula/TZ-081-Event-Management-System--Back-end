package com.eventwisp.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "ticket")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

    //bookings
    @JsonIgnore
    @ManyToMany(mappedBy = "tickets")
    private List<Booking> bookings;
}

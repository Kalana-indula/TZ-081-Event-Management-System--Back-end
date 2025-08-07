package com.eventwisp.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "event")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "starting_date")
    private LocalDate startingDate;

    @Column(name = "date_added")
    private LocalDate dateAdded;

    @Column(name = "cover_image_link")
    private String coverImageLink;

    @Column(name = "description")
    private String description;

    @Column(name="is_approved")
    private Boolean isApproved=false;

    //Make sure, that 'isCompleted' is false by default
    @Column(name = "is_completed",nullable = false,columnDefinition = "BOOLEAN DEFAULT FALSE")
    @JsonSetter(nulls = Nulls.SKIP) //skip null values assuring isCompleted would take only 'tru' or 'false'
    private Boolean isCompleted;

    //Create a column for foriegn key
    @ManyToOne
    @JoinColumn(name = "event_category_id")
    private EventCategory eventCategory;

    //Organizer
    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    //"mappedBy" attribute is used by the inverse side of the class
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "event")
    private List<Ticket> tickets;

    //sessions
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "event")
    private List<Session> sessions;

    //bookings
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "event")
    private List<Booking> bookings;

}

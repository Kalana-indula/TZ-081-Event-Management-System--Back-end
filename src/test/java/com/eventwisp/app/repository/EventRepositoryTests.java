package com.eventwisp.app.repository;

import com.eventwisp.app.entity.Event;
import com.eventwisp.app.entity.Organizer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

//Initiate testing
@DataJpaTest

//configure in memory database
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EventRepositoryTests {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Test
    public void EventRepository_FindByOrganizer_ReturnEventNotNull(){
        //Arrange
        Organizer organizer = Organizer.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .password("passwqord123")
                .nic("1232535325V")
                .companyName("Eventify")
                .phone("0515111363").build();

        organizerRepository.save(organizer);

        Event event1=Event.builder()
                .eventName("Tech Conference")
                .description("annual tech conference")
                .startingDate(LocalDate.now())
                .coverImageLink("Imgetlink")
                .isCompleted(false)
                .organizer(organizer).build();

        eventRepository.save(event1);

        Event event2=Event.builder()
                .eventName("Tech Conference")
                .description("annual tech conference")
                .startingDate(LocalDate.now())
                .coverImageLink("Imgetlink")
                .isCompleted(false)
                .organizer(organizer).build();

        eventRepository.save(event2);

        //act
        List<Event> events=eventRepository.eventsByOrganizer(organizer.getId());

        //Assert
        Assertions.assertThat(events).hasSize(2);
        Assertions.assertThat(events).isNotEmpty();
    }
}

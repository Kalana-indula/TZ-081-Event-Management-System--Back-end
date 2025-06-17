package com.eventwisp.app.service;

import com.eventwisp.app.dto.BookingDto;
import com.eventwisp.app.dto.response.CreateBookingResponse;
import com.eventwisp.app.dto.response.FindBookingsByEventResponse;
import com.eventwisp.app.entity.Booking;
import com.eventwisp.app.entity.Session;
import com.eventwisp.app.entity.Ticket;
import com.eventwisp.app.repository.BookingRepository;
import com.eventwisp.app.repository.EventRepository;
import com.eventwisp.app.repository.SessionRepository;
import com.eventwisp.app.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{

    //Create instances of repositories
    private BookingRepository bookingRepository;
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private SessionRepository sessionRepository;

    //Inject repositories
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              EventRepository eventRepository,
                              TicketRepository ticketRepository,
                              SessionRepository sessionRepository) {
        this.bookingRepository=bookingRepository;
        this.eventRepository=eventRepository;
        this.ticketRepository=ticketRepository;
        this.sessionRepository=sessionRepository;
    }

    @Override
    @Transactional
    public CreateBookingResponse createBooking(BookingDto bookingDto) {

        CreateBookingResponse response=new CreateBookingResponse();

        //check if the session exists
        Session existingSession=sessionRepository.findById(bookingDto.getSessionId()).orElse(null);

        if(existingSession==null){
            response.setMessage("No event found for the corresponding id");
            return response;
        }


        //create new booking object
        Booking booking=new Booking();

        booking.setBookingId("BK001");
        booking.setFirstName(bookingDto.getFirstName());
        booking.setLastName(bookingDto.getLastName());
        booking.setEmail(bookingDto.getEmail());
        booking.setPhone(bookingDto.getPhone());
        booking.setIdNumber(bookingDto.getIdNumber());
        booking.setBookingDate(LocalDate.now());
        booking.setBookingTime(LocalTime.now());
        booking.setSession(existingSession);
        booking.setEvent(existingSession.getEvent());

        //Create a list to store tickets
        List<Ticket> bookedTickets=new ArrayList<>();

        //ticket price
        double totalPrice=0.0;

        //Find tickets for each id
        for(Long ticketId:bookingDto.getTicketIdList()){
            Ticket ticket=ticketRepository.findById(ticketId).orElse(null);

            //check if the ticket is null
            if(ticket!=null){
                int count=ticket.getTicketCount();

                if(count>=0){
                    bookedTickets.add(ticket);
                    ticket.setTicketCount(count-1);
                    totalPrice+=ticket.getPrice();

                    ticketRepository.save(ticket);
                }
            }
        }

        //set ticket count
        booking.setTicketCount(bookedTickets.size());

        //set the ticket list
        booking.setTickets(bookedTickets);

        //set total price
        booking.setTotalPrice(totalPrice);

        bookingRepository.save(booking);

        response.setMessage("Booking successful");
        response.setBooking(booking);

        return response;
    }

    //get all booking details
    @Override
    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public FindBookingsByEventResponse findBookingsByEvent(Long eventId) {

        FindBookingsByEventResponse response=new FindBookingsByEventResponse();

        //check if event exists
        boolean isExist=eventRepository.existsById(eventId);

        if(!isExist){
            response.setMessage("No event found for entered id");
            return response;
        }

        List<Booking> bookingList=bookingRepository.bookingsByEvent(eventId);

        //check if the list is empty
        if(bookingList.isEmpty()){
            response.setMessage("No bookings found for the event");
            return response;
        }

        response.setMessage("Bookings List");
        response.setBookings(bookingList);

        return response;
    }
}

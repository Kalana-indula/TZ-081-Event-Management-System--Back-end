package com.eventwisp.app.service.impl;

import com.eventwisp.app.dto.BookingDto;
import com.eventwisp.app.dto.booking.BookingDetailsDto;
import com.eventwisp.app.dto.response.CreateBookingResponse;
import com.eventwisp.app.dto.response.general.MultipleEntityResponse;
import com.eventwisp.app.entity.*;
import com.eventwisp.app.repository.*;
import com.eventwisp.app.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    //Create instances of repositories
    private BookingRepository bookingRepository;
    private EventRepository eventRepository;
    private TicketRepository ticketRepository;
    private SessionRepository sessionRepository;
    private BookingSequenceRepository bookingSequenceRepository;
    private OrganizerRepository organizerRepository;
    private SessionTicketRepository sessionTicketRepository;

    //Inject repositories
    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository,
                              EventRepository eventRepository,
                              TicketRepository ticketRepository,
                              SessionRepository sessionRepository,
                              BookingSequenceRepository bookingSequenceRepository,
                              OrganizerRepository organizerRepository,
                              SessionTicketRepository sessionTicketRepository) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.sessionRepository = sessionRepository;
        this.bookingSequenceRepository = bookingSequenceRepository;
        this.organizerRepository = organizerRepository;
        this.sessionTicketRepository = sessionTicketRepository;
    }

    @Override
    @Transactional
    public CreateBookingResponse createBooking(BookingDto bookingDto) {

        CreateBookingResponse response = new CreateBookingResponse();

        //check if the session exists
        Session existingSession = sessionRepository.findById(bookingDto.getSessionId()).orElse(null);

        if (existingSession == null) {
            response.setMessage("No event found for the corresponding id");
            return response;
        }

        //Get event
        Event event = existingSession.getEvent();

        //get current attendees count for the event
        int currentEventAttendeesCount = event.getTotalAttendeesCount();

        //get current total earnings
        BigDecimal currentEarnings = event.getEarningsByEvent();

        //get current profit
        BigDecimal currentProfit = event.getTotalProfit();

        //get commission for this event
        double currentCommission = event.getCommission();

        //create new booking object
        Booking booking = new Booking();

        booking.setBookingId(generateBookingId());
        booking.setFirstName(bookingDto.getFirstName());
        booking.setLastName(bookingDto.getLastName());
        booking.setEmail(bookingDto.getEmail());
        booking.setPhone(bookingDto.getPhone());
        booking.setIdNumber(bookingDto.getIdNumber());
        booking.setBookingDate(LocalDate.now());
        booking.setBookingTime(LocalTime.now());
        booking.setSession(existingSession);
        booking.setEvent(event);

        //Create a list to store tickets
        List<Ticket> bookedTickets = new ArrayList<>();

        //get ticket details for the session
        List<SessionTicket> sessionTicketDetails=sessionTicketRepository.findSessionTicketsBySessionId(bookingDto.getSessionId());

        //ticket price
        double totalPrice = 0.0;
        int attendance = 0;

        //Find tickets for each id
        for (Long ticketId : bookingDto.getTicketIdList()) {
            Ticket ticket = ticketRepository.findById(ticketId).orElse(null);

            if(ticket!=null){
                //update session ticket details
                updateSessionTicketDetails(ticketId,sessionTicketDetails);

                attendance += 1;
                bookedTickets.add(ticket);
                totalPrice += ticket.getPrice();
            }

        }

        //calculate the profit from the booking
        double profit = totalPrice - totalPrice * currentCommission;

        //set ticket count
        booking.setTicketCount(bookedTickets.size());

        //set the ticket list
        booking.setTickets(bookedTickets);

        //set total price
        booking.setTotalPrice(totalPrice);


        //update event details
        //get current event revenue,profit and attendees count
        event.setTotalAttendeesCount(currentEventAttendeesCount + attendance);
        event.setEarningsByEvent(currentEarnings.add(BigDecimal.valueOf(totalPrice)));
        event.setTotalProfit(currentProfit.add(BigDecimal.valueOf(profit)));

        //below method is not mandatory as saving handles by @Transactional (dirty checking)
        eventRepository.save(event);

        //get current session revenue,profit and attendees count
        BigDecimal currentSessionRevenue = existingSession.getRevenue();
        BigDecimal currentSessionProfit = existingSession.getProfit();
        int currentSessionAttendees = existingSession.getAttendees();

        //update session details
        existingSession.setRevenue(currentSessionRevenue.add(BigDecimal.valueOf(totalPrice)));
        existingSession.setProfit(currentSessionProfit.add(BigDecimal.valueOf(profit)));
        existingSession.setAttendees(currentSessionAttendees + attendance);

        sessionRepository.save(existingSession);

        //get organizer details
        Organizer organizer = event.getOrganizer();

        BigDecimal currentEarningsByOrganizer = organizer.getTotalEarnings();
        BigDecimal currentBalance = organizer.getCurrentBalance();

        //update organizer earnings
        organizer.setTotalEarnings(currentEarningsByOrganizer.add(BigDecimal.valueOf(profit)));
        organizer.setCurrentBalance(currentBalance.add(BigDecimal.valueOf(profit)));
        organizerRepository.save(organizer);

        Booking newBooking = bookingRepository.save(booking);

        //get new booking details
        BookingDetailsDto bookingDetails = new BookingDetailsDto();

        bookingDetails.setBookingId(newBooking.getBookingId());
        bookingDetails.setName(newBooking.getFirstName() + " " + newBooking.getLastName());
        bookingDetails.setEmail(newBooking.getEmail());
        bookingDetails.setPhone(newBooking.getPhone());
        bookingDetails.setNic(newBooking.getIdNumber());

        response.setMessage("Booking successful");
        response.setBookingDetails(bookingDetails);

        return response;
    }

    //get all booking details
    @Override
    public MultipleEntityResponse<BookingDetailsDto> findAllBookings() {

        MultipleEntityResponse<BookingDetailsDto> response = new MultipleEntityResponse<>();

        //get all bookings
        List<Booking> bookings = bookingRepository.findAll();

        if (bookings.isEmpty()) {
            response.setMessage("No bookings found");
            return response;
        }

        List<BookingDetailsDto> bookingsDetails = new ArrayList<>();

        for (Booking booking : bookings) {
            BookingDetailsDto dto = new BookingDetailsDto();

            dto.setBookingId(booking.getBookingId());
            dto.setName(booking.getFirstName() + " " + booking.getLastName());
            dto.setEmail(booking.getEmail());
            dto.setPhone(booking.getPhone());
            dto.setNic(booking.getIdNumber());

            bookingsDetails.add(dto);
        }

        response.setMessage("Booking details");
        response.setEntityList(bookingsDetails);

        return response;
    }

    @Override
    public MultipleEntityResponse<BookingDetailsDto> findBookingsByEvent(Long eventId) {

        MultipleEntityResponse<BookingDetailsDto> response = new MultipleEntityResponse<>();

        // check if event exists
        boolean isExist = eventRepository.existsById(eventId);

        if (!isExist) {
            response.setMessage("No event found for entered id");
            return response;
        }

        // fetch bookings
        List<Booking> bookingList = bookingRepository.bookingsByEvent(eventId);

        // check if the list is empty
        if (bookingList.isEmpty()) {
            response.setMessage("No bookings found for the event");
            return response;
        }

        // map bookings -> DTOs
        List<BookingDetailsDto> bookingDtos = new ArrayList<>();
        for (Booking booking : bookingList) {
            BookingDetailsDto dto = new BookingDetailsDto();
            dto.setBookingId(booking.getBookingId());
            dto.setName(booking.getFirstName() + " " + booking.getLastName());
            dto.setEmail(booking.getEmail());
            dto.setPhone(booking.getPhone());
            dto.setNic(booking.getIdNumber());

            bookingDtos.add(dto);
        }

        response.setMessage("Bookings List");
        response.setEntityList(bookingDtos);

        return response;
    }


    //generate booking id
    private String generateBookingId() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String datePrefix = "BK" + today.format(formatter);

        BookingSequenceTracker tracker = bookingSequenceRepository.findById(today.toString())
                .orElseGet(() -> {
                    BookingSequenceTracker newTracker = new BookingSequenceTracker();
                    newTracker.setDate(today.toString());
                    newTracker.setSequence(0L);
                    return bookingSequenceRepository.save(newTracker);
                });

        synchronized (this) {
            long sequence = tracker.getSequence() + 1;
            tracker.setSequence(sequence);
            bookingSequenceRepository.save(tracker);
            return datePrefix + "-" + String.format("%03d", sequence);
        }
    }

    //update session ticket details
    private void updateSessionTicketDetails(Long ticketId, List<SessionTicket> sessionTicketDetails){
        for(SessionTicket sessionTicket : sessionTicketDetails){
            if(ticketId.equals(sessionTicket.getTicketId())){
                sessionTicket.setRemainingTicketCount(sessionTicket.getRemainingTicketCount()-1);
                sessionTicket.setSoldTicketCount(sessionTicket.getSoldTicketCount()+1);
                sessionTicketRepository.save(sessionTicket);
            }
        }
    }

}
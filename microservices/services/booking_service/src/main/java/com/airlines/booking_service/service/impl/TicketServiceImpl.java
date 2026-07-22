package com.airlines.booking_service.service.impl;

import com.airline.enums.TicketStatus;
import com.airlines.booking_service.model.Booking;
import com.airlines.booking_service.model.Passenger;
import com.airlines.booking_service.model.Ticket;
import com.airlines.booking_service.repository.TicketRepository;
import com.airlines.booking_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> generateTicketsForBooking(Booking booking) {
        List<Ticket> tickets = new ArrayList<>();

        for (Passenger passenger : booking.getPassengers()) {
            String ticketNumber = generateUniqueTicketNumber();

            Ticket ticket = Ticket.builder()
                    .ticketNumber(ticketNumber)
                    .status(TicketStatus.BOOKED)
                    .issuedAt(LocalDateTime.now())
                    .booking(booking)
                    .passenger(passenger)
                    .build();

            Ticket savedTicket = ticketRepository.save(ticket);
            tickets.add(savedTicket);
        }
        return tickets;
    }

    @Override
    public Ticket getTicketByNumber(String ticketNumber) throws Exception {
        return ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new Exception(
                        "Ticket not found with number: " + ticketNumber));

    }

    @Override
    public List<Ticket> getTicketsByBooking(Long bookingId) {
        return ticketRepository.findByBookingIdWithDetails(bookingId);
    }

    @Override
    public List<Ticket> getTicketsByPassenger(Long passengerId) {
        return ticketRepository.findByPassengerId(passengerId);
    }

    @Override
    public Ticket cancelTicket(Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception(
                        "Ticket not found with id: " + ticketId));

        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalStateException("Cannot cancel a ticket that has already been used");
        }

        if (ticket.getStatus() == TicketStatus.REFUNDED) {
            throw new IllegalStateException("Cannot cancel a ticket that has already been refunded");
        }

        ticket.setStatus(TicketStatus.CANCELLED);

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket markTicketAsUsed(Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception(
                        "Ticket not found with id: " + ticketId));

        if (ticket.getStatus() == TicketStatus.CANCELLED) {
            throw new IllegalStateException("Cannot use a cancelled ticket");
        }

        if (ticket.getStatus() == TicketStatus.REFUNDED) {
            throw new IllegalStateException("Cannot use a refunded ticket");
        }

        ticket.setStatus(TicketStatus.USED);

        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket refundTicket(Long ticketId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new Exception(
                        "Ticket not found with id: " + ticketId));

        if (ticket.getStatus() == TicketStatus.USED) {
            throw new IllegalStateException("Cannot refund a ticket that has already been used");
        }

        ticket.setStatus(TicketStatus.REFUNDED);

        return ticketRepository.save(ticket);

    }

    private String generateUniqueTicketNumber() {
        String ticketNumber;
        do {
            String datePart = LocalDateTime.now().toString().substring(0, 10).replace("-", "");
            String randomPart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            ticketNumber = String.format("TKT-%s-%s", datePart, randomPart);
        } while (ticketRepository.existsByTicketNumber(ticketNumber));

        return ticketNumber;
    }

}

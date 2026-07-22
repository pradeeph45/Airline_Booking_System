package com.airlines.booking_service.service;

import com.airlines.booking_service.model.Booking;
import com.airlines.booking_service.model.Ticket;

import java.util.List;

public interface TicketService {

    List<Ticket> generateTicketsForBooking(Booking booking);

    Ticket getTicketByNumber(String ticketNumber) throws Exception;

    List<Ticket> getTicketsByBooking(Long bookingId);

    List<Ticket> getTicketsByPassenger(Long passengerId);

    Ticket cancelTicket(Long ticketId) throws Exception;

    Ticket markTicketAsUsed(Long ticketId) throws Exception;

    Ticket refundTicket(Long ticketId) throws Exception;

}

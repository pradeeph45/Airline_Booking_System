package com.airlines.booking_service.service;

import com.airline.enums.BookingStatus;
import com.airline.payload.request.BookingRequest;
import com.airline.payload.response.BookingResponse;

import java.util.List;

public interface BookingService {

    BookingResponse createBooking(BookingRequest request, Long userId)
            throws Exception;

    BookingResponse updateBooking(Long id, BookingRequest request)
            throws Exception;

    BookingResponse getBookingById(Long id) throws Exception;



    List<BookingResponse> getBookingsByAirline(
            Long userId,
            String searchQuery,
            BookingStatus status,
            Long flightInstanceId,
            String sortDirection
    );

    List<BookingResponse> getBookingsByUser(Long userId);

    BookingResponse cancelBooking(Long id) throws Exception;

    void deleteBooking(Long id) throws Exception;

    boolean existsById(Long id);

    long count();

    long countByFlightId(Long flightId);

  //  BookingStatisticsResponse getBookingStatisticsForAirline(Long airlineId);

}

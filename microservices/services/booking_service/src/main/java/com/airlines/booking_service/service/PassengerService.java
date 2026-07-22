package com.airlines.booking_service.service;

import com.airline.payload.request.PassengerRequest;
import com.airline.payload.response.PassengerResponse;
import com.airlines.booking_service.model.Passenger;

public interface PassengerService {

    PassengerResponse createPassenger(PassengerRequest request, Long userId)
            throws Exception;

    Passenger findOrCreatePassengerEntity(PassengerRequest request, Long userId);

    Passenger findExistingPassenger(PassengerRequest request);

    boolean existsById(Long id);

    long count();

}

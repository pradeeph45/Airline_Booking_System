package com.airline.flight_ops_service.service;

import com.airline.enums.FlightStatus;
import com.airline.payload.request.FlightRequest;
import com.airline.payload.response.FlightResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlightService {

    FlightResponse createFlight(Long airlineId, FlightRequest flightRequest);

    Page<FlightResponse> getFlightsByAirline(Long airlineId,
                                             Long departureAirportId,
                                             Long arrivalAirportId,
                                             Pageable pageable);


    FlightResponse getFlightById(Long id);
    FlightResponse updateFlight(Long id,FlightRequest flightRequest);
    FlightResponse changeStatus(Long id, FlightStatus status);
    void deleteFlight(Long id);
}

package com.airlines.seat_service.service;

import com.airline.enums.SeatAvailabilityStatus;
import com.airline.payload.request.SeatInstanceRequest;
import com.airline.payload.response.SeatInstanceResponse;

import java.util.List;

public interface SeatInstanceService {

    SeatInstanceResponse createSeatInstance(SeatInstanceRequest request);
    SeatInstanceResponse getSeatInstanceById(Long id);
    List<SeatInstanceResponse> getSeatInstancesByFlightId(Long flightId);
    List<SeatInstanceResponse> getAvailableSeatsByFlightId(Long flightId);
    List<SeatInstanceResponse> getAllByIds(List<Long> Ids);
    SeatInstanceResponse updateSeatInstanceStatus(Long id, SeatAvailabilityStatus status);
    Long countAvailableByFlightId(Long flightId);
    Double calculateSeatPrice(List<Long> seatInstanceId);

}

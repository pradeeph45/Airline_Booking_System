package com.airlines.seat_service.service;

import com.airline.payload.request.SeatRequest;
import com.airline.payload.response.SeatResponse;

import java.util.List;

public interface SeatService {

    void generateSeats(Long seatMapId) throws Exception;
    SeatResponse getSeatById(Long id);
    List<SeatResponse> getAll();
    SeatResponse updateSeat(Long id, SeatRequest request);
}

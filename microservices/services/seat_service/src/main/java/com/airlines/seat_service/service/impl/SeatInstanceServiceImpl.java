package com.airlines.seat_service.service.impl;

import com.airline.enums.SeatAvailabilityStatus;
import com.airline.payload.request.SeatInstanceRequest;
import com.airline.payload.response.SeatInstanceResponse;
import com.airlines.seat_service.model.SeatInstance;
import com.airlines.seat_service.repository.FlightInstanceCabinRepository;
import com.airlines.seat_service.repository.SeatInstanceRepository;
import com.airlines.seat_service.repository.SeatRepository;
import com.airlines.seat_service.service.SeatInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatInstanceServiceImpl implements SeatInstanceService {

    private final SeatInstanceRepository seatInstanceRepository;
    private final SeatRepository seatRepository;
    private final FlightInstanceCabinRepository flightInstanceCabinRepository;

    @Override
    public SeatInstanceResponse createSeatInstance(SeatInstanceRequest request) {
        return null;
    }

    @Override
    public SeatInstanceResponse getSeatInstanceById(Long id) {
        return null;
    }

    @Override
    public List<SeatInstanceResponse> getSeatInstancesByFlightId(Long flightId) {
        return List.of();
    }

    @Override
    public List<SeatInstanceResponse> getAvailableSeatsByFlightId(Long flightId) {
        return List.of();
    }

    @Override
    public List<SeatInstanceResponse> getAllByIds(List<Long> Ids) {
        return List.of();
    }

    @Override
    public SeatInstanceResponse updateSeatInstanceStatus(Long id, SeatAvailabilityStatus status) {
        return null;
    }

    @Override
    public Long countAvailableByFlightId(Long flightId) {
        return 0L;
    }

    @Override
    public Double calculateSeatPrice(List<Long> seatInstanceId) {

        List<SeatInstance> seatInstances = seatInstanceRepository.findAllById(seatInstanceId);
        double total=0.0;
        for (SeatInstance si : seatInstances) {

            double seatPremium = si.getPremiumSurcharge() != null
                    ? si.getPremiumSurcharge()
                    : 0.0;

            total+=seatPremium;

        }
        return total;

    }
}

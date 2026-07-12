package com.airline.airline_core_service.service.impl;

import com.airline.airline_core_service.mapper.AircraftMapper;
import com.airline.airline_core_service.model.Aircraft;
import com.airline.airline_core_service.model.Airline;
import com.airline.airline_core_service.repository.AircraftRepository;
import com.airline.airline_core_service.repository.AirlineRepository;
import com.airline.airline_core_service.service.AircraftService;
import com.airline.payload.request.AircraftRequest;
import com.airline.payload.response.AircraftResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AircraftServiceImpl implements AircraftService {
    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    @Override
    public AircraftResponse getAircraftById(Long id) throws Exception {
        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new Exception("Aircraft not found with id: " + id));
        return AircraftMapper.toResponse(aircraft);

    }

    @Override
    public List<AircraftResponse> listAllAircraftsByOwner(Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));
        return aircraftRepository.findByAirline(airline)
                .stream()
                .map(AircraftMapper::toResponse)
                .toList();

    }

    @Override
    public AircraftResponse createAircraft(AircraftRequest request, Long ownerId) {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));

        Aircraft aircraft = AircraftMapper.toEntity(request, airline);
        if (aircraftRepository.existsByCode(aircraft.getCode())) {
            throw new IllegalArgumentException("Aircraft with code " + aircraft.getCode() + " already exists");
        }
        validateAircraftData(aircraft);
        return AircraftMapper.toResponse(aircraftRepository.save(aircraft));
    }

    @Override
    public AircraftResponse updateAircraft(Long id, AircraftRequest request, Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new Exception("Aircraft not found with id: " + id));

        String oldCode = aircraft.getCode();
        AircraftMapper.updateEntity(aircraft, request);

        if (!oldCode.equals(request.getCode()) && aircraftRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException("Aircraft with code " + request.getCode() + " already exists");
        }

        validateAircraftData(aircraft);
        return AircraftMapper.toResponse(aircraftRepository.save(aircraft));

    }

    @Override
    public void deleteAircraft(Long id,Long ownerId) throws Exception {
        Airline airline = airlineRepository.findByOwnerId(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Airline not found for owner: " + ownerId));

        Aircraft aircraft = aircraftRepository.findById(id)
                .orElseThrow(() -> new Exception("Aircraft not found with id: " + id));
        aircraftRepository.delete(aircraft);

    }

    private void validateAircraftData(Aircraft aircraft) {
        if (aircraft.getSeatingCapacity() != null && aircraft.getSeatingCapacity() <= 0) {
            throw new IllegalArgumentException("Seating capacity must be positive");
        }

        int totalSpecifiedSeats = (aircraft.getEconomySeats() != null ? aircraft.getEconomySeats() : 0) +
                (aircraft.getPremiumEconomySeats() != null ? aircraft.getPremiumEconomySeats() : 0) +
                (aircraft.getBusinessSeats() != null ? aircraft.getBusinessSeats() : 0) +
                (aircraft.getFirstClassSeats() != null ? aircraft.getFirstClassSeats() : 0);

        if (totalSpecifiedSeats > aircraft.getSeatingCapacity()) {
            throw new IllegalArgumentException("Total specified seats exceed aircraft seating capacity");
        }

        if (aircraft.getYearOfManufacture() != null &&
                (aircraft.getYearOfManufacture() < 1900
                        || aircraft.getYearOfManufacture() > LocalDate.now().getYear())) {
            throw new IllegalArgumentException("Invalid year of manufacture");
        }
    }

}

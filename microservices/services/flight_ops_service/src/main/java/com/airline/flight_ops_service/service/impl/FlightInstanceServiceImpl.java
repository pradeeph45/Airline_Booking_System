package com.airline.flight_ops_service.service.impl;

import com.airline.flight_ops_service.mapper.FlightInstanceMapper;
import com.airline.flight_ops_service.model.Flight;
import com.airline.flight_ops_service.model.FlightInstance;
import com.airline.flight_ops_service.repository.FlightInstanceRepository;
import com.airline.flight_ops_service.repository.FlightRepository;
import com.airline.flight_ops_service.service.FlightInstanceService;
import com.airline.payload.request.FlightInstanceRequest;
import com.airline.payload.response.AircraftResponse;
import com.airline.payload.response.AirlineResponse;
import com.airline.payload.response.AirportResponse;
import com.airline.payload.response.FlightInstanceResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;


    @Override
    public FlightInstanceResponse createFlightInstanceWithCabins(
        Long airlineId, FlightInstanceRequest request) throws Exception{

            Flight flight = flightRepository.findById(request.getFlightId())
                    .orElseThrow(() -> new Exception("Flight not found"));

            AircraftResponse aircraft = AircraftResponse.builder()
                    .id(1L)
                    .totalSeats(90)
                    .build();

            FlightInstance instance = FlightInstanceMapper.toEntity(request, flight);
            instance.setAirlineId(airlineId);
            instance.setFlight(flight);
            instance.setDepartureAirportId(request.getDepartureAirportId());
            instance.setArrivalAirportId(request.getArrivalAirportId());
            instance.setTotalSeats(aircraft.getTotalSeats());
            instance.setAvailableSeats(aircraft.getTotalSeats());

            FlightInstance flightInstance = flightInstanceRepository.save(instance);

            return getFlightInstance(flightInstance);
    }

    @Override
    public List<FlightInstanceResponse> getFlightInstances() {
        return flightInstanceRepository.findAll().stream()
                .map(fi-> {
                    try {
                        return getFlightInstance(fi);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).toList();

    }

    @Override
    public FlightInstanceResponse getFlightInstanceById(Long id) throws Exception {
        FlightInstance fi = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "Flight instance not found with id: " + id));


        return getFlightInstance(fi);

    }

    @Override
    public Page<FlightInstanceResponse> getByAirlineId(Long airlineId, Long departureAirportId, Long arrivalAirportId, Long flightId, LocalDate onDate, Pageable pageable) {
        LocalDateTime start = onDate != null ? onDate.atStartOfDay() : null;
        LocalDateTime end   = onDate != null ? onDate.plusDays(1).atStartOfDay() : null;

        return flightInstanceRepository.findByAirlineIdWithFilters(
                airlineId, departureAirportId, arrivalAirportId, flightId, start, end, pageable
        ).map(fi -> {
            try {
                return getFlightInstance(fi);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public FlightInstanceResponse updateFlightInstance(Long id, FlightInstanceRequest request) throws Exception {
        FlightInstance existing = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight instance not found with id: " + id));
        FlightInstanceMapper.updateEntity(request, existing);
        return getFlightInstance(flightInstanceRepository.save(existing));

    }

    @Override
    public void deleteFlightInstance(Long id) {
        FlightInstance fi = flightInstanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight instance not found with id: " + id));
        flightInstanceRepository.delete(fi);

    }

    private FlightInstanceResponse getFlightInstance(FlightInstance flightInstance) throws Exception {

    AirlineResponse airlineResponse = AirlineResponse.builder()
            .id(flightInstance.getAirlineId())
            .build();
    AirportResponse departureAirport = AirportResponse.builder()
            .id(flightInstance.getDepartureAirportId())
            .build();
        AirportResponse arrivalAirport = AirportResponse.builder()
                .id(flightInstance.getArrivalAirportId())
                .build();
         AircraftResponse aircraftResponse = AircraftResponse. builder()
                 .id(flightInstance.getFlight().getAircraftId())
                 .build();

         return FlightInstanceMapper.toResponse(
                 flightInstance,
                 aircraftResponse,
                 airlineResponse,
                 departureAirport,
                 arrivalAirport
         );
    }

}

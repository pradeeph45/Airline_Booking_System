package com.airline.flight_ops_service.service.impl;

import com.airline.enums.FlightStatus;
import com.airline.flight_ops_service.model.Flight;
import com.airline.flight_ops_service.repository.FlightRepository;
import com.airline.flight_ops_service.mapper.FlightMapper;
import com.airline.flight_ops_service.service.FlightService;
import com.airline.payload.request.FlightRequest;
import com.airline.payload.response.AircraftResponse;
import com.airline.payload.response.AirlineResponse;
import com.airline.payload.response.AirportResponse;
import com.airline.payload.response.FlightResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public FlightResponse createFlight(Long airlineId, FlightRequest flightRequest) {
        if (flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new IllegalArgumentException(
                    "Flight with number '" + flightRequest.getFlightNumber() + "' already exists");
        }

        //Long airlineId = getAirlineForUser(airlineId);
        //validateAircraftExists(flightRequest.getAircraftId());

        Flight flight = FlightMapper.toEntity(flightRequest);
        flight.setAirlineId(airlineId);
        Flight saved = flightRepository.save(flight);
        return convertToFlightResponse(saved);

    }

    @Override
    public Page<FlightResponse> getFlightsByAirline(Long airlineId, Long departureAirportId, Long arrivalAirportId, Pageable pageable) {
        return flightRepository.findByAirlineId(airlineId,
                departureAirportId,
                arrivalAirportId,
                pageable).map(this::convertToFlightResponse);
    }

    @Override
    public FlightResponse getFlightById(Long id) throws Exception {
        Flight flight = flightRepository.findById(id).orElseThrow(
                () -> new Exception("flight not fpund with the id " + id)
        );
        return convertToFlightResponse(flight);
    }

    @Override
    public FlightResponse updateFlight(Long id, FlightRequest flightRequest) throws Exception {
        Flight existing = flightRepository.findById(id).orElseThrow(
                () -> new Exception("Flight not found with id " + id)
        );
        if (flightRequest.getFlightNumber() != null &&
                flightRepository.existsByFlightNumberAndIdNot(flightRequest.getFlightNumber(), id)) {
            throw new IllegalArgumentException(
                    "Flight with number '" + flightRequest.getFlightNumber() + "' already exists");
        }

        FlightMapper.updateEntity(flightRequest, existing);
        Flight saved = flightRepository.save(existing);
        return convertToFlightResponse(saved);

    }

    @Override
    public FlightResponse changeStatus(Long id, FlightStatus status) throws Exception {
        Flight existing = flightRepository.findById(id).orElseThrow(
                () -> new Exception("Flight not found with id " + id)
        );
        existing.setStatus(status);
        Flight saved = flightRepository.save(existing);
        return convertToFlightResponse(saved);
    }

    @Override
    public void deleteFlight(Long id, Long airlineId) throws Exception {
        Flight existing = flightRepository.findByAirlineIdAndId(id, airlineId).orElseThrow(
                () -> new Exception("Flight not found with id " + id)
        );
        flightRepository.delete(existing);
    }

    private FlightResponse convertToFlightResponse(Flight flight) {
        AircraftResponse aircraft = AircraftResponse.builder()
                .id(flight.getAircraftId())
                .build();
        AirlineResponse airline = AirlineResponse.builder()
                .id(flight.getAirlineId())
                .build();
        AirportResponse departureAirport = AirportResponse.builder()
                .id(flight.getDepartureAirportId())
                .build();
        AirportResponse arrivalAirport = AirportResponse.builder()
                .id(flight.getAirlineId())
                .build();
        return FlightMapper.toResponse(
                flight,
                aircraft,
                airline,
                departureAirport,
                arrivalAirport
        );

    }

//    private FlightResponse getFlightResponse(Flight flight) {
//        AircraftResponse aircraft=airlineClient.getAircraftById(flight.getAircraftId());
//        AirlineResponse airline=airlineClient.getAirlineById(flight.getAirlineId());
//        AirportResponse departureAirport=locationClient.getAirportById(flight.getDepartureAirportId());
//        AirportResponse arrivalAirport=locationClient.getAirportById(flight.getArrivalAirportId());
//        return FlightMapper.toResponse(flight,aircraft,airline,departureAirport,arrivalAirport);
//    }

//    private void validateAircraftExists(Long aircraftId) {
//        try {
//            airlineClient.getAircraftById(aircraftId);
//        } catch (Exception e) {
//            throw new EntityNotFoundException("Aircraft not found with id: " + aircraftId);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to validate aircraft from airline-core-service: " + e.getMessage(), e);
//        }
//    }


}

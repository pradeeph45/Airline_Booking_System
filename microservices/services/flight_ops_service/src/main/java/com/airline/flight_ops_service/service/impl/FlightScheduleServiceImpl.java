package com.airline.flight_ops_service.service.impl;

import com.airline.enums.FlightStatus;
import com.airline.flight_ops_service.mapper.FlightScheduleMapper;
import com.airline.flight_ops_service.model.Flight;
import com.airline.flight_ops_service.model.FlightSchedule;
import com.airline.flight_ops_service.repository.FlightRepository;
import com.airline.flight_ops_service.repository.FlightScheduleRepository;
import com.airline.flight_ops_service.service.FlightInstanceService;
import com.airline.flight_ops_service.service.FlightScheduleService;
import com.airline.payload.request.FlightInstanceRequest;
import com.airline.payload.request.FlightScheduleRequest;
import com.airline.payload.response.AircraftResponse;
import com.airline.payload.response.AirportResponse;
import com.airline.payload.response.FlightScheduleResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightScheduleServiceImpl implements FlightScheduleService {

    private final FlightScheduleRepository flightScheduleRepository;
    private final FlightRepository flightRepository;
    private final FlightInstanceService flightInstanceService;

    @Override
    public FlightScheduleResponse createFlightSchedule(Long userId, FlightScheduleRequest request) throws Exception {
        Flight flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new Exception(
                        "Flight not found with id: " + request.getFlightId()));

        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date");
        }

        FlightSchedule schedule = FlightScheduleMapper.toEntity(
                request, flight);
        FlightSchedule savedSchedule = flightScheduleRepository.save(schedule);

//      Generate FlightInstances For schedule

        System.out.println("saved schedule: " + savedSchedule.getId());

       // AircraftResponse aircraft=airlineIntegrationService.getAircraftById(flight.getAircraftId());

        List<DayOfWeek> operatingDays = schedule.getOperatingDays(); // e.g., MON, WED, FRI
        LocalDate startDate = schedule.getStartDate();
        LocalDate endDate = schedule.getEndDate();

        FlightInstanceRequest flightInstanceRequest= FlightInstanceRequest.builder()
                .scheduleId(savedSchedule.getId())
                .flightId(flight.getId())
                .arrivalAirportId(flight.getArrivalAirportId())
                .departureAirportId(flight.getDepartureAirportId())
               // .totalSeats(aircraft.getTotalSeats())
                .status(FlightStatus.SCHEDULED)
                .build();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (operatingDays.contains(date.getDayOfWeek())) {
                flightInstanceRequest.setDepartureDateTime(
                        LocalDateTime.of(date, schedule.getDepartureTime()));
                flightInstanceRequest.setArrivalDateTime(
                        LocalDateTime.of(date, schedule.getArrivalTime()));
                System.out.println("flightInstanceRequest: " + flightInstanceRequest.getScheduleId());
                flightInstanceService.createFlightInstanceWithCabins(
                        userId,flightInstanceRequest);

            }
        }
        return getFlightScheduleResponse(savedSchedule);
    }

    @Override
    public FlightScheduleResponse getFlightScheduleById(Long id) throws Exception {
        FlightSchedule schedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight schedule not found with id: " + id));
        return getFlightScheduleResponse(schedule);

    }

    @Override
    public List<FlightScheduleResponse> getFlightScheduleByAirline(Long userId) {
        List<FlightSchedule> schedules = flightScheduleRepository.findByFlightAirlineId(userId);

        return schedules.stream().map(
                this::getFlightScheduleResponse
        ).toList();
    }

    @Override
    public FlightScheduleResponse updateFlightSchedule(Long id, FlightScheduleRequest request) throws Exception {
        FlightSchedule existing = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight schedule not found with id: " + id));

        FlightScheduleMapper.updateEntity(request, existing);
        FlightSchedule saved = flightScheduleRepository.save(existing);
        return getFlightScheduleResponse(saved);

    }

    @Override
    public void deleteFlightSchedule(Long id) {
        FlightSchedule schedule = flightScheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Flight schedule not found with id: " + id));
        flightScheduleRepository.delete(schedule);

    }

    private FlightScheduleResponse getFlightScheduleResponse(FlightSchedule flightSchedule){
        AirportResponse departureAirport = AirportResponse.builder()
                .id(flightSchedule.getDepartureAirportId())
                .build();
        AirportResponse arrivalAirport = AirportResponse.builder()
                .id(flightSchedule.getArrivalAirportId())
                .build();
        return FlightScheduleMapper.toResponse(
                flightSchedule,arrivalAirport,departureAirport
        );
    }
}

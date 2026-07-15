package com.airline.payload.response;

import com.airline.enums.RecurrenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightScheduleResponse {

    private Long id;

    private Long flightId;
    private String flightNumber;

    private AirportResponse departureAirport;
    private AirportResponse arrivalAirport;

    private LocalTime departureTime;
    private LocalTime arrivalTime;

    private LocalDate startDate;
    private LocalDate endDate;

    private RecurrenceType recurrenceType;
    private List<DayOfWeek> operatingDays;

    private Boolean isActive;
    private Long version;

}

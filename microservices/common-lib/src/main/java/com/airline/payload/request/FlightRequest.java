package com.airline.payload.request;

import com.airline.enums.FlightStatus;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class FlightRequest {

    @NotBlank(message = "Flight number is required")
    @Size(max = 10)
    private String flightNumber;

    private Long airlineId;

    @NotNull(message = "Aircraft ID is required")
    private Long aircraftId;

    @NotNull(message = "Departure Airport ID is required")
    private Long departureAirportId;

    @NotNull(message = "Arrival Airport ID is required")
    private Long arrivalAirportId;

    private FlightStatus status;

}

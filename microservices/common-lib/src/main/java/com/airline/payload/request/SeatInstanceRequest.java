package com.airline.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatInstanceRequest {

    @NotNull(message = "Flight ID is required")
    private Long flightId;

    @NotNull(message = "Flight instance ID is required")
    private Long flightInstanceId;

    @NotNull
    private Long flightInstanceCabinId;

    @NotNull
    private Long seatId;

    private String status;
    private String mealPreference;
    private Double fare;
    private Long flightScheduleId;

}

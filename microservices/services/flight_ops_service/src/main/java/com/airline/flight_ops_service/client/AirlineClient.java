package com.airline.flight_ops_service.client;

import com.airline.payload.response.AircraftResponse;
import com.airline.payload.response.AirlineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "airline-core-service")
public interface AirlineClient {

    @GetMapping("/api/airlines/{id}")
    AirlineResponse getAirlineById(
            @PathVariable Long id);

    @GetMapping("/api/aircrafts/{id}")
    AircraftResponse getAircraftById(@PathVariable("id") Long id);
}

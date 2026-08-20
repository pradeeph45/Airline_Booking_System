package com.airline.flight_ops_service.client;

import com.airline.payload.response.AircraftResponse;
import com.airline.payload.response.AirlineResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "airline-core-service")
public interface AirlineClient {

    @GetMapping("/api/airlines/admin")
    AirlineResponse getAirlineByOwner(@RequestHeader("X-User-Id") Long userId);

    @GetMapping("/api/airlines/{id}")
    AirlineResponse getAirlineById(
            @PathVariable Long id);

    @GetMapping("/api/aircrafts/{id}")
    AircraftResponse getAircraftById(@PathVariable("id") Long id);

    @GetMapping("/api/airlines/by-iata")
    List<AirlineResponse> getAirlinesByIataCodes(@RequestParam("codes") List<String> codes);

    /**
     * Returns all airlines belonging to the given alliance name.
     * Used during flight search to apply the alliance filter.
     */
    @GetMapping("/api/airlines/by-alliance")
    List<AirlineResponse> getAirlinesByAlliance(@RequestParam("alliance") String alliance);

}

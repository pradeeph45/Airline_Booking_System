package com.airline.flight_ops_service.client;

import com.airline.payload.response.AirportResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "location-service")
public interface LocationClient {

    @GetMapping("/api/airport/{id}")
    AirportResponse getAirportById(@PathVariable Long id);
}

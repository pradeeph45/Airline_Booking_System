package com.airline.flight_ops_service.service.interservice;

import com.airline.payload.response.AircraftResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AircraftIntegrationService {

    private final RestTemplate restTemplate;

    public AircraftResponse getAircraftById(Long id){
        String url = "http://localhost:5002/api/aircrafts/"+id;
        return restTemplate.getForObject(url, AircraftResponse.class);
    }
}

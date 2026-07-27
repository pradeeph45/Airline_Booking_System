package com.airline.flight_ops_service.service.interservice;

import com.airline.payload.response.AirlineResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AirlineIntegrationService {

    private final RestTemplate restTemplate;

    public AirlineResponse getAirlineById(Long id){
         String url = "http://localhost:5002/api/airlines/" + id;
         return restTemplate.getForObject(url, AirlineResponse.class);
    }
}

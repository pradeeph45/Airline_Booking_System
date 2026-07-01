package com.airline.location_service.service;

import com.airline.payload.request.AirportRequest;
import com.airline.payload.response.AirportResponse;

import java.util.List;

public interface AirportService {

    AirportResponse createAirport(AirportRequest request) throws Exception;

    AirportResponse getAirportById(Long id) throws Exception;

    List<AirportResponse> getAllAirports();

    AirportResponse updateAirport(Long id, AirportRequest airportRequest) throws Exception;

    void deleteAirport(Long id) throws Exception;

    List<AirportResponse> getAirportByCityId(Long cityId);
}

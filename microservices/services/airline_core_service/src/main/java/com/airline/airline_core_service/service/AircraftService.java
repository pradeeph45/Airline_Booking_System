package com.airline.airline_core_service.service;

import com.airline.payload.request.AircraftRequest;
import com.airline.payload.response.AircraftResponse;

import java.util.List;

public interface AircraftService {

    AircraftResponse getAircraftById(Long id) throws Exception;

    List<AircraftResponse> listAllAircraftsByOwner(Long ownerId);

    AircraftResponse createAircraft(AircraftRequest request, Long ownerId);

    AircraftResponse updateAircraft(Long id, AircraftRequest request, Long ownerId) throws Exception;

    void deleteAircraft(Long id,Long ownerId) throws Exception;

}

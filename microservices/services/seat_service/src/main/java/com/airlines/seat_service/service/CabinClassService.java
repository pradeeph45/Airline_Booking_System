package com.airlines.seat_service.service;

import com.airline.enums.CabinClassType;
import com.airline.payload.request.CabinClassRequest;
import com.airline.payload.response.CabinClassResponse;

import java.util.List;

public interface CabinClassService {
    CabinClassResponse createCabinClass(CabinClassRequest request);
    List<CabinClassResponse> createCabinClasses(List<CabinClassRequest> requests);
    CabinClassResponse getCabinClassById(Long id);
    List<CabinClassResponse> getCabinClassesByAircraftId(
            Long aircraftId);
    CabinClassResponse getByAircraftIdAndName(Long aircraftId,
                                              CabinClassType name);
    CabinClassResponse updateCabinClass(Long id, CabinClassRequest request);
    void deleteCabinClass(Long id);
}

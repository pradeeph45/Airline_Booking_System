package com.airlines.anciallary_service.service;

import com.airline.payload.request.AncillaryRequest;
import com.airline.payload.response.AncillaryResponse;

import java.util.List;

public interface AncillaryService {

    AncillaryResponse create(Long userId, AncillaryRequest request) throws Exception;

    AncillaryResponse getById(Long id) throws Exception;

    List<AncillaryResponse> getAllByAirlineId(Long userId);

    AncillaryResponse update(Long id, AncillaryRequest request) throws Exception;

    void delete(Long id);

}

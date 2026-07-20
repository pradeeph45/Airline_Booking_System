package com.airlines.anciallary_service.service;

import com.airline.payload.request.MealRequest;
import com.airline.payload.response.MealResponse;

import java.util.List;

public interface MealService {

    MealResponse create(Long userId, MealRequest request) throws Exception;

    List<MealResponse> bulkCreate(Long userId, List<MealRequest> requests) throws Exception;

    MealResponse getById(Long id) throws Exception;

    List<MealResponse> getByAirlineId(Long userId);

    MealResponse update(Long userId, Long id, MealRequest request) throws Exception;

    void delete(Long id) throws Exception;

    MealResponse updateAvailability(Long id, Boolean available) throws Exception;

}

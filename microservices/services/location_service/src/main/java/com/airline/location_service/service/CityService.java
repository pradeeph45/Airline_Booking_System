package com.airline.location_service.service;

import com.airline.payload.request.CityRequest;
import com.airline.payload.response.CityResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CityService {

    CityResponse createCity(CityRequest cityRequest) throws Exception;

    CityResponse getCityById(Long id) throws Exception;

    CityResponse updateCity(Long id, CityRequest cityRequest) throws Exception;

    void deleteCity(Long id) throws Exception;

    Page<CityResponse> getAllCities(Pageable pageable);

    Page<CityResponse> searchCities(String keyword,Pageable pageable);

    Page<CityResponse> getCitiesByCountryCode(String cityCode,Pageable pageable);

    boolean cityExists(String cityCode);

}

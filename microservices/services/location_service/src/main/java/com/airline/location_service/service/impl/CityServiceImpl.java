package com.airline.location_service.service.impl;

import com.airline.location_service.mapper.CityMapper;
import com.airline.location_service.model.City;
import com.airline.location_service.repository.CityRepository;
import com.airline.location_service.service.CityService;
import com.airline.payload.request.CityRequest;
import com.airline.payload.response.CityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {

    private final CityRepository cityRepository;

    @Override
    public CityResponse createCity(CityRequest cityRequest) throws Exception {
        if(cityRepository.existsByCityCode(cityRequest.getCityCode())){
            throw new Exception("City with given city code already exists");
        }

        City city = CityMapper.toEntity(cityRequest);
        cityRepository.save(city);
        return CityMapper.toResponse(city);
    }

    @Override
    public CityResponse getCityById(Long id) throws Exception {
        City city = cityRepository.findById(id).orElseThrow(
                () -> new Exception("City with the given id does not exist")
        );
        return CityMapper.toResponse(city);
    }

    @Override
    public CityResponse updateCity(Long id, CityRequest cityRequest) throws Exception {
        City city = cityRepository.findById(id).orElseThrow(
                () -> new Exception("City with the given id does not exist")
        );

        if(cityRepository.existsByCityCode(cityRequest.getCityCode())){
            throw new Exception("City with given code already exists");
        }

        City updatedCity = CityMapper.updateCity(city,cityRequest);
        cityRepository.save(updatedCity);
        return CityMapper.toResponse(updatedCity);
    }

    @Override
    public void deleteCity(Long id) throws Exception {
        City city = cityRepository.findById(id).orElseThrow(
            () -> new Exception("City with the given id does not exist")
    );
        cityRepository.delete(city);
    }

    @Override
    public Page<CityResponse> getAllCities(Pageable pageable) {
        return cityRepository.findAll(pageable).map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> searchCities(String keyword, Pageable pageable) {
        return cityRepository.searchByKeyword(keyword, pageable).map(CityMapper::toResponse);
    }

    @Override
    public Page<CityResponse> getCitiesByCountryCode(String countryCode, Pageable pageable) {
        return cityRepository.findByCountryCodeIgnoreCase(countryCode,pageable).map(CityMapper::toResponse);
    }

    @Override
    public boolean cityExists(String cityCode) {
        return cityRepository.existsByCityCode(cityCode);
    }
}

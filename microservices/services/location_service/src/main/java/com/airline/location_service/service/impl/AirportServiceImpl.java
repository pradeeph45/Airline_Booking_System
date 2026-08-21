package com.airline.location_service.service.impl;

import com.airline.location_service.mapper.AirportMapper;
import com.airline.location_service.model.Airport;
import com.airline.location_service.model.City;
import com.airline.location_service.repository.AirportRepository;
import com.airline.location_service.repository.CityRepository;
import com.airline.location_service.service.AirportService;
import com.airline.payload.request.AirportRequest;
import com.airline.payload.response.AirportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    private final CityRepository cityRepository;

    @Override
    public AirportResponse createAirport(AirportRequest request) throws Exception {
        if(airportRepository.findByIataCode(request.getIataCode()).isPresent()){
            throw new Exception("Airport with IATA code already exists");
        }

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new Exception("City not found"));

        Airport airport = AirportMapper.toEntity(request);
        airport.setCity(city);
        Airport savedAirport = airportRepository.save(airport);

        return AirportMapper.toResponse(savedAirport);
    }

    @Override
    @Cacheable(cacheNames = "airports",key = "#id")
    public AirportResponse getAirportById(Long id) throws Exception {
        Airport airport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport with given id does not exist")
        );
        return AirportMapper.toResponse(airport);
    }

    @Override
    @Cacheable(cacheNames = "allAirports")
    public List<AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream()
                .map(AirportMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "airports", key = "#id"),
            @CacheEvict(cacheNames = "allAirports", allEntries = true),
            @CacheEvict(cacheNames = "airportsByIata", allEntries = true),
            @CacheEvict(cacheNames = "airportsByCity", allEntries = true)
    })
    public AirportResponse updateAirport(Long id, AirportRequest airportRequest) throws Exception {
        Airport existingAirport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport with given id does not exists")
        );
        if(airportRequest.getIataCode() != null
            && !existingAirport.getIataCode().equals(airportRequest.getIataCode())
            && airportRepository.findByIataCode(airportRequest.getIataCode()).isPresent()){
            throw new Exception("Airport with Iata code already exists");
        }
        AirportMapper.updateEntity(airportRequest,existingAirport);
        Airport updatedAirport = airportRepository.save(existingAirport);
        return AirportMapper.toResponse(updatedAirport);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "airports", key = "#id"),
            @CacheEvict(cacheNames = "allAirports", allEntries = true),
            @CacheEvict(cacheNames = "airportsByIata", allEntries = true),
            @CacheEvict(cacheNames = "airportsByCity", allEntries = true)
    })
    public void deleteAirport(Long id) throws Exception {
        Airport airport = airportRepository.findById(id).orElseThrow(
                () -> new Exception("Airport with given id does not exist")
        );
        airportRepository.delete(airport);
    }

    @Override
    @Cacheable(cacheNames = "airportsByCity", key = "#cityId")
    public List<AirportResponse> getAirportByCityId(Long cityId) {
        return airportRepository.findByCityId(cityId).stream()
                .map(AirportMapper::toResponse)
                .collect(Collectors.toList());
    }
}

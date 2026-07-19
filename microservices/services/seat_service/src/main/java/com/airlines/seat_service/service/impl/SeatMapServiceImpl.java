package com.airlines.seat_service.service.impl;

import com.airline.payload.request.SeatMapRequest;
import com.airline.payload.response.SeatMapResponse;
import com.airlines.seat_service.mapper.SeatMapMapper;
import com.airlines.seat_service.model.CabinClass;
import com.airlines.seat_service.model.SeatMap;
import com.airlines.seat_service.repository.CabinClassRepository;
import com.airlines.seat_service.repository.SeatMapRepository;
import com.airlines.seat_service.service.SeatMapService;
import com.airlines.seat_service.service.SeatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatMapServiceImpl implements SeatMapService {

    private final SeatMapRepository seatMapRepository;
    private final CabinClassRepository cabinClassRepository;
//    private final AirlineClient airlineClient;
    private final SeatService seatService;

    @Override
    public SeatMapResponse createSeatMap(Long airlineId, SeatMapRequest request) throws Exception {
       // Long airlineId= getAirlineForUser(userId);

        CabinClass cabinClass = cabinClassRepository.findById(request.getCabinClassId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cabin class not found with id: " + request.getCabinClassId()));


        if (seatMapRepository.existsByAirlineIdAndCabinClassIdAndName(
                airlineId, request.getCabinClassId(), request.getName())) {
            throw new IllegalArgumentException(
                    "Seat map with name '" + request.getName() + "' already exists for this airline and cabin class");
        }

        SeatMap seatMap = SeatMapMapper.toEntity(request, cabinClass);
        seatMap.setAirlineId(airlineId);
        SeatMap savedSeatMap = seatMapRepository.save(seatMap);

//      generate seats for seatMap
        seatService.generateSeats(savedSeatMap.getId());
        return SeatMapMapper.toResponse(savedSeatMap);

    }

    @Override
    public List<SeatMapResponse> createSeatMaps(Long airlineId, List<SeatMapRequest> requests) throws Exception {
       // Long airlineId = getAirlineForUser(userId);

        List<SeatMap> toSave = requests.stream()
                .filter(req -> !seatMapRepository.existsByAirlineIdAndCabinClassIdAndName(
                        airlineId, req.getCabinClassId(), req.getName()))
                .map(req -> {
                    CabinClass cabinClass = cabinClassRepository.findById(req.getCabinClassId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Cabin class not found with id: " + req.getCabinClassId()));
                    SeatMap seatMap = SeatMapMapper.toEntity(req, cabinClass);
                    seatMap.setAirlineId(airlineId);
                    return seatMap;
                })
                .collect(Collectors.toList());

        List<SeatMap> saved = seatMapRepository.saveAll(toSave);

        for (SeatMap seatMap : saved) {
            seatService.generateSeats(seatMap.getId());
        }

        return saved.stream()
                .map(SeatMapMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public SeatMapResponse getSeatMapById(Long id) {
        SeatMap seatMap = seatMapRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat map not found with id: " + id));
        return SeatMapMapper.toResponse(seatMap);

    }

    @Override
    public SeatMapResponse getSeatMapsByCabinClass(Long cabinClassId) {
        cabinClassRepository.findById(cabinClassId).orElseThrow(
                ()-> new EntityNotFoundException(
                        "CabinClass not found with id: " + cabinClassId)
        );

        SeatMap seatMap = seatMapRepository.findByCabinClassId(cabinClassId);

        if(seatMap==null){
            throw new EntityNotFoundException("seat map not found with id cabin class id: " + cabinClassId);
        }

        return SeatMapMapper.toResponse(seatMap);

    }

    @Override
    public SeatMapResponse updateSeatMap(Long airlineId, Long id, SeatMapRequest request) {
       // Long airlineId= getAirlineForUser(userId);
        SeatMap existing = seatMapRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Seat map not found with id: " + id));

        if (seatMapRepository.existsByAirlineIdAndNameAndIdNot(airlineId, request.getName(), id)) {
            throw new IllegalArgumentException(
                    "Seat map with name '" + request.getName() + "' already exists for this airline");
        }

        SeatMapMapper.updateEntity(request, existing);
        SeatMap saved = seatMapRepository.save(existing);
        return SeatMapMapper.toResponse(saved);

    }

    @Override
    public void deleteSeatMap(Long id) throws Exception {
        if (!seatMapRepository.existsById(id)) {
            throw new Exception("Seat map not found with id: " + id);
        }
        seatMapRepository.deleteById(id);

    }

//    private Long getAirlineForUser(Long userId) {
//        try {
//            AirlineResponse airline = airlineClient.getAirlineByOwner(userId);
//            return airline.getId();
//        } catch (FeignException.NotFound e) {
//            throw new EntityNotFoundException("No airline found for user: " + userId);
//        } catch (FeignException e) {
//            throw new RuntimeException("Failed to fetch airline from airline-core-service: " + e.getMessage(), e);
//        }
//    }

}

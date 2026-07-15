package com.airline.pricing_service.service.impl;

import com.airline.payload.request.FareRequest;
import com.airline.payload.response.FareResponse;
import com.airline.pricing_service.mapper.FareMapper;
import com.airline.pricing_service.model.Fare;
import com.airline.pricing_service.repository.FareRepository;
import com.airline.pricing_service.service.FareService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FareServiceImpl implements FareService {

    private final FareRepository fareRepository;

    @Override
    public FareResponse createFare(FareRequest request) {
        if (fareRepository.existsByFlightIdAndCabinClassIdAndName(
                request.getFlightId(), request.getCabinClassId(), request.getName())) {
            throw new IllegalArgumentException(
                    "Fare '" + request.getName() + "' already exists for this flight and cabin class");
        }
        Fare fare = FareMapper.toEntity(request);
        Fare saved = fareRepository.save(fare);
        return FareMapper.toResponse(saved);

    }

    @Override
    public List<FareResponse> createFares(List<FareRequest> requests) {
        Set<Long> flightIds = requests.stream()
                .map(FareRequest::getFlightId)
                .collect(Collectors.toSet());
        Set<String> existingKeys = fareRepository.findExistingFareKeys(flightIds);

        List<Fare> toSave = requests.stream()
                .filter(req -> !existingKeys.contains(
                        req.getFlightId() + ":" + req.getCabinClassId() + ":" + req.getName()))
                .map(FareMapper::toEntity)
                .collect(Collectors.toList());

        return fareRepository.saveAll(toSave).stream()
                .map(FareMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public FareResponse getFareById(Long id) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found with id: " + id));
        return FareMapper.toResponse(fare);

    }

    @Override
    public List<FareResponse> getFaresByFlightIdAndCabinClassId(Long flightId, Long cabinClassId) {
        return fareRepository.findByFlightIdAndCabinClassId(flightId, cabinClassId).stream()
                .map(FareMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FareResponse updateFare(Long id, FareRequest request) {
        Fare existing = fareRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found with id: " + id));

        if (fareRepository.existsByFlightIdAndCabinClassIdAndNameAndIdNot(
                request.getFlightId(), request.getCabinClassId(), request.getName(), id)) {
            throw new IllegalArgumentException(
                    "Fare '" + request.getName() + "' already exists for this flight and cabin class");
        }

        FareMapper.updateEntity(request, existing);
        Fare saved = fareRepository.save(existing);
        return FareMapper.toResponse(saved);

    }

    @Override
    public void deleteFare(Long id) {
        Fare fare = fareRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Fare not found with id: " + id));
        fareRepository.delete(fare);

    }

    @Override
    public List<Fare> getFares() {
        return fareRepository.findAll();
    }

    @Override
    public Map<Long, FareResponse> getLowestFarePerFlight(List<Long> flightIds, Long cabinClassId) {
        if (flightIds == null || flightIds.isEmpty()) return Map.of();

        List<Fare> fares = fareRepository.findByFlightIdInAndCabinClassId(flightIds, cabinClassId);

        System.out.println("fares: -----------: " + fares.size());

        // Group by flightId and keep only the cheapest fare per flight
        Map<Long,FareResponse> result= fares.stream()
                .collect(Collectors.toMap(
                        Fare::getFlightId,
                        fare -> fare,
                        // merge: keep the fare with the lower total price
                        (existing, candidate) ->
                                candidate.getTotalPrice() < existing.getTotalPrice()
                                        ? candidate : existing
                ))
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> FareMapper.toResponse(e.getValue())
                ));
        System.out.println("result: -----------: lowest fare" + result);
        return result;

    }

    @Override
    public FareResponse getLowestFareForFlightAndCabin(Long flightId, Long cabinClassId) {
        List<Fare> fares = fareRepository.findByFlightIdAndCabinClassId(
                flightId,
                cabinClassId
        );

        Fare lowestFare = fares.stream()
                .min(Comparator.comparingDouble(Fare::getTotalPrice))
                .orElse(null);

        return FareMapper.toResponse(lowestFare);
    }

    @Override
    public Map<Long, FareResponse> getFaresByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Map.of();
        return fareRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Fare::getId, FareMapper::toResponse));

    }
}

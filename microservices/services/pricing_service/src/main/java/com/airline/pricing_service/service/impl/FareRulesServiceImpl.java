package com.airline.pricing_service.service.impl;

import com.airline.payload.request.FareRulesRequest;
import com.airline.payload.response.FareRulesResponse;
import com.airline.pricing_service.mapper.FareRulesMapper;
import com.airline.pricing_service.model.Fare;
import com.airline.pricing_service.model.FareRules;
import com.airline.pricing_service.repository.FareRepository;
import com.airline.pricing_service.repository.FareRulesRepository;
import com.airline.pricing_service.service.FareRulesService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FareRulesServiceImpl implements FareRulesService {

    private final FareRulesRepository fareRulesRepository;
    private final FareRepository fareRepository;
    @Override
    public FareRulesResponse createFareRules(FareRulesRequest request) {
        Fare fare = fareRepository.findById(request.getFareId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare not found with id: " + request.getFareId()));

        if (fareRulesRepository.existsByFareId(request.getFareId())) {
            throw new IllegalArgumentException(
                    "Fare rules already exist for fare id: " + request.getFareId());
        }

        FareRules fareRules = FareRulesMapper.toEntity(request, fare);
        FareRules saved = fareRulesRepository.save(fareRules);
        return FareRulesMapper.toResponse(saved);

    }

    @Override
    public FareRulesResponse getFareRulesById(Long id) {
        FareRules fareRules = fareRulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found with id: " + id));
        return FareRulesMapper.toResponse(fareRules);

    }

    @Override
    public FareRulesResponse getFareRulesByFareId(Long fareId) {
        FareRules fareRules = fareRulesRepository.findByFareId(fareId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found for fare id: " + fareId));
        return FareRulesMapper.toResponse(fareRules);

    }

    @Override
    public List<FareRulesResponse> getFareRulesByAirlineId(Long airlineId) {
        return fareRulesRepository.findByAirlineId(airlineId).stream()
                .map(FareRulesMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public FareRulesResponse updateFareRules(Long id, FareRulesRequest request) {
        FareRules existing = fareRulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found with id: " + id));

        FareRulesMapper.updateEntity(request, existing);
        FareRules saved = fareRulesRepository.save(existing);
        return FareRulesMapper.toResponse(saved);

    }

    @Override
    public void deleteFareRules(Long id) {
        FareRules fareRules = fareRulesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare rules not found with id: " + id));
        fareRulesRepository.delete(fareRules);
    }
}

package com.airline.pricing_service.service.impl;

import com.airline.payload.request.BaggagePolicyRequest;
import com.airline.payload.response.BaggagePolicyResponse;
import com.airline.pricing_service.mapper.BaggagePolicyMapper;
import com.airline.pricing_service.model.BaggagePolicy;
import com.airline.pricing_service.model.Fare;
import com.airline.pricing_service.repository.BaggagePolicyRepository;
import com.airline.pricing_service.repository.FareRepository;
import com.airline.pricing_service.service.BaggagePolicyService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BaggagePolicyServiceImpl implements BaggagePolicyService {

    private final BaggagePolicyRepository baggagePolicyRepository;
    private final FareRepository fareRepository;

    @Override
    public BaggagePolicyResponse createBaggagePolicy(BaggagePolicyRequest request) {
        Fare fare = fareRepository.findById(request.getFareId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fare not found with id: " + request.getFareId()));

        if (baggagePolicyRepository.existsByFareId(request.getFareId())) {
            throw new IllegalArgumentException(
                    "Baggage policy already exists for fare id: " + request.getFareId());
        }
        BaggagePolicy policy = BaggagePolicyMapper.toEntity(request, fare);
        BaggagePolicy saved = baggagePolicyRepository.save(policy);
        return BaggagePolicyMapper.toResponse(saved);

    }

    @Override
    public List<BaggagePolicyResponse> createBaggagePolicies(List<BaggagePolicyRequest> requests) {
        List<Long> fareIds = requests.stream()
                .map(BaggagePolicyRequest::getFareId)
                .collect(Collectors.toList());

        // Batch-fetch all required Fare entities; throw if any are missing
        Map<Long, Fare> fareMap = fareRepository.findAllById(fareIds).stream()
                .collect(Collectors.toMap(Fare::getId, fare -> fare));
        fareIds.forEach(fareId -> {
            if (!fareMap.containsKey(fareId)) {
                throw new EntityNotFoundException("Fare not found with id: " + fareId);
            }
        });

        // Single DB call to find fareIds that already have a policy
        Set<Long> alreadyHasPolicy = baggagePolicyRepository.findFareIdsWithExistingPolicy(fareIds);

        List<BaggagePolicy> toSave = requests.stream()
                .filter(req -> !alreadyHasPolicy.contains(req.getFareId()))
                .map(req -> BaggagePolicyMapper.toEntity(req, fareMap.get(req.getFareId())))
                .collect(Collectors.toList());

        return baggagePolicyRepository.saveAll(toSave).stream()
                .map(BaggagePolicyMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public BaggagePolicyResponse getBaggagePolicyById(Long id) {
        BaggagePolicy policy = baggagePolicyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Baggage policy not found with id: " + id));
        return BaggagePolicyMapper.toResponse(policy);
    }

    @Override
    public BaggagePolicyResponse getBaggagePolicyByFareId(Long fareId) {
        BaggagePolicy policy = baggagePolicyRepository.findByFareId(fareId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Baggage policy not found for fare id: " + fareId));
        return BaggagePolicyMapper.toResponse(policy);

    }

    @Override
    public List<BaggagePolicyResponse> getBaggagePoliciesByAirlineId(Long airlineId) {
        return baggagePolicyRepository.findByAirlineId(airlineId).stream()
                .map(BaggagePolicyMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BaggagePolicyResponse updateBaggagePolicy(Long id, BaggagePolicyRequest request) {
        BaggagePolicy existing = baggagePolicyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Baggage policy not found with id: " + id));

        BaggagePolicyMapper.updateEntity(request, existing);
        BaggagePolicy saved = baggagePolicyRepository.save(existing);
        return BaggagePolicyMapper.toResponse(saved);

    }

    @Override
    public void deleteBaggagePolicy(Long id) {
        BaggagePolicy policy = baggagePolicyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Baggage policy not found with id: " + id));
        baggagePolicyRepository.delete(policy);

    }
}

package com.airlines.anciallary_service.service.impl;

import com.airline.payload.request.InsuranceCoverageRequest;
import com.airline.payload.response.InsuranceCoverageResponse;
import com.airlines.anciallary_service.mapper.InsuranceCoverageMapper;
import com.airlines.anciallary_service.model.Ancillary;
import com.airlines.anciallary_service.model.InsuranceCoverage;
import com.airlines.anciallary_service.repository.AncillaryRepository;
import com.airlines.anciallary_service.repository.InsuranceCoverageRepository;
import com.airlines.anciallary_service.service.InsuranceCoverageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsuranceCoverageServiceImpl implements InsuranceCoverageService {

    private final InsuranceCoverageRepository coverageRepository;
    private final AncillaryRepository ancillaryRepository;

    @Override
    public InsuranceCoverageResponse createCoverage(InsuranceCoverageRequest request) throws Exception {
        Ancillary ancillary = ancillaryRepository.findById(request.getAncillaryId())
                .orElseThrow(() -> new Exception(
                        "Ancillary not found with ID: " + request.getAncillaryId()));

        InsuranceCoverage coverage = InsuranceCoverageMapper.toEntity(request, ancillary);
        InsuranceCoverage saved = coverageRepository.save(coverage);
        return InsuranceCoverageMapper.toResponse(saved);

    }

    @Override
    public List<InsuranceCoverageResponse> createCoveragesBulk(List<InsuranceCoverageRequest> requests) throws Exception {
        if (requests == null || requests.isEmpty()) {
            throw new IllegalArgumentException("Coverage request list cannot be empty");
        }

        Long ancillaryId = requests.get(0).getAncillaryId();
        boolean allSameAncillary = requests.stream()
                .allMatch(req -> req.getAncillaryId().equals(ancillaryId));

        if (!allSameAncillary) {
            throw new IllegalArgumentException(
                    "All coverages in bulk request must belong to the same ancillary");
        }

        Ancillary ancillary = ancillaryRepository.findById(ancillaryId)
                .orElseThrow(() -> new Exception(
                        "Ancillary not found with ID: " + ancillaryId));

        List<InsuranceCoverage> coverages = requests.stream()
                .map(request -> InsuranceCoverageMapper.toEntity(request, ancillary))
                .collect(Collectors.toList());

        List<InsuranceCoverage> saved = coverageRepository.saveAll(coverages);
        return saved.stream()
                .map(InsuranceCoverageMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public InsuranceCoverageResponse updateCoverage(Long id, InsuranceCoverageRequest request) throws Exception {
        InsuranceCoverage existing = coverageRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "Insurance coverage not found with ID: " + id));

        Ancillary ancillary = null;
        if (request.getAncillaryId() != null) {
            ancillary = ancillaryRepository.findById(request.getAncillaryId())
                    .orElseThrow(() -> new Exception(
                            "Ancillary not found with ID: " + request.getAncillaryId()));
        }

        InsuranceCoverageMapper.updateEntityFromRequest(existing, request, ancillary);
        InsuranceCoverage updated = coverageRepository.save(existing);
        return InsuranceCoverageMapper.toResponse(updated);

    }

    @Override
    public void deleteCoverage(Long id) throws Exception {
        InsuranceCoverage coverage = coverageRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "Insurance coverage not found with ID: " + id));
        coverageRepository.delete(coverage);

    }

    @Override
    public InsuranceCoverageResponse getCoverageById(Long id) throws Exception {
        InsuranceCoverage coverage = coverageRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "Insurance coverage not found with ID: " + id));
        return InsuranceCoverageMapper.toResponse(coverage);

    }

    @Override
    public List<InsuranceCoverageResponse> getCoveragesByAncillaryId(Long ancillaryId) {
        return coverageRepository.findByAncillaryIdAndActiveTrue(ancillaryId).stream()
                .map(InsuranceCoverageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InsuranceCoverageResponse> getActiveCoveragesByAncillaryId(Long ancillaryId) {
        return coverageRepository.findByAncillaryIdAndActiveTrue(ancillaryId).stream()
                .map(InsuranceCoverageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InsuranceCoverageResponse> getAllCoverages() {
        return coverageRepository.findAll().stream()
                .map(InsuranceCoverageMapper::toResponse)
                .collect(Collectors.toList());
    }
}

package com.airlines.anciallary_service.service;

import com.airline.payload.request.InsuranceCoverageRequest;
import com.airline.payload.response.InsuranceCoverageResponse;

import java.util.List;

public interface InsuranceCoverageService {
    InsuranceCoverageResponse createCoverage(InsuranceCoverageRequest request) throws Exception;

    List<InsuranceCoverageResponse> createCoveragesBulk(List<InsuranceCoverageRequest> requests) throws Exception;

    InsuranceCoverageResponse updateCoverage(Long id, InsuranceCoverageRequest request) throws Exception;

    void deleteCoverage(Long id) throws Exception;

    InsuranceCoverageResponse getCoverageById(Long id) throws Exception;

    List<InsuranceCoverageResponse> getCoveragesByAncillaryId(
            Long ancillaryId);

    List<InsuranceCoverageResponse> getActiveCoveragesByAncillaryId(
            Long ancillaryId);

    List<InsuranceCoverageResponse> getAllCoverages();
}

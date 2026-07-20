package com.airlines.anciallary_service.repository;

import com.airline.enums.CoverageType;
import com.airlines.anciallary_service.model.Ancillary;
import com.airlines.anciallary_service.model.InsuranceCoverage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InsuranceCoverageRepository extends JpaRepository<InsuranceCoverage,Long> {

    List<InsuranceCoverage> findByAncillary(Ancillary ancillary);

    List<InsuranceCoverage> findByAncillaryAndActiveTrue(Ancillary ancillary);

    List<InsuranceCoverage> findByCoverageType(CoverageType coverageType);

    List<InsuranceCoverage> findByAncillaryIdAndActiveTrue(Long ancillaryId);
}

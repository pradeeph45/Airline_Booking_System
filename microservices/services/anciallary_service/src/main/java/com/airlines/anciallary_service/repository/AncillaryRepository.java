package com.airlines.anciallary_service.repository;

import com.airlines.anciallary_service.model.Ancillary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AncillaryRepository extends JpaRepository<Ancillary,Long> {

    List<Ancillary> findByAirlineId(Long airlineId);

}

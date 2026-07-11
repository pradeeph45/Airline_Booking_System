package com.airline.airline_core_service.repository;

import com.airline.airline_core_service.model.Airline;
import com.airline.enums.AirlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline,Long> {

    Optional<Airline> findByOwnerId(Long ownerId);

    List<Airline> findByStatus(AirlineStatus airlineStatus);
}

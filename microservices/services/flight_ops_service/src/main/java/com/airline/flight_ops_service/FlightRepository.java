package com.airline.flight_ops_service;

import com.airline.payload.response.FlightResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightRepository extends JpaRepository<Flight,Long> {

    Page<FlightResponse> findByAirlineId(Long airlineId, Pageable pageable);

    boolean existsByFlightNumber(String flightNumber);
}

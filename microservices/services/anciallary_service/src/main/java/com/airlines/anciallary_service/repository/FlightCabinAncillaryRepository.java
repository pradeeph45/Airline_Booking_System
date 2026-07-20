package com.airlines.anciallary_service.repository;

import com.airline.enums.AncillaryType;
import com.airlines.anciallary_service.model.FlightCabinAncillary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FlightCabinAncillaryRepository extends JpaRepository<FlightCabinAncillary,Long> {
    List<FlightCabinAncillary> findByFlightId(Long flightId);

    List<FlightCabinAncillary> findByFlightIdAndCabinClassId(Long flightId, Long cabinClassId);

    List<FlightCabinAncillary> findByCabinClassId(Long cabinClassId);

    Optional<FlightCabinAncillary> findByFlightIdAndCabinClassIdAndAncillary_Type(
            Long flightId, Long cabinClassId, AncillaryType type);

    List<FlightCabinAncillary> findAllByFlightIdAndCabinClassIdAndAncillary_Type(
            Long flightId, Long cabinClassId, AncillaryType type);

}

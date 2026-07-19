package com.airlines.seat_service.repository;

import com.airline.enums.CabinClassType;
import com.airlines.seat_service.model.CabinClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CabinClassRepository extends JpaRepository<CabinClass,Long> {

    boolean existsByCode(String code);
    boolean existsByCodeAndAircraftId(String code, Long aircraftId);
    boolean existsByCodeAndAircraftIdAndIdNot(String code, Long aircraftId, Long id);
    List<CabinClass> findByAircraftId(Long aircraftId);

    CabinClass findByAircraftIdAndName(Long flightId, CabinClassType cabinClassType);

}

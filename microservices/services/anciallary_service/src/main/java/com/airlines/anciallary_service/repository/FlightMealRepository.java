package com.airlines.anciallary_service.repository;

import com.airlines.anciallary_service.model.FlightMeal;
import com.airlines.anciallary_service.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightMealRepository extends JpaRepository<FlightMeal,Long> {

    Optional<FlightMeal> findByFlightIdAndMeal(Long flightId, Meal meal);

    void deleteByFlightId(Long flightId);

}

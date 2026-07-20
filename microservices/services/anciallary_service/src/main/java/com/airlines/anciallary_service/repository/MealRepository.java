package com.airlines.anciallary_service.repository;

import com.airlines.anciallary_service.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealRepository extends JpaRepository<Meal,Long>, JpaSpecificationExecutor<Meal> {

    Optional<Meal> findByCode(String code);
}

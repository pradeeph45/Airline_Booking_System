package com.airlines.anciallary_service.service.impl;

import com.airline.payload.request.MealRequest;
import com.airline.payload.response.MealResponse;
import com.airlines.anciallary_service.mapper.MealMapper;
import com.airlines.anciallary_service.model.Meal;
import com.airlines.anciallary_service.repository.MealRepository;
import com.airlines.anciallary_service.service.MealService;
import com.airlines.anciallary_service.specification.MealSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    // private final AirlineIntegrationService airlineIntegrationService;

    @Override
    public MealResponse create(Long userId, MealRequest request) throws Exception {

        Meal meal = Meal.builder()
                .code(request.getCode())
                .name(request.getName())
                .mealType(request.getMealType())
                .dietaryRestriction(request.getDietaryRestriction())
                .ingredients(request.getIngredients())
                .imageUrl(request.getImageUrl())
                .available(request.getAvailable())
                .requiresAdvanceBooking(request.getRequiresAdvanceBooking() != null
                        ? request.getRequiresAdvanceBooking() : false)
                .advanceBookingHours(request.getAdvanceBookingHours())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .airlineId(userId)
                .build();

        Meal savedMeal = mealRepository.save(meal);
        return MealMapper.toResponse(savedMeal);

    }

    @Override
    public List<MealResponse> bulkCreate(Long userId, List<MealRequest> requests) throws Exception {
        List<MealResponse> responses = new ArrayList<>();

        for (MealRequest request : requests) {
            Specification<Meal> spec = MealSpecification.hasCodeAndAirlineId(request.getCode(), userId);
            if (mealRepository.exists(spec)) {
                continue;
            }

            Meal meal = Meal.builder()
                    .code(request.getCode())
                    .name(request.getName())
                    .mealType(request.getMealType())
                    .dietaryRestriction(request.getDietaryRestriction())
                    .ingredients(request.getIngredients())
                    .imageUrl(request.getImageUrl())
                    .available(request.getAvailable())
                    .requiresAdvanceBooking(request.getRequiresAdvanceBooking() != null
                            ? request.getRequiresAdvanceBooking() : false)
                    .advanceBookingHours(request.getAdvanceBookingHours())
                    .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                    .airlineId(userId)
                    .build();

            Meal savedMeal = mealRepository.save(meal);
            responses.add(MealMapper.toResponse(savedMeal));
        }

        return responses;

    }

    @Override
    public MealResponse getById(Long id) throws Exception {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal not found with id: " + id));
        return MealMapper.toResponse(meal);

    }

    @Override
    public List<MealResponse> getByAirlineId(Long userId) {
        Specification<Meal> spec = MealSpecification.hasAirlineId(userId);
        return mealRepository.findAll(spec).stream()
                .map(MealMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MealResponse update(Long userId, Long id, MealRequest request) throws Exception {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal not found with id: " + id));

        if (!meal.getCode().equals(request.getCode())) {
            Specification<Meal> spec = MealSpecification.hasCodeAndAirlineId(request.getCode(), userId);
            if (mealRepository.exists(spec)) {
                throw new IllegalArgumentException(
                        "Meal with code " + request.getCode() + " already exists for this airline");
            }
        }

        meal.setCode(request.getCode());
        meal.setName(request.getName());
        meal.setMealType(request.getMealType());
        meal.setDietaryRestriction(request.getDietaryRestriction());
        meal.setIngredients(request.getIngredients());
        meal.setImageUrl(request.getImageUrl());
        meal.setAvailable(request.getAvailable());
        meal.setRequiresAdvanceBooking(request.getRequiresAdvanceBooking());
        meal.setAdvanceBookingHours(request.getAdvanceBookingHours());
        meal.setDisplayOrder(request.getDisplayOrder());

        Meal updatedMeal = mealRepository.save(meal);
        return MealMapper.toResponse(updatedMeal);

    }

    @Override
    public void delete(Long id) throws Exception {
        if (!mealRepository.existsById(id)) {
            throw new Exception("Meal not found with id: " + id);
        }
        mealRepository.deleteById(id);
    }

    @Override
    public MealResponse updateAvailability(Long id, Boolean available) throws Exception {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new Exception("Meal not found with id: " + id));
        meal.setAvailable(available);
        Meal updatedMeal = mealRepository.save(meal);
        return MealMapper.toResponse(updatedMeal);

    }
}

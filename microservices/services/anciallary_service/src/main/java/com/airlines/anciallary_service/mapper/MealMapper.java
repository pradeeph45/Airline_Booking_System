package com.airlines.anciallary_service.mapper;

import com.airline.payload.response.MealResponse;
import com.airlines.anciallary_service.model.Meal;

public class MealMapper {

    public static MealResponse toResponse(Meal meal) {
        if (meal == null) {
            return null;
        }

        return MealResponse.builder()
                .id(meal.getId())
                .code(meal.getCode())

                .name(meal.getName())
                .mealType(meal.getMealType())
                .dietaryRestriction(meal.getDietaryRestriction())
                .ingredients(meal.getIngredients())
                .imageUrl(meal.getImageUrl())
                .available(meal.getAvailable())
                .requiresAdvanceBooking(meal.getRequiresAdvanceBooking())
                .advanceBookingHours(meal.getAdvanceBookingHours())
                .displayOrder(meal.getDisplayOrder())
                .airlineId(meal.getAirlineId())
                .createdAt(meal.getCreatedAt())
                .updatedAt(meal.getUpdatedAt())
                .build();
    }
}

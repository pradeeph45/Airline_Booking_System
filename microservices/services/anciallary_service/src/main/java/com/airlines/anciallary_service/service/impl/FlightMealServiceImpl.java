package com.airlines.anciallary_service.service.impl;

import com.airline.payload.request.FlightMealRequest;
import com.airline.payload.response.FlightMealResponse;
import com.airlines.anciallary_service.mapper.FlightMealMapper;
import com.airlines.anciallary_service.model.FlightMeal;
import com.airlines.anciallary_service.model.Meal;
import com.airlines.anciallary_service.repository.FlightMealRepository;
import com.airlines.anciallary_service.repository.MealRepository;
import com.airlines.anciallary_service.service.FlightMealService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightMealServiceImpl implements FlightMealService {

    private final FlightMealRepository flightMealRepository;
    private final MealRepository mealRepository;

    @Override
    public FlightMealResponse create(FlightMealRequest request) throws Exception {
        Meal meal = mealRepository.findById(request.getMealId())
                .orElseThrow(() -> new Exception(
                        "Meal not found with id: " + request.getMealId()));


        FlightMeal flightMeal = FlightMeal.builder()
                .flightId(request.getFlightId())
                .meal(meal)
                .available(request.getAvailable())
                .price(request.getPrice())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .build();

        FlightMeal saved = flightMealRepository.save(flightMeal);
        return FlightMealMapper.toResponse(saved);

    }

    @Override
    public List<FlightMealResponse> bulkCreate(List<FlightMealRequest> requests) throws Exception {
//        List<FlightMealResponse> responses = new ArrayList<>();
//
//        for (FlightMealRequest request : requests) {
//            Meal meal = mealRepository.findById(request.getMealId())
//                    .orElseThrow(() -> new Exception(
//                            "Meal not found with id: " + request.getMealId()));
//
////            Specification<FlightMeal> spec = FlightMealSpecification.hasFlightIdAndMealId(
////                    request.getFlightId(), request.getMealId());
////            if (flightMealRepository.exists(spec)) {
////                log.warn("Skipping flight meal - meal {} already assigned to flight {}",
////                        request.getMealId(), request.getFlightId());
//                continue;
//            }
//
//            FlightMeal flightMeal = FlightMeal.builder()
//                    .flightId(request.getFlightId())
//                    .meal(meal)
//                    .available(request.getAvailable())
//                    .price(request.getPrice())
//                    .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
//                    .build();
//
//            FlightMeal saved = flightMealRepository.save(flightMeal);
//            responses.add(FlightMealMapper.toResponse(saved));
//        }
//
//        log.info("Successfully created {} flight meals", responses.size());
//        return responses;
      return null;
    }

    @Override
    public FlightMealResponse getById(Long id) throws Exception {
        FlightMeal flightMeal = flightMealRepository.findById(id)
                .orElseThrow(() -> new Exception("FlightMeal not found with id: " + id));
        return FlightMealMapper.toResponse(flightMeal);

    }

    @Override
    public List<FlightMealResponse> getByFlightId(Long flightId) {
//        Specification<FlightMeal> spec = FlightMealSpecification.hasFlightId(flightId);
//        return flightMealRepository.findAll(spec).stream()
//                .map(FlightMealMapper::toResponse)
//                .collect(Collectors.toList());
        return List.of();
    }

    @Override
    public List<FlightMealResponse> getAllByIds(List<Long> Ids) {
        List<FlightMeal> meals = flightMealRepository.findAllById(Ids);
        return meals.stream().map(
                FlightMealMapper::toResponse
        ).collect(Collectors.toList());

    }

    @Override
    public FlightMealResponse update(Long id, FlightMealRequest request) throws Exception {
        FlightMeal flightMeal = flightMealRepository.findById(id)
                .orElseThrow(() -> new Exception("FlightMeal not found with id: " + id));

        // Update flight ID if changed
        if (!flightMeal.getFlightId().equals(request.getFlightId())) {
            flightMeal.setFlightId(request.getFlightId());
        }

        // Update meal if changed
        if (!flightMeal.getMeal().getId().equals(request.getMealId())) {
            Meal meal = mealRepository.findById(request.getMealId())
                    .orElseThrow(() -> new Exception(
                            "Meal not found with id: " + request.getMealId()));
            flightMeal.setMeal(meal);
        }

        flightMeal.setAvailable(request.getAvailable());
        flightMeal.setPrice(request.getPrice());
        flightMeal.setDisplayOrder(request.getDisplayOrder());

        FlightMeal updated = flightMealRepository.save(flightMeal);

        return FlightMealMapper.toResponse(updated);

    }

    @Override
    public void delete(Long id) throws Exception {
        if (!flightMealRepository.existsById(id)) {
            throw new Exception("FlightMeal not found with id: " + id);
        }
        flightMealRepository.deleteById(id);

    }

    @Override
    public FlightMealResponse updateAvailability(Long id, Boolean available) throws Exception {
        FlightMeal flightMeal = flightMealRepository.findById(id)
                .orElseThrow(() -> new Exception("FlightMeal not found with id: " + id));
        flightMeal.setAvailable(available);
        FlightMeal updated = flightMealRepository.save(flightMeal);
        return FlightMealMapper.toResponse(updated);
    }

    @Override
    public Double calculateMealPrice(List<Long> mealIds) {
        List<FlightMeal> meals=flightMealRepository.findAllById(mealIds);
        double total=0.0;
        for(FlightMeal flightMeal : meals) {
            total+=flightMeal.getPrice();
        }
        return total;
    }
}

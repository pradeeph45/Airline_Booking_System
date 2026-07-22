package com.airlines.booking_service.service.impl;

import com.airline.payload.request.PassengerRequest;
import com.airline.payload.response.PassengerResponse;
import com.airlines.booking_service.mapper.PassengerMapper;
import com.airlines.booking_service.model.Passenger;
import com.airlines.booking_service.repository.PassengerRepository;
import com.airlines.booking_service.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;

    @Override
    public PassengerResponse createPassenger(PassengerRequest request, Long userId) throws Exception {
        Passenger passenger = PassengerMapper.toEntity(request);
        passenger.setPrimaryUserId(userId);
        Passenger saved = passengerRepository.save(passenger);
        return PassengerMapper.toResponse(saved);

    }

    @Override
    public Passenger findOrCreatePassengerEntity(PassengerRequest request, Long userId) {
        Optional<Passenger> existing = findExistingPassengerOptional(request);
        if (existing.isPresent()) {
            Passenger passenger = existing.get();
            PassengerMapper.updateEntityFromRequest(request, passenger);
            return passengerRepository.save(passenger);
        }

        Passenger newPassenger = PassengerMapper.toEntity(request);
        newPassenger.setPrimaryUserId(userId);
        return passengerRepository.save(newPassenger);
    }

    @Override
    public Passenger findExistingPassenger(PassengerRequest request) {
        return findExistingPassengerOptional(request).orElse(null);
    }

    @Override
    public boolean existsById(Long id) {
        return passengerRepository.existsById(id);
    }

    @Override
    public long count() {
        return passengerRepository.count();
    }

    private Optional<Passenger> findExistingPassengerOptional(PassengerRequest request) {
        if (request.getPassportNumber() != null && !request.getPassportNumber().isEmpty()) {
            Optional<Passenger> byPassport = passengerRepository.findByPassportNumber(
                    request.getPassportNumber());
            if (byPassport.isPresent()) {
                return byPassport;
            }
        }

        return passengerRepository.findByEmailAndPhoneAndDateOfBirth(
                request.getEmail(), request.getPhone(), request.getDateOfBirth());
    }
}

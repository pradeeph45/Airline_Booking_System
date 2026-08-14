package com.airlines.booking_service.client;

import com.airline.payload.dto.PaymentDTO;
import com.airline.payload.request.PaymentInitiateRequest;
import com.airline.payload.response.PaymmentInitiateResponse;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/api/payments/initiate")
    PaymmentInitiateResponse initiatePayment(
            @Valid @RequestBody PaymentInitiateRequest request,
            @RequestHeader("X-User-Id") Long userId);

    @GetMapping("/api/payments/booking/{bookingId}")
    PaymentDTO getPaymentByBookingId(@PathVariable Long bookingId);

    @PostMapping("/api/payments/batch/bookings")
    Map<Long, PaymentDTO> getPaymentsByBookingIds(@RequestBody List<Long> bookingIds);

}

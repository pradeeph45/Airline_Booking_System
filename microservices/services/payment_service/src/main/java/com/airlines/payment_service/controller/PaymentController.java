package com.airlines.payment_service.controller;

import com.airline.payload.dto.PaymentDTO;
import com.airline.payload.request.PaymentInitiateRequest;
import com.airline.payload.request.PaymentVerifyRequest;
import com.airline.payload.response.PaymmentInitiateResponse;
import com.airlines.payment_service.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(
            @Valid @RequestBody PaymentInitiateRequest request,
            @RequestHeader("X-User-Id") Long userId) throws Exception {


        PaymmentInitiateResponse response = paymentService
                .initiatePayment(request);
        return ResponseEntity.ok(response);


    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(
            @Valid @RequestBody PaymentVerifyRequest request)
            throws Exception {

        PaymentDTO payment = paymentService.verifyPayment(request);
        return ResponseEntity.ok(payment);

    }

    @PostMapping("/batch/bookings")
    public ResponseEntity<Map<Long, PaymentDTO>> getPaymentsByBookingIds(@RequestBody List<Long> bookingIds) {
        return ResponseEntity.ok(paymentService
                .getPaymentsByBookingIds(bookingIds));
    }

    @GetMapping
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestHeader("X-User-Id") Long userId) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC") ?
                Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PaymentDTO> payments = paymentService.getAllPayments(pageable);
        return ResponseEntity.ok(payments);
    }

    private record ErrorResponse(String message) {}

}

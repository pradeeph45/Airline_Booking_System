package com.airlines.payment_service.service;

import com.airline.payload.dto.PaymentDTO;
import com.airline.payload.request.PaymentInitiateRequest;
import com.airline.payload.request.PaymentVerifyRequest;
import com.airline.payload.response.PaymmentInitiateResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PaymentService {

    PaymmentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws Exception;

    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws Exception;

    Page<PaymentDTO> getAllPayments(Pageable pageable);

    Map<Long, PaymentDTO> getPaymentsByBookingIds(List<Long> bookingIds);

}

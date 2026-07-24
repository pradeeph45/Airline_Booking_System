package com.airlines.payment_service.service.impl;

import com.airline.enums.PaymentGateway;
import com.airline.enums.PaymentStatus;
import com.airline.payload.dto.PaymentDTO;
import com.airline.payload.dto.UserDTO;
import com.airline.payload.request.PaymentInitiateRequest;
import com.airline.payload.request.PaymentVerifyRequest;
import com.airline.payload.response.PaymmentInitiateResponse;
import com.airlines.payment_service.mapper.PaymentMapper;
import com.airlines.payment_service.model.Payment;
import com.airlines.payment_service.repository.PaymentRepository;
import com.airlines.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
//    private final PaymentEventProducer paymentEventProducer;
//    private final RazorpayService razorpayService;
//    private final UserClient userClient;

    @Override
    public PaymmentInitiateResponse initiatePayment(PaymentInitiateRequest request) throws Exception {
        try {
            // Check if payment already exists for this booking
            paymentRepository.findByBookingId(request.getBookingId())
                    .ifPresent(existingPayment -> {
                        if (existingPayment.getStatus() == PaymentStatus.SUCCESS) {
                            throw new RuntimeException("Payment already completed for this booking");
                        }
                    });

            // Create payment entity
            Payment payment = Payment.builder()
                    .userId(request.getUserId())
                    .bookingId(request.getBookingId())
                    .amount(request.getAmount())
                    .provider(request.getGateway())
                    .status(PaymentStatus.PENDING)
                    .transactionId(generateTransactionId())
                    .build();

            payment = paymentRepository.save(payment);

            // Create response based on gateway
            PaymmentInitiateResponse response = PaymmentInitiateResponse.builder()
                    .paymentId(payment.getId())
                    .gateway(request.getGateway())
                    .transactionId(payment.getTransactionId())
                    .amount(request.getAmount())
                    .description(request.getDescription())
                    .success(true)
                    .message("Payment initiated successfully")
                    .build();

            if (request.getGateway() == PaymentGateway.RAZORPAY) {


        //        UserDTO user=userClient.getUserById(payment.getUserId());

//                PaymentLinkResponse paymentLinkResponse=razorpayService.createPaymentLink(
//                        user, payment
//                );
//                response.setCheckoutUrl(paymentLinkResponse.getPayment_link_url());
//                response.setRazorpayOrderId(paymentLinkResponse.getPayment_link_id());


            } else if (request.getGateway() == PaymentGateway.STRIPE) {
                String checkoutUrl = "https://checkout.stripe.com/pay/" + payment.getTransactionId();
                response.setCheckoutUrl(checkoutUrl);
                // TODO: Integrate with Stripe gateway service
            }
            return response;

        } catch (Exception e) {
            throw new Exception("Failed to initiate payment: " + e.getMessage());
        }

    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest request) throws Exception {
        // gatway payment
//        JSONObject paymentDetails = razorpayService
//                .fetchPaymentDetails(request.getRazorpayPaymentId());
//
//        System.out.println("gatway payment details: " + paymentDetails);
//
//
//        String status = paymentDetails.optString("status");
//        long amount = paymentDetails.optLong("amount");
//        long amountInRupees = amount / 100;


        // Extract 'notes' object
//        JSONObject notes = paymentDetails.getJSONObject("notes");
//
//        Long paymentId = Long.parseLong(notes.optString("payment_id"));


        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentException("Payment not found with ID: " + paymentId));


        boolean isValid = "captured".equalsIgnoreCase(status);

        if (payment.getProvider() == PaymentGateway.RAZORPAY) {

            if (isValid) {
                payment.setProviderPaymentId(request.getRazorpayPaymentId());

            }
        } else if (payment.getProvider() == PaymentGateway.STRIPE) {
//            isValid = stripeService.verifyPayment(request.getStripePaymentIntentId());
//
//            if (isValid) {
//                payment.setProviderPaymentId(request.getStripePaymentIntentId());
//            }
        }

        if (isValid) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());

            // Save payment first
            payment = paymentRepository.save(payment);
            System.out.println("send payment event and payment status is : "+status);
            paymentEventProducer.sendPaymentCompleted(payment);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailureReason("Payment verification failed");
            payment = paymentRepository.save(payment);

            paymentEventProducer.sendPaymentFailed(payment);
        }

        return PaymentMapper.toDTO(payment);

    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(PaymentMapper::toDTO);
    }

    @Override
    public Map<Long, PaymentDTO> getPaymentsByBookingIds(List<Long> bookingIds) {
        if (bookingIds == null || bookingIds.isEmpty()) return Map.of();
        return paymentRepository.findByBookingIdIn(bookingIds).stream()
                .collect(Collectors.toMap(Payment::getBookingId, PaymentMapper::toDTO));
    }

    private String generateTransactionId() {
        return "TXN_" + System.currentTimeMillis() + "_" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}

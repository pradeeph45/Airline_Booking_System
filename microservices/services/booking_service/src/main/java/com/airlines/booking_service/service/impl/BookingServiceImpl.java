package com.airlines.booking_service.service.impl;

import com.airline.enums.BookingStatus;
import com.airline.payload.request.BookingRequest;
import com.airline.payload.request.PassengerRequest;
import com.airline.payload.response.*;
import com.airlines.booking_service.mapper.BookingMapper;
import com.airlines.booking_service.model.Booking;
import com.airlines.booking_service.model.Passenger;
import com.airlines.booking_service.repository.BookingRepository;
import com.airlines.booking_service.service.BookingService;
import com.airlines.booking_service.service.PassengerService;
import com.airlines.booking_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerService passengerService;
    private final TicketService ticketService;
//    private final PricingIntegrationService pricingIntegrationService;
//    private final PricingClient pricingClient;
//    private final AncillaryClient ancillaryClient;
//    private final PaymentClient paymentClient;
//    private final SeatClient seatClient;
//    private final FlightClient flightClient;
//    private final AirlineClient airlineClient;


    @Override
    public BookingResponse createBooking(BookingRequest request, Long userId) throws Exception {
        String bookingReference = generateBookingReference();

        Set<Passenger> passengers = new HashSet<>();
        for (PassengerRequest passengerRequest : request.getPassengers()) {
            Passenger passenger = passengerService
                    .findOrCreatePassengerEntity(passengerRequest, userId);
            passengers.add(passenger);
        }

     //   FlightResponse flightResponse = flightClient.getFlightById(request.getFlightId());

        // Create booking entity with cross-service IDs
        Booking booking = BookingMapper.toEntity(
                request, userId, passengers, bookingReference);
        booking.setStatus(BookingStatus.PENDING);
    //    booking.setAirlineId(flightResponse.getAirline().getId());

        // Set seat instance IDs from passenger requests
        List<Long> seatInstanceIds = request.getPassengers().stream()
                .map(PassengerRequest::getSeatInstanceId)
                .collect(Collectors.toList());
        booking.setSeatInstanceIds(seatInstanceIds);

        // Save booking
        booking = bookingRepository.save(booking);

        // Set booking reference on passengers
        for (Passenger passenger : passengers) {
            passenger.setBooking(booking);
        }

        // Generate tickets
        ticketService.generateTicketsForBooking(booking);


        // Calculate total amount

        int passengerCount = booking.getPassengers().size();
//        Double fareTotal = pricingIntegrationService.calculateFareTotal(booking.getFareId()) * passengerCount;
//        Double seatPrice= seatClient.calculateSeatPrice(booking.getSeatInstanceIds());
//        Double ancillaryPrice=ancillaryClient.calculateAncillariesPrice(booking.getAncillaryIds());
//        Double mealPrice=ancillaryClient.calculateMealPrice(booking.getMealIds());

     //   Double totalPrice=fareTotal+seatPrice+ancillaryPrice+mealPrice;


        // Initiate payment
//        PaymentInitiateRequest paymentRequest = PaymentInitiateRequest.builder()
//                .userId(userId)
//                .bookingId(booking.getId())
//                .amount(totalPrice)
//                .gateway(PaymentGateway.RAZORPAY)
//                .description("Booking: " + bookingReference)
//                .build();

 //       System.out.println("Booking created successfully: {}   - "+paymentRequest);

   //     PaymentInitiateResponse paymentInit = paymentClient.initiatePayment(paymentRequest, userId);
//        if (paymentInit == null) {
//            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
//                    "Payment service is temporarily unavailable. Please retry.");
//        }
//        return paymentInit;


        return convertBookingResponse(booking);
    }

    @Override
    public BookingResponse updateBooking(Long id, BookingRequest request) throws Exception {
        return null;
    }

    @Override
    public BookingResponse getBookingById(Long id) throws Exception {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new Exception(
                        "Booking not found with ID: " + id));
        return convertBookingResponse(booking);

    }

    @Override
    public List<BookingResponse> getBookingsByAirline(Long userId, String searchQuery, BookingStatus status, Long flightInstanceId, String sortDirection) {
       // AirlineResponse airlineResponse = airlineClient.getAirlineByOwner(userId);
       // Long airlineId = airlineResponse.getId();

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDirection)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "bookingDate");

        List<Booking> bookings = bookingRepository.findByAirlineWithFilters(
                userId, searchQuery, status, flightInstanceId, sort);

        return bookings.stream().map(
                this::convertBookingResponse
        ).toList();

    }

    @Override
    public List<BookingResponse> getBookingsByUser(Long userId) {
        return bookingRepository.findByUserId(userId).stream()
                .map(this::convertBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponse cancelBooking(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "Booking not found with ID: " + id));

        booking.setStatus(BookingStatus.CANCELLED);
        booking.setLastModified(LocalDateTime.now());
        Booking updated = bookingRepository.save(booking);

        return convertBookingResponse(updated);
    }

    @Override
    public void deleteBooking(Long id) throws Exception {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new Exception(
                        "Booking not found with ID: " + id));
        bookingRepository.delete(booking);
    }

    @Override
    public boolean existsById(Long id) {
        return bookingRepository.existsById(id);
    }

    @Override
    public long count() {
        return bookingRepository.count();
    }

    @Override
    public long countByFlightId(Long flightId) {
        return bookingRepository.countByFlightInstanceId(flightId);
    }

    private String generateBookingReference() {
        String reference;
        do {
            reference = "BK" + UUID.randomUUID().toString()
                    .substring(0, 8).toUpperCase();
        } while (bookingRepository.findByBookingReference(reference).isPresent());
        return reference;
    }

    private List<BookingResponse> enrichBatch(List<Booking> bookings) {
        if (bookings.isEmpty()) return Collections.emptyList();

        List<Long> bookingIds = bookings.stream().map(Booking::getId).collect(Collectors.toList());

        List<Long> fareIds = bookings.stream().map(Booking::getFareId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> flightIds = bookings.stream().map(Booking::getFlightId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> flightInstanceIds = bookings.stream().map(Booking::getFlightInstanceId)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<Long> allSeatIds = bookings.stream()
                .flatMap(b -> b.getSeatInstanceIds() != null ? b.getSeatInstanceIds().stream() : Stream.empty())
                .distinct().collect(Collectors.toList());
        List<Long> allAncillaryIds = bookings.stream()
                .flatMap(b -> b.getAncillaryIds() != null ? b.getAncillaryIds().stream() : Stream.empty())
                .distinct().collect(Collectors.toList());
        List<Long> allMealIds = bookings.stream()
                .flatMap(b -> b.getMealIds() != null ? b.getMealIds().stream() : Stream.empty())
                .distinct().collect(Collectors.toList());

        // One call per service
//        Map<Long, FareResponse> fareMap = pricingClient.getFaresByIds(fareIds);
//        Map<Long, FlightResponse> flightMap = flightClient.getFlightsByIds(flightIds);
//        Map<Long, FlightInstanceResponse> flightInstanceMap = flightClient.getFlightInstancesByIds(flightInstanceIds);
//        Map<Long, PaymentDTO> paymentMap = paymentClient.getPaymentsByBookingIds(bookingIds);
//
//        Map<Long, SeatInstanceResponse> seatMap = allSeatIds.isEmpty()
//                ? Collections.emptyMap()
//                : seatClient.getAllByIds(allSeatIds).stream()
//                .collect(Collectors.toMap(SeatInstanceResponse::getId, s -> s));
//        Map<Long, FlightCabinAncillaryResponse> ancillaryMap = allAncillaryIds.isEmpty()
//                ? Collections.emptyMap()
//                : ancillaryClient.getAllByIds(allAncillaryIds).stream()
//                .collect(Collectors.toMap(FlightCabinAncillaryResponse::getId, a -> a));
//        Map<Long, FlightMealResponse> mealMap = allMealIds.isEmpty()
//                ? Collections.emptyMap()
//                : ancillaryClient.getMealsByIds(allMealIds).stream()
//                .collect(Collectors.toMap(FlightMealResponse::getId, m -> m));

//        return bookings.stream().map(booking -> {
//            List<SeatInstanceResponse> seats = booking.getSeatInstanceIds() != null
//                    ? booking.getSeatInstanceIds().stream()
//                    .map(seatMap::get).filter(Objects::nonNull).collect(Collectors.toList())
//                    : Collections.emptyList();
//            List<FlightCabinAncillaryResponse> ancillaries = booking.getAncillaryIds() != null
//                    ? booking.getAncillaryIds().stream()
//                    .map(ancillaryMap::get).filter(Objects::nonNull).collect(Collectors.toList())
//                    : Collections.emptyList();
//            List<FlightMealResponse> meals = booking.getMealIds() != null
//                    ? booking.getMealIds().stream()
//                    .map(mealMap::get).filter(Objects::nonNull).collect(Collectors.toList())
//                    : Collections.emptyList();
//
//            return BookingMapper.toResponse(
//                    booking,
//                    paymentMap.get(booking.getId()),
//                    fareMap.get(booking.getFareId()),
//                    flightMap.get(booking.getFlightId()),
//                    flightInstanceMap.get(booking.getFlightInstanceId()),
//                    ancillaries,
//                    meals,
//                    seats
//            );
//        }).collect(Collectors.toList());
        return List.of();
    }

    private BookingResponse convertBookingResponse(Booking booking)  {
//        List<FlightCabinAncillaryResponse> ancillaryResponses=ancillaryClient.getAllByIds(
//                booking.getAncillaryIds()
//        );
//        List<FlightMealResponse> mealResponses=ancillaryClient.getMealsByIds(booking.getMealIds());
//        PaymentDTO paymentDTO=paymentClient.getPaymentByBookingId(booking.getId());
//        FareResponse fareResponse=pricingClient.getFareById(booking.getFareId());
//        FlightResponse flightResponse=flightClient.getFlightById(booking.getFlightId());

//        List<SeatInstanceResponse> seatInstanceResponses=seatClient.getAllByIds(booking.getSeatInstanceIds());
//        FlightInstanceResponse flightInstanceResponse=flightClient.getFlightInstanceResponse(booking.getFlightInstanceId());


       // System.out.println("seat instances -------- "+seatInstanceResponses.size());

        return BookingMapper.toResponse(booking,
             //   paymentDTO,
                new FareResponse(),
                new FlightResponse(),
                new FlightInstanceResponse(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

}

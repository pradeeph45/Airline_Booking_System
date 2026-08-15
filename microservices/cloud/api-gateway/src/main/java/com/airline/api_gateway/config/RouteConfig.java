package com.airline.api_gateway.config;

import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.function.*;

@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> authRoute(){
        return GatewayRouterFunctions.route("auth-routes")
                .route(RequestPredicates.path("/auth/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("user-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> adminLocationServiceRoute(){
        return GatewayRouterFunctions.route("auth-location-routes")
                .route(RequestPredicates.POST("/auth/cities/**"), HandlerFunctions.http())
                .route(RequestPredicates.POST("/auth/airports/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("location-service"))
                .build();
    }

    @Bean
    @Order(1)
    public RouterFunction<ServerResponse> adminAirlineCoreServiceRoute(){
        return GatewayRouterFunctions.route("auth-airline-core-routes")
                .route(RequestPredicates.POST("/api/airlines/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("airline-core-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoutes(){
        return GatewayRouterFunctions.route("user-service-routes")
                .route(RequestPredicates.path("/api/users/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("user-service"))
                .build();
    }

    @Bean
    @Order(2)
    public RouterFunction<ServerResponse> airlineCoreServiceRoute(){
        return GatewayRouterFunctions.route("airline-core-routes")
                .route(RequestPredicates.POST("/api/airlines/**"), HandlerFunctions.http())
                .route(RequestPredicates.POST("/api/aircrafts/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("airline-core-service"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> seatServiceRoutes() {
        return GatewayRouterFunctions.route("seat-service-routes")
                .route(RequestPredicates.path("/api/cabin-classes/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/seat-maps/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/seats/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/seat-instances/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/flight-instance-cabins/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("seat-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("seat-service-cb", URI.create("forward:/fallback")))
//                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> flightOpsServiceRoutes() {
        return GatewayRouterFunctions.route("flight-ops-routes")
                .route(RequestPredicates.path("/api/flights/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/flight-instances/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/flight-schedules/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("flight-ops-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("flight-ops-service-cb", URI.create("forward:/fallback")))
 //               .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> pricingServiceRoutes() {
        return GatewayRouterFunctions.route("pricing-service-routes")
                .route(RequestPredicates.path("/api/fares/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/fare-rules/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/baggage-policies/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("pricing-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("pricing-service-cb", URI.create("forward:/fallback")))
 //               .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> AncillaryServiceRoutes() {
        return GatewayRouterFunctions.route("ancillary-service-routes")
                .route(RequestPredicates.path("/api/meals/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/ancillaries/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/insurance-coverages/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/flight-meals/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/flight-cabin-ancillaries/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("ancillary-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("ancillary-service-cb", URI.create("forward:/fallback")))
     //           .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    @Order(2)
    public RouterFunction<ServerResponse> locationServiceRoutes() {
        return GatewayRouterFunctions.route("location-service-routes")
                .route(RequestPredicates.path("/api/cities/**"), HandlerFunctions.http())
                .route(RequestPredicates.path("/api/airports/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("location-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("location-service-cb", URI.create("forward:/fallback")))
//                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceRoutes() {
        return GatewayRouterFunctions.route("booking-service-routes")
                .route(RequestPredicates.path("/api/bookings/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("booking-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("booking-service-cb", URI.create("forward:/fallback")))
//                .before(this::jwtAuthFilter)
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> paymentServiceRoutes() {
        return GatewayRouterFunctions.route("payment-service-routes")
                .route(RequestPredicates.path("/api/payments/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("payment-service"))
//                .filter(CircuitBreakerFilterFunctions.circuitBreaker("payment-service-cb", URI.create("forward:/fallback")))
 //               .before(this::jwtAuthFilter)
                .build();
    }

}

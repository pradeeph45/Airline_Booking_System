package com.airlines.anciallary_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class FlightMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cross-service reference: Flight lives in flight-ops-service
    @Column(name = "flight_id", nullable = false)
    private Long flightId;

    // Same bounded context: Meal lives in this service
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_id", nullable = false)
    private Meal meal;

    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    private Double price;

    @Builder.Default
    private Integer displayOrder = 0;
}

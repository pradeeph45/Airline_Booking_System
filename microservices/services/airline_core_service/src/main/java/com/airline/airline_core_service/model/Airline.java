package com.airline.airline_core_service.model;

import com.airline.embeddable.Support;
import com.airline.enums.AirlineStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Airline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true,nullable = false)
    private String iataCode;

    @Column(unique = true,nullable = false)
    private String icaoCode;

    @Column(nullable = false)
    private String name;

    private String alias;

    private String logoUrl;

    private String website;

    @Enumerated(EnumType.STRING)
    private AirlineStatus airlineStatus = AirlineStatus.ACTIVE;

    private String alliance;

    @Column(unique = true,nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private String country;

    private Long headquatersCityId;

    @Embedded
    private Support support;

    private Long updatedById;

    @CreatedDate
    @Column(updatable = false,nullable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}

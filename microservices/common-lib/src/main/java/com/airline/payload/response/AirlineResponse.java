package com.airline.payload.response;

import com.airline.embeddable.Support;
import com.airline.enums.AirlineStatus;
import com.airline.payload.dto.UserDTO;
import jakarta.persistence.Column;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AirlineResponse {

    private Long id;

    private String iataCode;

    private String icaoCode;

    private String name;

    private String alias;

    private String logoUrl;

    private String website;

    private AirlineStatus airlineStatus;

    private String alliance;

    private Instant createdAt;

    private Instant updatedAt;

    private CityResponse headquartersCity;

    private Long ownerId;
    private UserDTO owner;
    private Long updatedById;

    private Support support;
}

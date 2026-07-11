package com.airline.payload.request;

import com.airline.enums.AirlineStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AirlineRequest {

    @NotBlank(message = "iata code is mandatory")
    @Size(min=2,max = 2,message = "IATA code must be exactly 2 characters")
    private String iataCode;

    @NotBlank(message = "icao code is mandatory")
    @Size(min=3,max = 3,message = "ICAO code must be exactly 2 characters")
    private String icaoCode;

    @NotBlank(message = "Airline name is mandatory")
    private String name;

    private String alias;

    private String logoUrl;

    private String website;

    private AirlineStatus airlineStatus;

    private String alliance;

    @NotBlank
    private String country;

    private Long headquatersCityId;

    private String supportEmail;

    private String supportPhone;

    private String supportHours;
}

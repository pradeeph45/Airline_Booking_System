package com.airline.embeddable;

import jakarta.persistence.Embeddable;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class Support {

    private String email;
    private String phone;
    private String hours;
}

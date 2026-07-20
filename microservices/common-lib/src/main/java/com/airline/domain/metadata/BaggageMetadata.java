package com.airline.domain.metadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaggageMetadata {

    private Integer weight;

    // "KG" or "LB"
    private String unit;

    private Integer pieces;

    // "CHECKED", "CABIN", "SPORTS", "OVERSIZED", "SPECIAL"
    private String category;

    // e.g., "55x35x25 cm", "158 cm linear"
    private String dimensions;

    private String notes;

}

package com.airlines.anciallary_service.model;

import com.airline.domain.metadata.AncillaryMetadata;
import com.airline.enums.AncillaryType;
import com.airlines.anciallary_service.converter.AncillaryMetadataConverter;
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
public class Ancillary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AncillaryType type;

    @Column(length = 100)
    private String subType;

    @Column(length = 10)
    private String rfisc;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = AncillaryMetadataConverter.class)
    private AncillaryMetadata metadata;

    private Integer displayOrder;

    // Cross-service reference: Airline lives in airline-core-service
    @Column(name = "airline_id", nullable = false)
    private Long airlineId;

}

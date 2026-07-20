package com.airlines.anciallary_service.mapper;

import com.airline.payload.response.AncillaryResponse;
import com.airline.payload.response.InsuranceCoverageResponse;
import com.airlines.anciallary_service.model.Ancillary;

import java.util.List;

public class AncillaryMapper {

    public static AncillaryResponse toResponse(
            Ancillary entity
            , List<InsuranceCoverageResponse> coverageResponseList ) {   //
        if (entity == null) {
            return null;
        }

        return AncillaryResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .subType(entity.getSubType())
                .rfisc(entity.getRfisc())
                .name(entity.getName())
                .description(entity.getDescription())
                .metadata(entity.getMetadata())
                .coverages(coverageResponseList)
                .displayOrder(entity.getDisplayOrder())
                .airlineId(entity.getAirlineId())
                .build();
    }
}

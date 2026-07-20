package com.airlines.anciallary_service.converter;

import com.airline.domain.metadata.AncillaryMetadata;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Converter;
import tools.jackson.databind.ObjectMapper;

@Convert
@Converter
public class AncillaryMetadataConverter implements AttributeConverter<AncillaryMetadata,String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AncillaryMetadata metadata) {
        if (metadata == null) {
            return null;
        }
            return objectMapper.writeValueAsString(metadata);
        }

    @Override
    public AncillaryMetadata convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
            return objectMapper.readValue(dbData, AncillaryMetadata.class);
    }
}

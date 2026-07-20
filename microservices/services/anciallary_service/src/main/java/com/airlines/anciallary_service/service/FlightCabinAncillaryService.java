package com.airlines.anciallary_service.service;

import com.airline.enums.AncillaryType;
import com.airline.payload.request.FlightCabinAncillaryRequest;
import com.airline.payload.response.FlightCabinAncillaryResponse;

import java.util.List;

public interface FlightCabinAncillaryService {

    FlightCabinAncillaryResponse create(FlightCabinAncillaryRequest request) throws Exception;

    List<FlightCabinAncillaryResponse> bulkCreate(List<FlightCabinAncillaryRequest> requests) throws Exception;

    FlightCabinAncillaryResponse getById(Long id) throws Exception;

    List<FlightCabinAncillaryResponse> getAllByFlightAndCabinClass(
            Long flightId, Long cabinClassId);

    List<FlightCabinAncillaryResponse> getAllByIds(List<Long> ids);
    FlightCabinAncillaryResponse getByFlightIdAndCabinClassAndType(
            Long flightId, Long cabinClassId, AncillaryType type) throws Exception;

    List<FlightCabinAncillaryResponse> getAllByFlightIdAndCabinClassAndType(
            Long flightId, Long cabinClassId, AncillaryType type) throws Exception;

    FlightCabinAncillaryResponse update(Long id, FlightCabinAncillaryRequest request) throws Exception;

    void delete(Long id);

    Double calculateAncillaryPrice(List<Long> ancillaryIds);

}

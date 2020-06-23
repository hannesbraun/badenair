package de.hso.badenair.controller.dto.flightplan;

import lombok.Value;

import java.util.List;

@Value
public class PlaneScheduleDto {
    long id;
    String plane;
    String status;
    boolean hasConflict;
    List<FlightWithoutPriceDto> flights;
}

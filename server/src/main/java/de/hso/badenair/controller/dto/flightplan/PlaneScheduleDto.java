package de.hso.badenair.controller.dto.flightplan;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class PlaneScheduleDto {
    long id;
    String plane;
    String status;
    boolean hasConflict;
    List<FlightWithoutPriceDto> flights;
}

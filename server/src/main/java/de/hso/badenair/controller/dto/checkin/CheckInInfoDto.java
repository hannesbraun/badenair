package de.hso.badenair.controller.dto.checkin;

import de.hso.badenair.controller.dto.flight.FlightDto;
import de.hso.badenair.controller.dto.traveler.CheckInTravelerDto;
import lombok.Value;

import java.util.List;

@Value
public class CheckInInfoDto {
    List<CheckInTravelerDto> travelers;
    FlightDto flight;
}

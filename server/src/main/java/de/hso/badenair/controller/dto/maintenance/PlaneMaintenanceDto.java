package de.hso.badenair.controller.dto.maintenance;

import de.hso.badenair.domain.plane.PlaneState;
import lombok.Value;

@Value
public class PlaneMaintenanceDto {
	Long id;
	PlaneState state;
	Integer flightHours;
}

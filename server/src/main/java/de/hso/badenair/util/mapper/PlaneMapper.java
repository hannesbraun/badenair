package de.hso.badenair.util.mapper;

import de.hso.badenair.controller.dto.maintenance.PlaneMaintenanceDto;
import de.hso.badenair.domain.plane.Plane;

public abstract class PlaneMapper {

    public static PlaneMaintenanceDto mapToDto(Plane plane) {
        return new PlaneMaintenanceDto(plane.getId(), plane.getState(), plane.getTraveledDistance());
    }
}

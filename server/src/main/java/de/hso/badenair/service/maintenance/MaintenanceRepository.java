package de.hso.badenair.service.maintenance;

import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import org.springframework.data.repository.CrudRepository;

public interface MaintenanceRepository extends CrudRepository<Plane, Long> {
}

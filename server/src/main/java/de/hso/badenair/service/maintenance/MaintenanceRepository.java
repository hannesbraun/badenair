package de.hso.badenair.service.maintenance;

import de.hso.badenair.domain.plane.Plane;
import org.springframework.data.repository.CrudRepository;

public interface MaintenanceRepository extends CrudRepository<Plane, Long> {
}

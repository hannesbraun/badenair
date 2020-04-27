package de.hso.badenair.service.maintenance;


import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;

    public boolean updateMaintenanceState(Long id) {
        Optional<Plane> plane = maintenanceRepository.findById(id);

        if (!plane.isPresent()) {
            return false;
        }

        Plane realPlane = plane.get();
        realPlane.setState(PlaneState.WAITING);

        // Update state
        maintenanceRepository.save(plane.get());

        return true;
    }

    public List<Plane> getAllPlanes() {
        return (List<Plane>) maintenanceRepository.findAll();
    }

}

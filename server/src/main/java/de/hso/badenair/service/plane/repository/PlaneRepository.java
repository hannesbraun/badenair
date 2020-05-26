package de.hso.badenair.service.plane.repository;

import de.hso.badenair.domain.flight.Flight;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.plane.Plane;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Long> {
}

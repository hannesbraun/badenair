package de.hso.badenair.service.plane.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.plane.Plane;

@Repository
public interface PlaneRepository extends CrudRepository<Plane, Long> {
}

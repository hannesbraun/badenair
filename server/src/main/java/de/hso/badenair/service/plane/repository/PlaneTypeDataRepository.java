package de.hso.badenair.service.plane.repository;

import de.hso.badenair.domain.plane.PlaneTypeData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaneTypeDataRepository extends CrudRepository<PlaneTypeData, Long> {
}

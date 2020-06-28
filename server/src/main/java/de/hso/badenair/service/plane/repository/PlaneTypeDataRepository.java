package de.hso.badenair.service.plane.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;

@Repository
public interface PlaneTypeDataRepository
		extends
			CrudRepository<PlaneTypeData, Long> {
	List<PlaneTypeData> findAllByType(PlaneType type);
}

package de.hso.badenair.service.plane.repository;

import de.hso.badenair.domain.schedule.StandbySchedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StandbyScheduleRepository extends CrudRepository<StandbySchedule, Long> {
}

package de.hso.badenair.service.workinghours;

import de.hso.badenair.domain.schedule.WorkingHours;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingHoursRepository extends CrudRepository<WorkingHours, Long> {
    List<WorkingHours> findByEmployeeUserIdOrderByStartTimeDesc(String employeeUserId);
}

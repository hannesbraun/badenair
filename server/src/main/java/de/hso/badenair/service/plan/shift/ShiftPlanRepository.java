package de.hso.badenair.service.plan.shift;


import de.hso.badenair.domain.schedule.ShiftSchedule;
import de.hso.badenair.domain.schedule.Vacation;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShiftPlanRepository extends CrudRepository<ShiftSchedule, Long> {
    List<ShiftSchedule> findByEmployeeUserIDOrderByStartTimeAsc(String employeeUserId);
}

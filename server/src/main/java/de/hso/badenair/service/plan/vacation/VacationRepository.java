package de.hso.badenair.service.plan.vacation;

import de.hso.badenair.domain.schedule.Vacation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends CrudRepository<Vacation, Long> {
    List<Vacation> findByEmployeeUserIdOrderByStartTimeAsc(String employeeUserId);
}

package de.hso.badenair.service.workinghours;

import de.hso.badenair.domain.schedule.WorkingHours;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkingHoursService {

    private final WorkingHoursRepository workingHoursRepository;

    public WorkingHours triggerWorkingHours(String employeeUserId) {
        Optional<WorkingHours> latestWorkingHours = getLatestWorkingHours(employeeUserId);

        if(latestWorkingHours.isEmpty() || latestWorkingHours.get().getEndTime() != null) {
            WorkingHours workingHours = WorkingHours.builder()
                .employeeUserId(employeeUserId)
                .startTime(OffsetDateTime.now())
                .endTime(null)
                .build();

            workingHoursRepository.save(workingHours);

            return workingHours;
        } else {
            latestWorkingHours.get().setEndTime(OffsetDateTime.now());
            workingHoursRepository.save(latestWorkingHours.get());

            return latestWorkingHours.get();
        }
    }

    public Optional<WorkingHours> getLatestWorkingHours(String employeeUserId) {
        Optional<WorkingHours> workingHours = Optional.empty();

        List<WorkingHours> employeeWorkingHours = workingHoursRepository.findByEmployeeUserIdOrderByStartTimeDesc(employeeUserId);

        if(employeeWorkingHours.size() != 0) {
            workingHours = Optional.of(employeeWorkingHours.get(0));
        }

        return workingHours;
    }
}

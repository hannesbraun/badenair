package de.hso.badenair.service.plan.vacation;

import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.domain.schedule.Vacation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationService {

    private final VacationRepository vacationRepository;

    public List<Vacation> getVacation(String employeeUserId) {
        return vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc(employeeUserId);
    }

    public void requestVacation(String employeeUserId, RequestVacationDto requestVacationDto) {
        // TODO: Check if vacation can be approved

        final Vacation vacation = Vacation.builder()
            .employeeUserId(employeeUserId)
            .startTime(requestVacationDto.getStartDate())
            .endTime(requestVacationDto.getEndDate())
            .build();

        vacationRepository.save(vacation);
    }
}

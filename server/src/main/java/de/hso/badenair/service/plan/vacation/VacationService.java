package de.hso.badenair.service.plan.vacation;

import de.hso.badenair.controller.dto.plan.RequestVacationDto;
import de.hso.badenair.domain.schedule.Vacation;
import de.hso.badenair.service.keycloakapi.EmployeeRole;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.service.keycloakapi.dto.UserRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class VacationService {

    private final VacationRepository vacationRepository;
    private final KeycloakApiService keycloakApiService;
    private final Integer MAX_VACATION_DAYS = 36;
    private final Integer LAST_POSSIBLE_REQUEST_DAY_OF_MONTH = 10;

    public List<Vacation> getVacation(String employeeUserId) {
        return vacationRepository.findByEmployeeUserIdOrderByStartTimeAsc(employeeUserId);
    }

    public Integer getRemainingVacationDays(String employeeUserId) {
        return MAX_VACATION_DAYS - getUsedVacationDays(employeeUserId);
    }

    public Integer getUsedVacationDays(String employeeUserId) {
        return getVacation(employeeUserId).stream()
            .mapToInt(this::getDifferenceInDays)
            .sum();
    }

    @Transactional
    public void requestVacation(String employeeUserId, RequestVacationDto requestVacationDto) {
        final OffsetDateTime startDate = requestVacationDto.getStartDate().withHour(0).withOffsetSameLocal(ZoneOffset.of("+1"));
        final OffsetDateTime endDate = requestVacationDto.getEndDate().withHour(0).withOffsetSameLocal(ZoneOffset.of("+1"));
        final int differenceInDays = getDifferenceInDays(startDate, endDate);

        if (isRequestValid(startDate, endDate, differenceInDays)) {
            return;
        }

        if (!canPilotRequestVacation(employeeUserId, startDate, endDate)) {
            return;
        }

        final List<Vacation> vacations = getVacation(employeeUserId);
        if (vacations.stream().anyMatch(vacation -> isOverlapping(vacation, startDate, endDate))) {
            vacations.stream()
                .filter(vacation -> isOverlapping(vacation, startDate, endDate))
                .forEach(vacation -> {
                    if (vacation.getStartTime().isAfter(startDate)) {
                        vacation.setStartTime(startDate);
                    }

                    if (vacation.getEndTime().isBefore(endDate)) {
                        vacation.setEndTime(endDate);
                    }
                });
            return;
        }

        final int usedVacationDays = differenceInDays + getUsedVacationDays(employeeUserId);

        if (usedVacationDays > MAX_VACATION_DAYS) {
            return;
        }

        final Vacation vacation = Vacation.builder()
            .employeeUserId(employeeUserId)
            .startTime(startDate)
            .endTime(endDate)
            .build();

        vacationRepository.save(vacation);
    }

    private boolean canPilotRequestVacation(String employeeUserId, OffsetDateTime startDate, OffsetDateTime endDate) {
        if (keycloakApiService.isPilot(employeeUserId)) {
            final List<String> userIds = Stream.concat(keycloakApiService.getEmployeeUsersWithRole(EmployeeRole.DASH_PILOT).stream(),
                keycloakApiService.getEmployeeUsersWithRole(EmployeeRole.JET_PILOT).stream())
                .map(UserRepresentation::getId)
                .collect(Collectors.toList());

            return userIds.stream()
                .map(vacationRepository::findByEmployeeUserIdOrderByStartTimeAsc)
                .flatMap(Collection::stream)
                .noneMatch(vacation -> isOverlapping(vacation, startDate.minusHours(1), endDate.plusHours(1)));
        }

        return true;
    }

    private boolean isRequestValid(OffsetDateTime startDate, OffsetDateTime endDate, int differenceInDays) {
        return endDate.isBefore(startDate) || differenceInDays <= 0 || OffsetDateTime.now().getDayOfMonth() < LAST_POSSIBLE_REQUEST_DAY_OF_MONTH;
    }

    private int getDifferenceInDays(Vacation vacation) {
        return getDifferenceInDays(vacation.getStartTime(), vacation.getEndTime());
    }

    private boolean isOverlapping(Vacation vacation, OffsetDateTime startDate, OffsetDateTime endDate) {
        if (isOnSameDay(vacation.getStartTime(), startDate, endDate) || isOnSameDay(vacation.getEndTime(), startDate, endDate)) {
            return true;
        }

        return isWithinDateRange(vacation.getStartTime(), startDate, endDate) || isWithinDateRange(vacation.getEndTime(), startDate, endDate);
    }

    private boolean isWithinDateRange(OffsetDateTime date, OffsetDateTime startDate, OffsetDateTime endDate) {
        return date.isAfter(startDate) && date.isBefore(endDate);
    }

    private boolean isOnSameDay(OffsetDateTime date, OffsetDateTime startDate, OffsetDateTime endDate) {
        return ChronoUnit.DAYS.between(date, startDate) == 0 || ChronoUnit.DAYS.between(date, endDate) == 0;
    }

    private int getDifferenceInDays(OffsetDateTime startDate, OffsetDateTime endDate) {
        return (int) Math.ceil(ChronoUnit.HOURS.between(startDate, endDate) / 24.0);
    }
}

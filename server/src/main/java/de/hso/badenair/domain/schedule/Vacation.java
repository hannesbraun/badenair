package de.hso.badenair.domain.schedule;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VACATION")
public class Vacation extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_VACATION_ID")
	@SequenceGenerator(name = "GEN_VACATION_ID", sequenceName = "SEQ_VACATION")
	private Long id;

	@Column(name = "EMPLOYEE_USER_ID")
	private String employeeUserId;

	@Column(name = "START_TIME")
	private OffsetDateTime startTime;

	@Column(name = "END_TIME")
	private OffsetDateTime endTime;

	public OffsetDateTime getStartTime() {
		if (startTime == null) {
			return null;
		}
		return startTime.withOffsetSameInstant(ZoneOffset.of("+1"));
	}

	public OffsetDateTime getEndTime() {
		if (endTime == null) {
			return null;
		}
		return endTime.withOffsetSameInstant(ZoneOffset.of("+1"));
	}

    public boolean isOverlapping(OffsetDateTime startDate, OffsetDateTime endDate) {
        if (isOnSameDay(startTime, startDate, endDate) || isOnSameDay(endTime, startDate, endDate)) {
            return true;
        }

        return isWithinDateRange(startTime, startDate, endDate) || isWithinDateRange(endTime, startDate, endDate);
    }

    private boolean isWithinDateRange(OffsetDateTime date, OffsetDateTime startDate, OffsetDateTime endDate) {
        return date.isAfter(startDate) && date.isBefore(endDate);
    }

    private boolean isOnSameDay(OffsetDateTime date, OffsetDateTime startDate, OffsetDateTime endDate) {
        return ChronoUnit.DAYS.between(date, startDate) == 0 || ChronoUnit.DAYS.between(date, endDate) == 0;
    }
}

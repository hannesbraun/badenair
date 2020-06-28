package de.hso.badenair.domain.flight;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "SCHEDULED_FLIGHT")
public class ScheduledFlight extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_SCHEDULED_FLIGHT_ID")
	@SequenceGenerator(name = "GEN_SCHEDULED_FLIGHT", sequenceName = "SEQ_SCHEDULED_FLIGHT")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn
	private Airport startingAirport;

	@ManyToOne(optional = false)
	@JoinColumn
	private Airport destinationAirport;

	@Column(name = "START_TIME")
	private OffsetDateTime startTime;

	@Column(name = "DURATION_IN_HOURS")
	private Double durationInHours;

	@Column(name = "BASE_PRICE")
	private Double basePrice;

	public OffsetDateTime getStartTime() {
		if (startTime == null) {
			return null;
		}
		return startTime.withOffsetSameInstant(ZoneOffset.of("+1"));
	}

    public OffsetDateTime getLandingTime(LocalDate startDate) {
        int hours = (int)durationInHours.doubleValue();
        int minutes = (int)((durationInHours * 60) % 60);

        if(startDate == null) {
            return getStartTime().plusHours(hours).plusMinutes(minutes);
        } else {
            OffsetDateTime startDateTime = startDate.atTime(getStartTime().toOffsetTime());
            return startDateTime.plusHours(hours).plusMinutes(minutes);
        }
    }
}

package de.hso.badenair.domain.flight;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.hso.badenair.domain.base.BaseEntity;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.plane.Plane;
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
@Table(name = "FLIGHT")
public class Flight extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FLIGHT_ID")
	@SequenceGenerator(name = "GEN_FLIGHT_ID", sequenceName = "SEQ_FLIGHT")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn
	private ScheduledFlight scheduledFlight;

	@ManyToOne(optional = false)
	@JoinColumn
	private Plane plane;

	@Column(name = "START_DATE")
	private OffsetDateTime startDate;

	@Column(name = "ACTUAL_START_TIME")
	private OffsetDateTime actualStartTime;

	@Column(name = "ACTUAL_LANDING_TIME")
	private OffsetDateTime actualLandingTime;

	@Column(name = "DELAY")
	private double delay;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATE")
	private FlightState state;

	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Booking> bookings;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FlightCrewMember> flightCrewMembers;

	public OffsetDateTime getStartDate() {
		if (startDate == null) {
			return null;
		}
		return startDate.withOffsetSameInstant(ZoneOffset.of("+1"));
	}

	public OffsetDateTime getActualStartTime() {
		if (actualStartTime == null) {
			return null;
		}
		return actualStartTime.withOffsetSameInstant(ZoneOffset.of("+1"));
	}

	public OffsetDateTime getActualLandingTime() {
		if (actualLandingTime == null) {
			return null;
		}
		return actualLandingTime.withOffsetSameInstant(ZoneOffset.of("+1"));
	}
}

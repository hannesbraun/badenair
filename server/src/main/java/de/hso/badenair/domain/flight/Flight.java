package de.hso.badenair.domain.flight;

import de.hso.badenair.domain.base.BaseEntity;
import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.plane.Plane;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Set;

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
}

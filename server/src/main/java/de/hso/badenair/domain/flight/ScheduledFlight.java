package de.hso.badenair.domain.flight;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

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
}

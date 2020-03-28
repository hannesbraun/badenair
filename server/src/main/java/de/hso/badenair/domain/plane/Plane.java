package de.hso.badenair.domain.plane;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "PLANE")
public class Plane extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FLIGHT_ID")
    @SequenceGenerator(name = "GEN_FLIGHT_ID", sequenceName = "SEQ_FLIGHT")
    private Long id;

    @ManyToOne
    private PlaneTypeData typeData;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private PlaneState state;

    @Column(name = "TRAVELED_DISTANCE")
    private Integer traveledDistance;
}

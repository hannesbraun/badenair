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
@Table(name = "PLANE_TYPE_DATA")
public class PlaneTypeData extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_PLANE_TYPE_DATA_ID")
    @SequenceGenerator(name = "GEN_PLANE_TYPE_DATA", sequenceName = "SEQ_PLANE_TYPE_DATA")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE")
    private PlaneType type;

    @Column(name = "NUMBER_OF_PASSENGERS")
    private Integer numberOfPassengers;

    @Column(name = "RANGE")
    private Integer range;
}
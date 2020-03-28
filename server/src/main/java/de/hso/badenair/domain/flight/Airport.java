package de.hso.badenair.domain.flight;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AIRPORT")
public class Airport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_AIRPORT_ID")
    @SequenceGenerator(name = "GEN_AIRPORT", sequenceName = "SEQ_AIRPORT")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISTANCE")
    private Integer distance;
}

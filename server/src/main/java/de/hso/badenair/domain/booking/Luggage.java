package de.hso.badenair.domain.booking;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "LUGGAGE")
public class Luggage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_LUGGAGE_ID")
    @SequenceGenerator(name = "GEN_LUGGAGE", sequenceName = "SEQ_LUGGAGE")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE")
    private LuggageState state;

    @Column(name = "WEIGHT")
    private Integer weight;
}

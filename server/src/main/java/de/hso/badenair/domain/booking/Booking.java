package de.hso.badenair.domain.booking;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "BOOKING")
public class Booking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_BOOKING_ID")
    @SequenceGenerator(name = "GEN_BOOKING", sequenceName = "SEQ_BOOKING")
    private Long id;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Traveler> travelers;

    @Column(name = "PRICE")
    private Double price;
}

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
@Table(name = "TRAVELER")
public class Traveler extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_TRAVELER_ID")
    @SequenceGenerator(name = "GEN_TRAVELER", sequenceName = "SEQ_TRAVELER")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Booking booking;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "CHECKED_IN")
    private boolean checkedIn;

    @Column(name = "SEAT_ROW")
    private int seatRow;

    @Column(name = "SEAT_COLUMN")
    private int seatColumn;

    @OneToMany(mappedBy = "traveler", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Luggage> luggage;
}

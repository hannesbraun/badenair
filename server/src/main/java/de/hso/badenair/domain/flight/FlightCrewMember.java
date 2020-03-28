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
@Table(name = "FLIGHT_CREW_MEMBER")
public class FlightCrewMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FLIGHT_CREW_MEMBER_ID")
    @SequenceGenerator(name = "GEN_FLIGHT_CREW_MEMBER", sequenceName = "SEQ_FLIGHT_CREW_MEMBER")
    private Long id;

    @ManyToOne
    private Flight flight;

    @Column(name = "EMPLOYEE_USER_ID")
    private String employeeUserId;
}

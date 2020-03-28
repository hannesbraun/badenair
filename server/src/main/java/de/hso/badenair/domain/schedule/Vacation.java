package de.hso.badenair.domain.schedule;

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
@Table(name = "VACATION")
public class Vacation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_VACATION_ID")
    @SequenceGenerator(name = "GEN_VACATION_ID", sequenceName = "SEQ_VACATION")
    private Long id;

    @Column(name = "EMPLOYEE_USER_ID")
    private String employeeUserId;

    @Column(name = "START_TIME")
    private OffsetDateTime startTime;

    @Column(name = "END_TIME")
    private OffsetDateTime endTime;
}

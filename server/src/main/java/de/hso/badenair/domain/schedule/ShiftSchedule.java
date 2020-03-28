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
@Table(name = "SHIFT_SCHEDULE")
public class ShiftSchedule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_SHIFT_SCHEDULE_ID")
    @SequenceGenerator(name = "GEN_SHIFT_SCHEDULE_ID", sequenceName = "SEQ_SHIFT_SCHEDULE")
    private Long id;

    @Column(name = "EMPLOYEE_USER_ID")
    private String employeeUserId;

    @Column(name = "START_TIME")
    private OffsetDateTime startTime;

    @Column(name = "END_TIME")
    private OffsetDateTime endTime;
}

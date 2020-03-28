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
@Table(name = "WORKING_HOURS")
public class WorkingHours extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_WORKING_HOURS_ID")
    @SequenceGenerator(name = "GEN_WORKING_HOURS_ID", sequenceName = "SEQ_WORKING_HOURS")
    private Long id;

    @Column(name = "EMPLOYEE_USER_ID")
    private String employeeUserId;

    @Column(name = "START_TIME")
    private OffsetDateTime startTime;

    @Column(name = "END_TIME")
    private OffsetDateTime endTime;
}

package de.hso.badenair.domain.flight;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.hso.badenair.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Column(name = "TIMEZONE")
	private String timezone;
}

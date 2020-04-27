package de.hso.badenair.domain.plane;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import de.hso.badenair.domain.base.BaseEntity;
import de.hso.badenair.domain.flight.Flight;
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
@Table(name = "PLANE")
public class Plane extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_FLIGHT_ID")
	@SequenceGenerator(name = "GEN_FLIGHT_ID", sequenceName = "SEQ_FLIGHT")
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn
	private PlaneTypeData typeData;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATE")
	private PlaneState state;

	@Column(name = "TRAVELED_DISTANCE")
	private Integer traveledDistance;

	@OneToMany(mappedBy = "plane", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Flight> flight;
}

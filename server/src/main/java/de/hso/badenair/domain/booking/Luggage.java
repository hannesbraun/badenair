package de.hso.badenair.domain.booking;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@Table(name = "LUGGAGE")
public class Luggage extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_LUGGAGE_ID")
	@SequenceGenerator(name = "GEN_LUGGAGE", sequenceName = "SEQ_LUGGAGE")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Traveler traveler;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATE")
	private LuggageState state;

	@Column(name = "WEIGHT")
	private Integer weight;
}

package de.hso.badenair.domain.booking;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

	@Column(name = "SEAT_NUMBER")
	private String seatNumber;

	@OneToMany(mappedBy = "traveler", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Luggage> luggage;
}

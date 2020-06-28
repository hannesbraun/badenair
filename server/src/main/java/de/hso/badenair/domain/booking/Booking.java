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
@Table(name = "BOOKING")
public class Booking extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEN_BOOKING_ID")
	@SequenceGenerator(name = "GEN_BOOKING", sequenceName = "SEQ_BOOKING")
	private Long id;

	@Column(name = "CUSTOMER_USER_ID")
	private String customerUserId;

	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Traveler> travelers;

	@ManyToOne(fetch = FetchType.LAZY)
	private Flight flight;

	@Column(name = "PRICE")
	private Double price;
}

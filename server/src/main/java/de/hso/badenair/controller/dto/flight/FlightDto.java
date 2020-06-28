package de.hso.badenair.controller.dto.flight;

import java.time.OffsetDateTime;

import lombok.Value;

@Value
public class FlightDto {
	long id;
	String start;
	String destination;
	OffsetDateTime startTime;
	OffsetDateTime arrivalTime;
	String startTimezone;
	String destinationTimezone;
	double price;
}

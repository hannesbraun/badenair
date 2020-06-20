package de.hso.badenair.controller.dto.flightplan;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.util.time.DateFusioner;
import lombok.Data;
import lombok.Value;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.TimeZone;

@Value
public class FlightWithoutPriceDto {
    long id;
    String start;
    String destination;
    double delay;
    OffsetDateTime startTime;
    OffsetDateTime arrivalTime;
    OffsetDateTime realStartTime;
    OffsetDateTime realLandingTime;

    public FlightWithoutPriceDto(long id, String start, String destination, double delay, OffsetDateTime startTime, OffsetDateTime arrivalTime, OffsetDateTime realStartTime, OffsetDateTime realLandingTime){
        this.id = id;
        this.start = start;
        this.destination = destination;
        this.delay = delay;
        this.startTime = startTime;
        this.arrivalTime = arrivalTime;
        this.realStartTime = realStartTime;
        this.realLandingTime = realLandingTime;
    }

    public FlightWithoutPriceDto(Flight flight){
        this.id = flight.getId();
        this.start = flight.getScheduledFlight().getStartingAirport().getName();
        this.destination = flight.getScheduledFlight().getDestinationAirport().getName();
        this.delay = flight.getDelay();
        this.startTime = DateFusioner.fusionStartDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), null);
        this.arrivalTime = DateFusioner.fusionArrivalDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), flight.getScheduledFlight().getDurationInHours(), null).withOffsetSameLocal(ZoneOffset.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1"));
        this.realStartTime = flight.getActualStartTime();
        this.realLandingTime = flight.getActualLandingTime();
    }
}

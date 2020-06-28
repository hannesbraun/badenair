package de.hso.badenair.service.flightplan;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.flight.repository.FlightRepository;
import org.springframework.stereotype.Service;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightPlanService {

	private final FlightplanRepository flightplanRepository;
	private final PlaneRepository planeRepository;
	private final FlightRepository flightRepository;
	private ArrayList<ConflictDto> conflicts = new ArrayList<>();
	private ArrayList<Long> conflictBlackList = new ArrayList<>();

	public ArrayList<PlaneScheduleDto> getPlaneSchedules() {
		OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.of("+1"));
		now = now.truncatedTo(ChronoUnit.DAYS);
		now = now.plusHours(6);
		OffsetDateTime oneDayLater = now.plusHours(21);

		List<Plane> planes = (List<Plane>) planeRepository.findAll();
		ArrayList<PlaneScheduleDto> planeSchedules = new ArrayList<>(0);

		OffsetDateTime finalNow = now;

		//create Dtos for each plane
		planes.forEach(plane -> {
			final List<FlightWithoutPriceDto> flights = flightplanRepository
					.findByStartDateBetweenAndPlane_IdEquals(finalNow.withHour(0).withMinute(0).withSecond(0),
							oneDayLater.withHour(23).withMinute(59).withSecond(59), plane.getId())
					.stream().filter(flight -> {
						OffsetDateTime fusionedStartDate = DateFusioner.fusionStartDate(flight.getStartDate(),
								flight.getScheduledFlight().getStartTime(), null);
						return fusionedStartDate.isAfter(finalNow) && fusionedStartDate.isBefore(oneDayLater);
					})

					.map(flight -> {
						final ScheduledFlight scheduledFlight = flight.getScheduledFlight();

						// Adjusting timezones in these dtos again because the frontend wants another
						// timezone :(
						return new FlightWithoutPriceDto(flight.getId(), scheduledFlight.getStartingAirport().getName(),
								scheduledFlight.getDestinationAirport().getName(),
								flight.getDelay(),
								DateFusioner
										.fusionStartDate(flight.getStartDate(), scheduledFlight.getStartTime(), null)
										.withOffsetSameLocal(ZoneOffset
												.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
								DateFusioner
										.fusionArrivalDate(flight.getStartDate(), scheduledFlight.getStartTime(),
												scheduledFlight.getDurationInHours(), null)
										.withOffsetSameLocal(ZoneOffset
												.of(TimeZone.getDefault().inDaylightTime(new Date()) ? "+2" : "+1")),
								flight.getActualStartTime(), flight.getActualLandingTime());
					}).collect(Collectors.toList());

			//create the Dto for the next plane
			planeSchedules.add(new PlaneScheduleDto(plane.getId(), plane.getTypeData().getType().getName(),
					plane.getState().getName(), false, flights));
		});

		//update conflict list
		conflicts.clear();
		ConflictFinder.findConflicts(planeSchedules, conflicts);

        //remove blacklisted conflicts from conflict list
        for (int i = 0; i < conflicts.size(); i++){
            for (int j = 0; j < conflictBlackList.size(); j++){
                if (conflicts.get(i).getFlightID() == conflictBlackList.get(j)){
                    conflicts.remove(i);
                    break;
                }
            }
        }

		//update has conflict boolean in transmitted flightplan. needs to be done again, cause ConflictFinder needs the Dto representation
		planeSchedules.forEach(planeScheduleDto -> {
		    planeScheduleDto.getFlights().forEach( flightWithoutPriceDto -> {
		        conflicts.forEach( conflictDto -> {
		            if (conflictDto.getFlightID() == flightWithoutPriceDto.getId())
                        planeScheduleDto.setHasConflict(true);
                    }
                );
            });
        });

		return planeSchedules;
	}

	public ArrayList<ConflictDto> getConflicts() {
		return conflicts;
	}

	public void resolvePlaneConflict(long flightID, long reservePlaneID){
        Flight flight = flightRepository.findById(flightID).get();

        //need the whole list of flights for that plane and day to create the FlightGroup
        OffsetDateTime startOfDay = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.of("+1"));
        startOfDay = startOfDay.truncatedTo(ChronoUnit.DAYS);
        startOfDay = startOfDay.plusHours(6);
        OffsetDateTime oneDayLater = startOfDay.plusHours(21);

        OffsetDateTime finalNow = startOfDay;
        final ArrayList<Flight> flights = new ArrayList<>();
        flights.addAll(flightplanRepository
            .findByStartDateBetweenAndPlane_IdEquals(startOfDay.withHour(0).withMinute(0).withSecond(0),
                oneDayLater.withHour(23).withMinute(59).withSecond(59), flight.getPlane().getId())
            .stream().filter(f -> {
                OffsetDateTime fusionedStartDate = DateFusioner.fusionStartDate(f.getStartDate(),
                    f.getScheduledFlight().getStartTime(), null);
                return fusionedStartDate.isAfter(finalNow) && fusionedStartDate.isBefore(oneDayLater);
            }).collect(Collectors.toList()));

        FlightGroup flightGroup = FlightGroup.getFlightGroupForFlight(flight, flights);

        Flight outgoingFlight = flightRepository.findById(flightGroup.getOutgoingFlight().getId()).get();
        Flight incomingFlight = flightRepository.findById(flightGroup.getIncomingFlight().getId()).get();

        outgoingFlight.setPlane(planeRepository.findById(reservePlaneID).get());
        incomingFlight.setPlane(planeRepository.findById(reservePlaneID).get());
        outgoingFlight.setDelay(0);
        incomingFlight.setDelay(0);

        OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.of("+1"));

        if (outgoingFlight.getActualStartTime() == null || now.isBefore(outgoingFlight.getActualStartTime()))
            flightRepository.save(outgoingFlight);
        if (incomingFlight.getActualStartTime() == null || now.isBefore(incomingFlight.getActualStartTime()))
            flightRepository.save(incomingFlight);
    }

    public void cancelFlight(long flightID){
        Flight flight = flightRepository.findById(flightID).get();

        //need the whole list of flights for that plane and day to create the FlightGroup
        OffsetDateTime startOfDay = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.of("+1"));
        startOfDay = startOfDay.truncatedTo(ChronoUnit.DAYS);
        startOfDay = startOfDay.plusHours(6);
        OffsetDateTime oneDayLater = startOfDay.plusHours(21);

        OffsetDateTime finalNow = startOfDay;
        final ArrayList<Flight> flights = new ArrayList<>();
        flights.addAll(flightplanRepository
            .findByStartDateBetweenAndPlane_IdEquals(startOfDay.withHour(0).withMinute(0).withSecond(0),
                oneDayLater.withHour(23).withMinute(59).withSecond(59), flight.getPlane().getId())
            .stream().filter(f -> {
                OffsetDateTime fusionedStartDate = DateFusioner.fusionStartDate(f.getStartDate(),
                    f.getScheduledFlight().getStartTime(), null);
                return fusionedStartDate.isAfter(finalNow) && fusionedStartDate.isBefore(oneDayLater);
            }).collect(Collectors.toList()));

        FlightGroup flightGroup = FlightGroup.getFlightGroupForFlight(flight, flights);

        OffsetDateTime now = OffsetDateTime.now().withOffsetSameInstant(ZoneOffset.of("+1"));

        if (flightGroup.getOutgoingFlight().getRealStartTime() == null || now.isBefore(flightGroup.getOutgoingFlight().getRealStartTime()))
            flightRepository.deleteById(flightGroup.getOutgoingFlight().getId());
        if (flightGroup.getIncomingFlight().getRealStartTime() == null || now.isBefore(flightGroup.getIncomingFlight().getRealStartTime()))
            flightRepository.deleteById(flightGroup.getIncomingFlight().getId());
    }

    public void ignoreDelay(long flightID){
        //add the flight id to the blacklist to ignore this conflict

	    conflictBlackList.add(flightID);
    }
}

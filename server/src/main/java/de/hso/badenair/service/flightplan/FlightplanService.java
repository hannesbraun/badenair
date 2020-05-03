package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.account.AccountDataDto;
import de.hso.badenair.controller.dto.account.UpdateAccountDataDto;
import de.hso.badenair.controller.dto.flightplan.FlightDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.booking.account.AccountData;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.flight.ScheduledFlight;
import de.hso.badenair.util.mapper.AccountDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.List;

import de.hso.badenair.domain.plane.Plane;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class FlightplanService {

    private final FlightplanRepository flightplanRepository;

    public ArrayList<PlaneScheduleDto> getPlaneSchedules() {
        OffsetDateTime now = OffsetDateTime.now();
        now = now.minusHours(1);
        OffsetDateTime oneDayLater = now.plusHours(12);

        List<Plane> data = flightplanRepository.findByActualStartTimeBetween(now, oneDayLater);
        ArrayList<PlaneScheduleDto> ret = new ArrayList<>(0);

        for (int i = 0; i < data.size(); i++){
            ArrayList<FlightDto> flights = new ArrayList<>(0);



            data.get(i).getFlight().forEach(flight -> {
                flights.add(new FlightDto(flight.getId(),
                    flight.getScheduledFlight().getStartingAirport().getName(),
                    flight.getScheduledFlight().getDestinationAirport().getName(),
                    flight.getActualStartTime(),
                    flight.getActualLandingTime()));
            });

            ret.add(new PlaneScheduleDto(data.get(i).getId(),
                "",
                "",
                false,
                flights));
        }

        return ret;
    }
}

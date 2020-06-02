package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class ConflictFinder {
    private static FlightplanRepository flightplanRepository;
    private static PlaneRepository planeRepository;

    public static void findConflicts(ArrayList<PlaneScheduleDto> planes, ArrayList<ConflictDto> conflicts){
        boolean planeDamaged = false;
        boolean unavailablePlaneFixable = false;
        AtomicBoolean flightTooLate = new AtomicBoolean(false);

        for (int i = 0; i < planes.size(); i++){
            PlaneScheduleDto plane = planes.get(i);

            if (plane.getStatus().equals("In Wartung"))
            {
                planeDamaged = true;

                //durch alle planes gehen. kein start oder landung darf sich mit flug überschneiden
                if (plane.getFlights().size() == 0)
                    unavailablePlaneFixable = true;
            }
            else
                planeDamaged = false;

            boolean finalPlaneDamaged = planeDamaged;

            boolean finalUnavailablePlaneFixable = unavailablePlaneFixable;
            plane.getFlights().forEach((FlightWithoutPriceDto flight) -> {

                flightTooLate.set(false);
                if (flight.getRealStartTime() != null && flight.getRealStartTime().isAfter(flight.getStartTime()))
                {
                    //flight too late
                    flightTooLate.set(true);

                    if (finalPlaneDamaged)
                    {
                        //search for unused plane
                    }
                }

                if (flightTooLate.get() || finalPlaneDamaged)
                    conflicts.add(new ConflictDto(flight.getId(), flightTooLate.get(), false, finalPlaneDamaged, finalUnavailablePlaneFixable));
                /*
                not implemented because of missing data base relation

                switch (plane.getTypeData().getType()){
                    case B737_400:

                        break;
                    case Dash_8_200:

                        break;
                    case Dash_8_400:

                        break;
                    default:
                        //unknown plane type
                }
                */
            });
        }

    }
}

package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.controller.dto.flightplan.PlaneDto;
import de.hso.badenair.controller.dto.flightplan.PlaneScheduleDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
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
                for (int j = 0; j < planes.size(); j++){
                    if (i == j)
                        continue;
                    if (planes.get(j).getFlights().size() == 0)
                        unavailablePlaneFixable = true;
                }
            }
            else
                planeDamaged = false;

            plane.getFlights().sort((FlightWithoutPriceDto flight1, FlightWithoutPriceDto flight2) -> flight1.getStartTime().compareTo(flight2.getStartTime()));

            boolean finalPlaneDamaged = planeDamaged;
            boolean finalUnavailablePlaneFixable = unavailablePlaneFixable;

            for (int j = 0; j < plane.getFlights().size(); j++){
                ArrayList<PlaneDto> backupPlanes = new ArrayList<>();

                FlightWithoutPriceDto flight = plane.getFlights().get(j);

                flightTooLate.set(false);
                if (flight.getDelay() > 0)
                {
                    //flight too late
                    flightTooLate.set(true);

                    FlightGroup lateFlightGroup;

                    if (flight.getStart().equals("Karlsruhe/Baden-Baden"))
                    {
                        lateFlightGroup = new FlightGroup(flight, plane.getFlights().get(j + 1));
                    }
                    else if (flight.getDestination().equals("Karlsruhe/Baden-Baden")){
                        lateFlightGroup = new FlightGroup(plane.getFlights().get(j - 1), flight);
                    }
                    else{
                        System.out.println(flight.getStart() + "   " + flight.getDestination());
                        throw new InvalidParameterException();
                    }

                    ArrayList<FlightGroup> otherPlanesFlightGroups;

                    for (int k = 0; k < planes.size(); k++){
                        if (i == k)
                            continue;

                        otherPlanesFlightGroups = FlightGroup.getFlightGroupsForPlane((ArrayList<FlightWithoutPriceDto>) planes.get(k).getFlights());

                        if (lateFlightGroup.checkIfPlaneIsAvailable(otherPlanesFlightGroups)){
                            backupPlanes.add(new PlaneDto(planes.get(k).getId(), planes.get(k).getPlane()));
                        }

                    }


                }

                if (backupPlanes.size() > 0)
                    finalUnavailablePlaneFixable = true;

                if (flightTooLate.get() || finalPlaneDamaged)
                    conflicts.add(new ConflictDto(flight.getId(), flightTooLate.get(), false, finalPlaneDamaged, finalUnavailablePlaneFixable, backupPlanes));
            }



        }

    }
}

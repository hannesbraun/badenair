package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.flightplan.ConflictDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;


public class ConflictFinder {
    private static FlightplanRepository flightplanRepository;

    public static void findConflicts(List<Plane> planes, ArrayList<ConflictDto> conflicts){
        boolean planeDamaged = false;

        for (int i = 0; i < planes.size(); i++)
        {
            Plane plane = planes.get(i);

            if (plane.getState() == PlaneState.IN_MAINTENANCE)
                planeDamaged = true;
            else
                planeDamaged = false;

            boolean finalPlaneDamaged = planeDamaged;
            plane.getFlight().forEach((Flight flight) -> {

                if (flight.getActualStartTime().isAfter(flight.getStartDate()))
                {
                    //flight too late

                    if (finalPlaneDamaged)
                    {
                        //search for unused plane
                        List<Plane> temp = flightplanRepository.findByActualStartTimeBetween(flight.getStartDate(), flight.getActualLandingTime());
                        temp.forEach((Plane p) -> {
                            if (p.getFlight().size() == 0)
                            {
                                //plane available
                            }
                    });
                    }
                    conflicts.add(new ConflictDto(flight.getId(), true, false, finalPlaneDamaged, false));
                }
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

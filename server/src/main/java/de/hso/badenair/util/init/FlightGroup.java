package de.hso.badenair.util.init;

import de.hso.badenair.domain.flight.Flight;
import lombok.Value;

@Value
public class FlightGroup {
    Flight flight;
    Flight returnFlight;
}

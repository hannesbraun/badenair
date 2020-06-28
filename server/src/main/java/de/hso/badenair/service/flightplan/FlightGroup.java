package de.hso.badenair.service.flightplan;

import de.hso.badenair.controller.dto.flightplan.FlightWithoutPriceDto;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.security.InvalidParameterException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FlightGroup {
    private FlightWithoutPriceDto outgoingFlight;
    private FlightWithoutPriceDto incomingFlight;

    public FlightGroup(FlightWithoutPriceDto outgoing, FlightWithoutPriceDto incoming){
        this.outgoingFlight = outgoing;
        this.incomingFlight = incoming;
    }

    public static ArrayList<FlightGroup> getFlightGroupsForPlane(ArrayList<FlightWithoutPriceDto> flights){
        ArrayList<FlightGroup> ret = new ArrayList<FlightGroup>();

        flights.sort((FlightWithoutPriceDto flight1, FlightWithoutPriceDto flight2) -> flight1.getStartTime().compareTo(flight2.getStartTime()));

        for (int i = 0; i < flights.size(); i+=2){
            if (flights.get(i).getStart().equals("Karlsruhe/Baden-Baden"))
            {
                if (flights.get(i+1).getDestination().equals("Karlsruhe/Baden-Baden")){
                    ret.add(new FlightGroup(flights.get(i), flights.get(i+1)));
                }
                else{
                    System.out.println(flights.get(i).getDestination());
                    throw new InvalidParameterException();
                }
            }
            else{
                System.out.println(flights.get(i).getStart());
                throw new InvalidParameterException();
            }
        }

        return ret;
    }

    public boolean checkIfPlaneIsAvailable(ArrayList<FlightGroup> otherPlaneFlightGroups){
        if (otherPlaneFlightGroups.size() == 0)
            return true;
        else if (otherPlaneFlightGroups.size() == 1){
            return this.checkForConflict(otherPlaneFlightGroups.get(0));
        }
        else {
            boolean fixable = true;

            for (int i = 0; i < otherPlaneFlightGroups.size(); i++){
                if (this.checkForConflict(otherPlaneFlightGroups.get(i)) == false)
                    fixable = false;
            }

            return fixable;
        }
    }

    private boolean checkForConflict(FlightGroup other){
        if (this.outgoingFlight.getStartTime().isAfter(other.incomingFlight.getArrivalTime()))
            return true;
        else if (other.outgoingFlight.getStartTime().isAfter(this.incomingFlight.getArrivalTime()))
            return true;
        else
            return false;
    }

    public static FlightGroup getFlightGroupForFlight(Flight flight, ArrayList<Flight> flights){
        flights.sort((Flight flight1, Flight flight2) -> flight1.getScheduledFlight().getStartTime().compareTo(flight2.getScheduledFlight().getStartTime()));

        if (flight.getScheduledFlight().getStartingAirport().getName().equals("Karlsruhe/Baden-Baden")){
            return new FlightGroup(new FlightWithoutPriceDto(flight), new FlightWithoutPriceDto(flights.get(flights.indexOf(flight)+1)));
        }
        else if (flight.getScheduledFlight().getDestinationAirport().getName().equals("Karlsruhe/Baden-Baden")){
            return new FlightGroup(new FlightWithoutPriceDto(flights.get(flights.indexOf(flight) - 1)), new FlightWithoutPriceDto(flight));
        }
        else{
            System.out.println(flight.getScheduledFlight().getStartingAirport().getName());
            System.out.println(flight.getScheduledFlight().getDestinationAirport().getName());
            throw new InvalidParameterException();
        }
    }

    public FlightWithoutPriceDto getOutgoingFlight(){
        return this.outgoingFlight;
    }

    public FlightWithoutPriceDto getIncomingFlight(){
        return this.incomingFlight;
    }
}

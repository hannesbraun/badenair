package de.hso.badenair.service.email;

import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.util.time.DateFusioner;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MailNotificationThread extends Thread {
    private final Set<Long> flightIdSet;
    private final FlightRepository flightRepository;
    private final MailNotificationService mailservice;
    private final KeycloakApiService keycloack;
    public MailNotificationThread(Set<Long> flightIdSet, FlightRepository r, MailNotificationService s, KeycloakApiService k){
        this.flightIdSet = flightIdSet;
        this.flightRepository = r;
        this.mailservice = s;
        this.keycloack = k;
    }

    
    public void run(){
        //get all delayed Flights
        List<Flight> flights= (List<Flight>) flightRepository.findAllById(this.flightIdSet);

        for(int i = 0; i< flights.size(); i++){
            Flight flight = flights.get(i);

            //get all bookings for this flight
             Iterator<Booking> iterator =  flight.getBookings().iterator();
            while (iterator.hasNext()){
                Booking booking = iterator.next();
                try{
                    //send email
                    var user = this.keycloack.getUserById(booking.getCustomerUserId()).get();
                    mailservice.sendFlightScheduleChangeNotification(user.getEmail(),
                        user.getFirstName()+ " "+ user.getLastName(), flight.getScheduledFlight(),
                        //not really Arrivaltime but new Starttime
                        DateFusioner.fusionArrivalDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), flight.getDelay(),null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

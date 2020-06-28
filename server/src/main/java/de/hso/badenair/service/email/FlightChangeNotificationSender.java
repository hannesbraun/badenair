package de.hso.badenair.service.email;

import de.hso.badenair.domain.booking.Booking;
import de.hso.badenair.domain.flight.Flight;
import de.hso.badenair.service.flight.repository.FlightRepository;
import de.hso.badenair.service.keycloakapi.KeycloakApiService;
import de.hso.badenair.util.time.DateFusioner;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class FlightChangeNotificationSender {

    private final FlightRepository flightRepository;
    private final MailNotificationService mailservice;
    private final KeycloakApiService keycloack;

    @Transactional
    public void send(Set<Long> flightIdSet) {
        List<Flight> flights = (List<Flight>) flightRepository.findAllById(flightIdSet);

        for (Flight flight : flights) {
            //get all bookings for this flight
            for (Booking booking : flight.getBookings()) {
                try {
                    //send email
                    var user = this.keycloack.getUserById(booking.getCustomerUserId()).get();
                    mailservice.sendFlightScheduleChangeNotification(user.getEmail(),
                        user.getFirstName() + " " + user.getLastName(), flight.getScheduledFlight(),
                        //not really Arrivaltime but new Starttime
                        DateFusioner.fusionArrivalDate(flight.getStartDate(), flight.getScheduledFlight().getStartTime(), flight.getDelay(), null));
                } catch (Exception e) {
                    // e.printStackTrace();
                }
            }

        }
    }
    
    @Transactional
	public void sendCancel(List<String> customers, String startingAirport, String destinationAirport) {
		// get all bookings for this flight
		for (String customer : customers) {
			try {
				// send email
				var user = this.keycloack.getUserById(customer).get();
				mailservice.sendCancelledFlightNotification(user.getEmail(),
						user.getFirstName() + " " + user.getLastName(), startingAirport, destinationAirport);
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

	}
}

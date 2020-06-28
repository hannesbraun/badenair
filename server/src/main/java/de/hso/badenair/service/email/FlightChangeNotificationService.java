package de.hso.badenair.service.email;

import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlightChangeNotificationService {

	private final FlightChangeNotificationSender sender;

	@Async
	public void sendNotifications(Set<Long> flightIdSet) {
		sender.send(flightIdSet);
	}

	@Async
	public void sendCancelNotifications(List<String> customers, String startingAirport, String destinationAirport) {
		sender.sendCancel(customers, startingAirport, destinationAirport);
	}
}

package de.hso.badenair.service.email;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class FlightChangeNotificationService {

    private final FlightChangeNotificationSender sender;

    @Async
    public void sendNotifications(Set<Long> flightIdSet) {
        sender.send(flightIdSet);
    }
}

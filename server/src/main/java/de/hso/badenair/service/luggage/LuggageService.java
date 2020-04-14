package de.hso.badenair.service.luggage;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LuggageService {

	private final LuggageRepository luggageRepository;

	public boolean updateLuggageState(Long id, LuggageState state) {
		Optional<Luggage> luggage = luggageRepository.findById(id);

		if (!luggage.isPresent()) {
			return false;
		}

		// Update state
		luggage.get().setState(state);
		luggageRepository.save(luggage.get());

		return true;
	}
}

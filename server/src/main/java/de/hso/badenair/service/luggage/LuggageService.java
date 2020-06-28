package de.hso.badenair.service.luggage;

import java.util.Optional;

import org.springframework.stereotype.Service;

import de.hso.badenair.domain.booking.Luggage;
import de.hso.badenair.domain.booking.LuggageState;
import lombok.RequiredArgsConstructor;

/**
 * Updates the luggage state.
 */
@Service
@RequiredArgsConstructor
public class LuggageService {

	private final LuggageRepository luggageRepository;

	/**
	 * Updates the luggage state for a given piece of luggage.
	 * @param id the id of the piece of luggage to update
	 * @param state the new state to update to
	 * @return true in case of success, false otherwise
	 */
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

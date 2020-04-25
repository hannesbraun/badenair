package de.hso.badenair.util.init;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import de.hso.badenair.domain.flight.Airport;
import de.hso.badenair.domain.plane.Plane;
import de.hso.badenair.domain.plane.PlaneState;
import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.AirportRepository;
import de.hso.badenair.service.plane.repository.PlaneRepository;
import de.hso.badenair.service.plane.repository.PlaneTypeDataRepository;
import de.hso.badenair.util.csv.CsvHelper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StaticDataInitializer {

	private final PlaneTypeDataRepository planeTypeDataRepository;

	private final PlaneRepository planeRepository;

	private final AirportRepository airportRepository;

	@PostConstruct
	private void init() {
		initPlaneTypeData();
		initPlanes();

		initAirports();

		// TODO: Initialize all static data
	}

	private void initPlaneTypeData() {
		final PlaneTypeData planeTypeDataDash_8_400 = PlaneTypeData.builder()
				.type(PlaneType.Dash_8_400).numberOfPassengers(68)
				.flightRange(1000).build();

		final PlaneTypeData planeTypeDataDash_8_200 = PlaneTypeData.builder()
				.type(PlaneType.Dash_8_200).numberOfPassengers(38)
				.flightRange(1000).build();

		final PlaneTypeData planeTypeDataB737_400 = PlaneTypeData.builder()
				.type(PlaneType.B737_400).numberOfPassengers(188)
				.flightRange(2500).build();

		planeTypeDataRepository.saveAll(List.of(planeTypeDataDash_8_400,
				planeTypeDataDash_8_200, planeTypeDataB737_400));
	}

	private void initPlanes() {
		// Get types
		PlaneTypeData planeTypeDataDash_8_200 = planeTypeDataRepository
				.findAllByType(PlaneType.Dash_8_200).get(0);
		PlaneTypeData planeTypeDataDash_8_400 = planeTypeDataRepository
				.findAllByType(PlaneType.Dash_8_400).get(0);
		PlaneTypeData planeTypeDataB737_400 = planeTypeDataRepository
				.findAllByType(PlaneType.B737_400).get(0);

		List<Plane> planes = new ArrayList<Plane>();

		// Number of available plane
		final int dash_8_200_count = 3;
		final int dash_8_400_count = 5;
		final int b737_400_count = 2;

		// Create planes
		for (int i = 0; i < dash_8_200_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataDash_8_200)
					.state(PlaneState.WAITING).traveledDistance(0).build());
		}

		for (int i = 0; i < dash_8_400_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataDash_8_400)
					.state(PlaneState.WAITING).traveledDistance(0).build());
		}

		for (int i = 0; i < b737_400_count; i++) {
			planes.add(Plane.builder().typeData(planeTypeDataB737_400)
					.state(PlaneState.WAITING).traveledDistance(0).build());
		}

		planeRepository.saveAll(planes);
	}

	private void initAirports() {
		List<Airport> airports = new ArrayList<Airport>();

		try {
			List<String[]> data = CsvHelper.getData(
					ResourceUtils.getFile("classpath:data/airports.csv"));

			// Get all (valid) airports
			for (String[] airportData : data) {
				if (airportData.length == 3) {
					airports.add(Airport.builder().name(airportData[0])
							.distance(Integer.valueOf(airportData[1]))
							.timezone(airportData[2]).build());
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		airportRepository.saveAll(airports);
	}
}

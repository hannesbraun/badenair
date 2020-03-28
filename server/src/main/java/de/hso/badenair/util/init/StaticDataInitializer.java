package de.hso.badenair.util.init;

import de.hso.badenair.domain.plane.PlaneType;
import de.hso.badenair.domain.plane.PlaneTypeData;
import de.hso.badenair.service.plane.repository.PlaneTypeDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StaticDataInitializer {

    private final PlaneTypeDataRepository planeTypeDataRepository;

    @PostConstruct
    private void init() {
        initPlaneTypeData();

        // TODO: Initialize all static data
    }

    private void initPlaneTypeData() {
        final PlaneTypeData planeTypeDataDash_8_400 = PlaneTypeData.builder()
            .type(PlaneType.Dash_8_400)
            .numberOfPassengers(68)
            .flightRange(2000) // TODO: Find actual range
            .build();

        final PlaneTypeData planeTypeDataDash_8_200 = PlaneTypeData.builder()
            .type(PlaneType.Dash_8_200)
            .numberOfPassengers(38)
            .flightRange(2000) // TODO: Find actual range
            .build();

        final PlaneTypeData planeTypeDataB737_400 = PlaneTypeData.builder()
            .type(PlaneType.B737_400)
            .numberOfPassengers(188)
            .flightRange(2000) // TODO: Find actual range
            .build();

        planeTypeDataRepository.saveAll(List.of(planeTypeDataDash_8_400, planeTypeDataDash_8_200, planeTypeDataB737_400));
    }
}

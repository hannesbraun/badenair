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
            .numberOfPassengers(76)
            .numberOfRows(19)
            .numberOfColumns(4)
            .flightRange(1000).build();

        final PlaneTypeData planeTypeDataDash_8_200 = PlaneTypeData.builder()
            .type(PlaneType.Dash_8_200)
            .numberOfPassengers(36)
            .numberOfRows(9)
            .numberOfColumns(4)
            .flightRange(1000).build();

        final PlaneTypeData planeTypeDataB737_400 = PlaneTypeData.builder()
            .type(PlaneType.B737_400)
            .numberOfPassengers(186)
            .numberOfRows(31)
            .numberOfColumns(6)
            .flightRange(2500).build();

        planeTypeDataRepository.saveAll(List.of(planeTypeDataDash_8_400,
            planeTypeDataDash_8_200, planeTypeDataB737_400));
    }
}

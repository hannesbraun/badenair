package de.hso.badenair.util.init;

import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class CrewData {
    @Getter List<String> employees = new ArrayList<>();
    @Setter OffsetDateTime busyUntil = null;
}

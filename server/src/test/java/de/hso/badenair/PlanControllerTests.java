package de.hso.badenair;

import de.hso.badenair.controller.PlanController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlanControllerTests {
    @Autowired
    private PlanController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
}

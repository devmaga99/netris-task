package ru.saiev.netristask.controller;

import ru.saiev.netristask.dto.AggregatedCameraData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CameraControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetAggregatedCameras() {
        ResponseEntity<List<AggregatedCameraData>> response = this.restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/aggregated-cameras",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<AggregatedCameraData> aggregatedCameraDataList = response.getBody();

        assertThat(aggregatedCameraDataList).isNotNull();
        assertThat(aggregatedCameraDataList.size()).isGreaterThan(0);
    }
}
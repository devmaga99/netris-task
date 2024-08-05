package ru.saiev.netristask.controller;

import ru.saiev.netristask.dto.AggregatedCameraData;
import ru.saiev.netristask.service.CameraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CameraController {

    private final CameraService cameraService;

    @GetMapping("/aggregated-cameras")
    public List<AggregatedCameraData> getAggregatedCameras() {
        return cameraService.getAggregatedCameraData();
    }
}

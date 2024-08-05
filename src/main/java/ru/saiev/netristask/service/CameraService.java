package ru.saiev.netristask.service;

import ru.saiev.netristask.client.NetrisCameraClient;
import ru.saiev.netristask.dto.AggregatedCameraData;
import ru.saiev.netristask.dto.CameraResponse;
import ru.saiev.netristask.dto.SourceDataResponse;
import ru.saiev.netristask.dto.TokenDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class CameraService {

    private final NetrisCameraClient cameraClient;
    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    public List<AggregatedCameraData> getAggregatedCameraData() {
        List<CameraResponse> cameras = cameraClient.getCameras();

        List<CompletableFuture<AggregatedCameraData>> aggregatedCamerasFuture = new ArrayList<>();
        for (CameraResponse camera : cameras) {
            aggregatedCamerasFuture.add(aggregateCameraDataAsync(camera));
        }

        return aggregatedCamerasFuture.stream()
                .map(CompletableFuture::join)
                .toList();
    }

    private CompletableFuture<AggregatedCameraData> aggregateCameraDataAsync(CameraResponse camera) {
        var sourceDataResponseCompletableFuture =
                CompletableFuture.supplyAsync(() -> cameraClient.getSourceData(getSourceDataId(camera)), threadPool);

        var tokenDataResponseCompletableFuture =
                CompletableFuture.supplyAsync(() -> cameraClient.getTokenData(getTokenDataId(camera)), threadPool);


        return sourceDataResponseCompletableFuture
                .thenCombineAsync(
                        tokenDataResponseCompletableFuture,
                        (sourceDataResponse, tokenDataResponse) -> buildAggregatedCameraData(
                                camera,
                                sourceDataResponse,
                                tokenDataResponse
                        ),
                        threadPool
                );
    }

    private String getSourceDataId(CameraResponse camera) {
        String url = camera.getSourceDataUrl();
        return getLastPartOfUrl(url);
    }

    private String getTokenDataId(CameraResponse camera) {
        String url = camera.getTokenDataUrl();
        return getLastPartOfUrl(url);
    }

    private String getLastPartOfUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    private static AggregatedCameraData buildAggregatedCameraData(
            CameraResponse camera,
            SourceDataResponse sourceDataResponse,
            TokenDataResponse tokenDataResponse
    ) {
        return AggregatedCameraData.builder()
                .id(camera.getId())
                .urlType(sourceDataResponse.getUrlType())
                .videoUrl(sourceDataResponse.getVideoUrl())
                .value(tokenDataResponse.getValue())
                .ttl(tokenDataResponse.getTtl())
                .build();
    }
}

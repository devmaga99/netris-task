package ru.saiev.netristask.client;

import ru.saiev.netristask.dto.CameraResponse;
import ru.saiev.netristask.dto.SourceDataResponse;
import ru.saiev.netristask.dto.TokenDataResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "netrisCameraClient", url = "https://run.mocky.io/v3")
public interface NetrisCameraClient {

    @GetMapping("/362027cf-d1df-4c6c-87c3-1c9ab1a8a3d6")
    List<CameraResponse> getCameras();

    @GetMapping("/{sourceDataId}")
    SourceDataResponse getSourceData(@PathVariable("sourceDataId") String sourceDataId);

    @GetMapping("/{tokenDataId}")
    TokenDataResponse getTokenData(@PathVariable("tokenDataId") String tokenDataId);
}

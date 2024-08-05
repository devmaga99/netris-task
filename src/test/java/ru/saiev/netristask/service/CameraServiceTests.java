package ru.saiev.netristask.service;

import ru.saiev.netristask.client.NetrisCameraClient;
import ru.saiev.netristask.dto.AggregatedCameraData;
import ru.saiev.netristask.dto.CameraResponse;
import ru.saiev.netristask.dto.SourceDataResponse;
import ru.saiev.netristask.dto.TokenDataResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith({ SpringExtension.class, MockitoExtension.class })
public class CameraServiceTests {

    @Mock
    private NetrisCameraClient cameraClient;

    private CameraService cameraService;

    @BeforeEach
    void setUp() {
        cameraService = new CameraService(cameraClient);
    }

    @Test
    public void testGetAggregatedCameraData() {
        CameraResponse camera1 = new CameraResponse();
        camera1.setId(1);
        camera1.setSourceDataUrl("source1");
        camera1.setTokenDataUrl("token1");

        CameraResponse camera2 = new CameraResponse();
        camera2.setId(2);
        camera2.setSourceDataUrl("source2");
        camera2.setTokenDataUrl("token2");

        List<CameraResponse> cameraResponses = Arrays.asList(camera1, camera2);

        SourceDataResponse sourceData1 = new SourceDataResponse();
        sourceData1.setUrlType("LIVE");
        sourceData1.setVideoUrl("rtsp://127.0.0.1/1");

        SourceDataResponse sourceData2 = new SourceDataResponse();
        sourceData2.setUrlType("ARCHIVE");
        sourceData2.setVideoUrl("rtsp://127.0.0.1/2");

        TokenDataResponse tokenData1 = new TokenDataResponse();
        tokenData1.setValue("fa4b588e-249b-11e9-ab14-d663bd873d93");
        tokenData1.setTtl(120);

        TokenDataResponse tokenData2 = new TokenDataResponse();
        tokenData2.setValue("fa4b5d52-249b-11e9-ab14-d663bd873d93");
        tokenData2.setTtl(60);

        given(cameraClient.getCameras()).willReturn(cameraResponses);
        given(cameraClient.getSourceData("source1")).willReturn(sourceData1);
        given(cameraClient.getSourceData("source2")).willReturn(sourceData2);
        given(cameraClient.getTokenData("token1")).willReturn(tokenData1);
        given(cameraClient.getTokenData("token2")).willReturn(tokenData2);

        List<AggregatedCameraData> aggregatedCameraData = cameraService.getAggregatedCameraData();

        assertEquals(2, aggregatedCameraData.size());

        AggregatedCameraData aggregated1 = aggregatedCameraData.get(0);
        assertEquals(1, aggregated1.getId());
        assertEquals("LIVE", aggregated1.getUrlType());
        assertEquals("rtsp://127.0.0.1/1", aggregated1.getVideoUrl());
        assertEquals("fa4b588e-249b-11e9-ab14-d663bd873d93", aggregated1.getValue());
        assertEquals(120, aggregated1.getTtl());

        AggregatedCameraData aggregated2 = aggregatedCameraData.get(1);
        assertEquals(2, aggregated2.getId());
        assertEquals("ARCHIVE", aggregated2.getUrlType());
        assertEquals("rtsp://127.0.0.1/2", aggregated2.getVideoUrl());
        assertEquals("fa4b5d52-249b-11e9-ab14-d663bd873d93", aggregated2.getValue());
        assertEquals(60, aggregated2.getTtl());
    }
}

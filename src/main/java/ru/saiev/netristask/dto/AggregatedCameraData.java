package ru.saiev.netristask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AggregatedCameraData {
    private int id;
    private String urlType;
    private String videoUrl;
    private String value;
    private long ttl;
}

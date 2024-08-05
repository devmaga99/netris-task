package ru.saiev.netristask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SourceDataResponse {
    private String urlType;
    private String videoUrl;
}
package ru.saiev.netristask.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraResponse {
    private int id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
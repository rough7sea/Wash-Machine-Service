package com.example.washmachine.api.dto;

import com.example.washmachine.entity.WashParams;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WashActionDto {
    private Long actionId;
    private Long machineId;
    private String washMode;

    private String status;
    private WashParams customParams;
}

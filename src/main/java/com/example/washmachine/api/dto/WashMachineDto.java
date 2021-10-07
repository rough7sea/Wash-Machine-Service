package com.example.washmachine.api.dto;

import lombok.Data;

@Data
public class WashMachineDto{
    private Long machineId;
    private String name;
    private String status;
}

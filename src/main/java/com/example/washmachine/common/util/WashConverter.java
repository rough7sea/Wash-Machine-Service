package com.example.washmachine.common.util;

import java.util.Objects;
import java.util.UUID;

import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashStep;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.common.MachineStatus;
import com.example.washmachine.common.WashMode;
import com.example.washmachine.entity.WashEvent;
import com.example.washmachine.entity.WashMachine;

public class WashConverter {

    private WashConverter(){}

    public static WashMachine convertWashMachine(WashMachineDto dto){
        return WashMachine.builder()
                .id(dto.getId() != null ? dto.getId() : String.valueOf(UUID.randomUUID()))
                .name(dto.getName())
                .status(MachineStatus.valueOf(dto.getStatus()))
//                .description(dto.getDescription())
//                .accessories(dto.getAccessories())
                .build();
    }

    public static WashAction convertWashAction(WashActionDto dto){
        String id = dto.getActionId() != null ? dto.getActionId() : String.valueOf(UUID.randomUUID());
        if (Objects.nonNull(dto.getCustomParams())){
            dto.getCustomParams().setActionId(id);
        }
        return WashAction.builder()
                .actionId(id)
                .machineId(dto.getMachineId())
                .status(WashActionStatus.valueOf(dto.getStatus()))
                .washMode(WashMode.valueOf(dto.getWashMode()))
                .customParams(dto.getCustomParams())
                .build();
    }

    public static WashEvent convertWashEvent(WashEventDto dto){
        return WashEvent.builder()
                .actionId(dto.getActionId())
                .step(WashStep.valueOf(dto.getStep()))
                .build();
    }

}

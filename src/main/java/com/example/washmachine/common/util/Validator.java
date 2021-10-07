package com.example.washmachine.common.util;

import java.util.Objects;

import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashStep;
import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.exception.ServiceException;
import com.google.common.primitives.Longs;
import org.apache.commons.lang3.StringUtils;

public class Validator {

    public static void validate(WashEventDto event) {
        validateActionId(event.getActionId());
        if (!WashStep.contains(event.getStep())){
            throw new ServiceException(ExceptionId.INVALID_WASH_STEP_EX, "Invalid wash step");
        }
    }

    public static void validateMachineId(Long machineId) {
        if (machineId == null){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_ID_EX, "Invalid machine id");
        }
    }

    public static void validateActionId(Long actionId) {
        if (actionId == null){
            throw new ServiceException(ExceptionId.INVALID_ACTION_ID_EX, "Invalid action id");
        }
    }

    public static void validate(WashActionDto dto) {
        if (Objects.isNull(dto)){
            throw new ServiceException(ExceptionId.EMPTY_POST_BODY_REQUEST_EX, "Empty POST request");
        }
        validateMachineId(dto.getMachineId());
        if (!WashActionStatus.contains(dto.getStatus())){
            throw new ServiceException(ExceptionId.INVALID_WASH_ACTION_STATUS_EX, "Invalid action status");
        }
    }
}

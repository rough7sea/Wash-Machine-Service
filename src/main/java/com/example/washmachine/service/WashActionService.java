package com.example.washmachine.service;

import java.util.List;
import java.util.Objects;

import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.exception.ServiceException;
import com.example.washmachine.common.mappers.WashMapper;
import com.example.washmachine.common.util.ServiceUtils;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.entity.WashMachine;
import com.example.washmachine.repositories.WashActionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.washmachine.common.util.Validator.*;

@Service
public class WashActionService {


    private final WashMachineService washMachineService;
    private final WashActionRepository washActionRepository;

    public WashActionService(WashMachineService washMachineService, WashActionRepository washActionRepository) {
        this.washMachineService = washMachineService;
        this.washActionRepository = washActionRepository;
    }


    public List<WashAction> list() {
        return washActionRepository.findAll();
    }

    public WashAction getWashAction(Long actionId) {
        validateActionId(actionId);

        return ServiceUtils.executeWithCatch(
                () -> washActionRepository.getById(actionId),
                ExceptionId.INVALID_ACTION_ID_EX,
                "No action by id [" + actionId + "]");
    }

    public List<WashAction> getWashActionByMachineId(Long machineId) {
        validateMachineId(machineId);

        WashMachine machine = ServiceUtils.executeWithCatch(
                () -> washMachineService.getWashMachine(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX,
                "Invalid machineId [" + machineId + "]");

        return machine.getActions();
    }

    @Transactional
    public WashAction upsertWashAction(WashActionDto dto) {
        validate(dto);
        // check machine status before

        WashMachine machine = washMachineService.getWashMachine(dto.getMachineId());
        if (machine == null){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_ID_EX,
                    "Invalid machineId [" + dto.getMachineId() + "]");
        }
        if (washMachineService.isWashMachineInactive(machine)){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_STATE_EX,
                    "Machine is INACTIVE. Action can't be executed");
        }
        WashAction action = WashMapper.INSTANCE.toWashAction(dto);

        if (Objects.nonNull(action.getActionId())){
            // check in case new action
            boolean anyMatch = machine.getActions().stream()
                    .anyMatch(a -> !a.getStatus().equals(WashActionStatus.STOPPED) &&
                            !a.getStatus().equals(WashActionStatus.COMPLETED));

            if (anyMatch){
                throw new ServiceException(ExceptionId.INVALID_MACHINE_STATE_EX, "Machine have ACTIVE actions");
            }
        }

        action.setMachine(machine);
        return washActionRepository.save(action);
    }
}

package com.example.washmachine.service;

import java.util.List;
import java.util.Objects;

import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.util.WashConverter;
import com.example.washmachine.dao.WashActionDao;
import com.example.washmachine.dao.WashParamsDao;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.common.exception.ServiceException;
import com.example.washmachine.common.util.ServiceUtils;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.entity.WashParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.washmachine.common.util.Validator.*;

@Service
public class WashActionService {


    private final WashActionDao washActionDao;
    private final WashParamsDao washParamsDao;
    private final WashMachineService washMachineService;

    public WashActionService(WashActionDao washActionDao, WashParamsDao washParamsDao, WashMachineService washMachineService) {
        this.washActionDao = washActionDao;
        this.washParamsDao = washParamsDao;
        this.washMachineService = washMachineService;
    }


    public List<WashAction> list() {
        return washActionDao.getAll();
    }

    public WashAction getWashAction(String actionId) {
        validateActionId(actionId);

        WashAction action = ServiceUtils.executeWithCatch(
                () -> washActionDao.getActionById(actionId),
                ExceptionId.INVALID_ACTION_ID_EX,
                "No action by id [" + actionId + "]");

        WashParams params = ServiceUtils.executeWithCatch(
                () -> washParamsDao.getWashParams(actionId),
                ExceptionId.INVALID_ACTION_ID_EX,
                "No action by id [" + actionId + "]");

        action.setCustomParams(params);

        return action;
    }

    public List<WashAction> getWashActionByMachineId(String machineId) {
        validateMachineId(machineId);

        List<WashAction> action = ServiceUtils.executeWithCatch(
                () -> washActionDao.getActionsByMachineId(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX,
                "Invalid machineId [" + machineId + "]");

        action.forEach(
                washAction -> {
                    WashParams params = ServiceUtils.executeWithCatch(
                            () -> washParamsDao.getWashParams(washAction.getActionId()),
                            ExceptionId.INVALID_POST_BODY_REQUEST_EX,
                            "Invalid action request");
                    washAction.setCustomParams(params);
                }
        );

        return action;
    }

    @Transactional
    public WashAction upsertWashAction(WashActionDto dto) {
        validate(dto);
        // check machine status before
        if (washMachineService.isWashMachineInactive(dto.getMachineId())){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_STATE_EX,
                    "Machine is INACTIVE. Action can't be executed");
        }

        if (Objects.nonNull(dto.getActionId())){
            return updateWashAction(dto);
        }
        return insertWashAction(dto);
    }

    private WashAction updateWashAction(WashActionDto dto) {
        WashAction action = ServiceUtils.executeWithCatch(
                () -> washActionDao.updateAction(WashConverter.convertWashAction(dto)),
                ExceptionId.INVALID_POST_BODY_REQUEST_EX,
                "Invalid action request");

        action.setCustomParams(
                ServiceUtils.executeWithCatch(
                () -> washParamsDao.getWashParams(action.getActionId()),
                ExceptionId.INVALID_POST_BODY_REQUEST_EX,
                "Invalid action request")
        );

        return action;
    }

    private WashAction insertWashAction(WashActionDto dto) {

        List<WashAction> machineActions = ServiceUtils.executeWithCatch(
                () -> washActionDao.getActionsByMachineId(dto.getMachineId()),
                ExceptionId.INVALID_POST_BODY_REQUEST_EX,
                "Invalid action request");

        boolean anyMatch = machineActions.stream()
                .anyMatch(a -> !a.getStatus().equals(WashActionStatus.STOPPED) &&
                        !a.getStatus().equals(WashActionStatus.COMPLETED));

        if (anyMatch){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_STATE_EX, "Machine have ACTIVE actions");
        }


        WashAction action = ServiceUtils.executeWithCatch(
                () -> washActionDao.insertAction(WashConverter.convertWashAction(dto)),
                ExceptionId.INVALID_POST_BODY_REQUEST_EX,
                "Invalid action request");

        if (Objects.isNull(dto.getCustomParams())){
            return action;
        }

        // insert washParams
        ServiceUtils.executeWithCatch(
                () -> washParamsDao.insertParams(dto.getCustomParams()),
                ExceptionId.INVALID_POST_BODY_REQUEST_EX,
                "Invalid action request");

        action.setCustomParams(dto.getCustomParams());
        return action;
    }
}

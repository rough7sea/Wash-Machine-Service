package com.example.washmachine.service;

import java.util.List;

import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashStep;
import com.example.washmachine.common.mappers.WashMapper;
import com.example.washmachine.dao.WashActionDao;
import com.example.washmachine.dao.WashEventDao;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.exception.ServiceException;
import com.example.washmachine.common.util.ServiceUtils;
import com.example.washmachine.entity.WashEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.washmachine.common.util.Validator.validate;
import static com.example.washmachine.common.util.Validator.validateActionId;

@Service
public class WashEventService {


    private final WashEventDao washEventDao;
    private final WashActionDao washActionDao;
    private final WashActionService washActionService;
    private final WashMachineService washMachineService;

    public WashEventService(WashEventDao washEventDao, WashActionDao washActionDao,
                            WashActionService washActionService, WashMachineService washMachineService) {
        this.washEventDao = washEventDao;
        this.washActionDao = washActionDao;
        this.washActionService = washActionService;
        this.washMachineService = washMachineService;
    }

    @Transactional
    public WashEvent insertEvent(WashEventDto dto) {
        validate(dto);

        WashAction action = ServiceUtils.executeWithCatch(
                () -> washActionService.getWashAction(dto.getActionId()),
                ExceptionId.INVALID_ACTION_ID_EX, "No action by id [" + dto.getActionId() + "]");


        if (washMachineService.isWashMachineInactive(action.getMachineId())){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_STATE_EX,
                    "Machine is INACTIVE. Action can't be executed");
        }

        WashEvent washEventFromDto = WashMapper.INSTANCE.toWashEvent(dto);

        if (action.getStatus() != WashActionStatus.PROCESS && washEventFromDto.getStep() != WashStep.COMPLETE){
            throw new ServiceException(ExceptionId.INVALID_ACTION_STATE_EX,
                    "Action must be in PROCESS to change step");
        }

        WashEvent washEvent = ServiceUtils.executeWithCatch(
                () -> washEventDao.insertEvent(washEventFromDto),
                ExceptionId.INVALID_ACTION_ID_EX, "No event by action id [" + dto.getActionId() + "]");

        if (washEvent.getStep() == WashStep.COMPLETE){
            washActionDao.updateWashActionStatus(
                    WashAction.builder()
                            .actionId(washEvent.getActionId())
                            .status(WashActionStatus.COMPLETED)
                            .build()
            );
        }
        return washEvent;
    }


    public List<WashEvent> getEventsByActionId(String actionId) {
        validateActionId(actionId);
        return ServiceUtils.executeWithCatch(
                () -> washEventDao.getEventsByActionId(actionId),
                ExceptionId.INVALID_ACTION_ID_EX, "No action by id [" + actionId + "]");
    }


    public WashEvent currentActionEvent(String actionId) {
        validateActionId(actionId);
        return ServiceUtils.executeWithCatch(
                () -> washEventDao.currentActionEvent(actionId),
                ExceptionId.INVALID_ACTION_ID_EX, "No action by id [" + actionId + "]"
        );
    }
}

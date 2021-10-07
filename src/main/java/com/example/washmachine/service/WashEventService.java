package com.example.washmachine.service;

import java.util.List;

import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashStep;
import com.example.washmachine.common.mappers.WashMapper;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.exception.ServiceException;
import com.example.washmachine.common.util.ServiceUtils;
import com.example.washmachine.entity.WashEvent;
import com.example.washmachine.repositories.WashEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.washmachine.common.util.Validator.validate;
import static com.example.washmachine.common.util.Validator.validateActionId;

@Service
public class WashEventService {


    private final WashEventRepository washEventRepository;
    private final WashActionService washActionService;
    private final WashMachineService washMachineService;

    public WashEventService(WashEventRepository washEventRepository,
                            WashActionService washActionService,
                            WashMachineService washMachineService) {
        this.washEventRepository = washEventRepository;
        this.washActionService = washActionService;
        this.washMachineService = washMachineService;
    }

    @Transactional
    public WashEvent insertEvent(WashEventDto dto) {

        validate(dto);
        WashEvent event = WashMapper.INSTANCE.toWashEvent(dto);

        WashAction action = ServiceUtils.executeWithCatch(
                () -> washActionService.getWashAction(event.getActionId()),
                ExceptionId.INVALID_ACTION_ID_EX, "No action by id [" + dto.getActionId() + "]");


        if (washMachineService.isWashMachineInactive(action.getMachine())){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_STATE_EX,
                    "Machine is INACTIVE. Action can't be executed");
        }

        if (action.getStatus() != WashActionStatus.PROCESS && event.getStep() != WashStep.COMPLETE){
            throw new ServiceException(ExceptionId.INVALID_ACTION_STATE_EX,
                    "Action must be in PROCESS to change step");
        }

        ServiceUtils.executeWithCatch(
                () -> washEventRepository.save(event),
                ExceptionId.INVALID_ACTION_ID_EX,
                "No event by action id [" + dto.getActionId() + "]");

        if (event.getStep() == WashStep.COMPLETE){
            action.setStatus(WashActionStatus.COMPLETED);
            // chek if need to be safe
        }

        return event;
    }


    public List<WashEvent> getEventsByActionId(Long actionId) {
        validateActionId(actionId);

        return ServiceUtils.executeWithCatch(
                () -> washEventRepository.getAllByActionId(actionId),
                ExceptionId.INVALID_ACTION_ID_EX, "No action by id [" + actionId + "]");
    }


    public WashEvent currentActionEvent(Long actionId) {
        validateActionId(actionId);

        List<WashEvent> events = ServiceUtils.executeWithCatch(
                () -> washEventRepository.getAllByActionId(actionId),
                ExceptionId.INVALID_ACTION_ID_EX, "No action by id [" + actionId + "]");

        return events.stream().max((e1, e2) -> {
            if (e1.getUpdateDate() != null) {
                if (e2.getUpdateDate() != null) {
                    return e1.getUpdateDate().compareTo(e2.getUpdateDate());
                }
                return e1.getUpdateDate().compareTo(e2.getCreateDate());
            } else {
                if (e2.getUpdateDate() != null) {
                    return e1.getCreateDate().compareTo(e2.getUpdateDate());
                }
                return e1.getCreateDate().compareTo(e2.getCreateDate());
            }
        }).orElse(null);
    }
}

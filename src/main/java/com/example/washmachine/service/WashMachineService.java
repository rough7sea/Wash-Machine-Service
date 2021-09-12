package com.example.washmachine.service;

import java.util.List;
import java.util.Objects;

import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.common.MachineStatus;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.exception.ServiceException;
import com.example.washmachine.common.util.ServiceUtils;
import com.example.washmachine.common.util.Validator;
import com.example.washmachine.common.util.WashConverter;
import com.example.washmachine.dao.WashMachineDao;
import com.example.washmachine.entity.WashMachine;
import com.example.washmachine.listener.WashMachineListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WashMachineService {

    @Qualifier(value = "getWashMachineListener")
    // TODO not implemented yet
    private final WashMachineListener<WashMachine> washMachineListener;

    private final WashMachineDao washMachineDao;

    public WashMachineService(WashMachineListener<WashMachine> washMachineListener, WashMachineDao washMachineDao) {
        this.washMachineListener = washMachineListener;
        this.washMachineDao = washMachineDao;
    }


    public List<WashMachine> list() {
        return ServiceUtils.executeWithCatch(
                () -> washMachineDao.getAll(),
                ExceptionId.INTERNAL_ERROR, "Something go wrong");
    }


    public WashMachine getWashMachine(String machineId) {
        Validator.validateMachineId(machineId);
        return ServiceUtils.executeWithCatch(
                () -> washMachineDao.getWashMachine(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX,
                "No any machine by id [" + machineId + "]"
        );
    }


    public boolean deleteWashMachine(String machineId) {

        Validator.validateMachineId(machineId);

        String exMessage = "No any machine by id [" + machineId + "]";

        boolean delete = ServiceUtils.executeWithCatch(
                () -> washMachineDao.deleteWashMachine(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX, exMessage);

        if (!delete){
            throw new ServiceException(ExceptionId.INVALID_MACHINE_ID_EX, exMessage);
        }

        return true;
    }


    public WashMachine upsertWashMachine(WashMachineDto dto) {
        WashMachine washMachine = WashConverter.convertWashMachine(dto);

        if (Objects.nonNull(dto.getId())){
            return ServiceUtils.executeWithCatch(
                    () -> washMachineDao.updateWashMachine(washMachine),
                    ExceptionId.INVALID_POST_BODY_REQUEST_EX, "Invalid POST request");
        }
        return ServiceUtils.executeWithCatch(
                () -> washMachineDao.insertWashMachine(washMachine),
                ExceptionId.INVALID_POST_BODY_REQUEST_EX, "Invalid POST request");
    }


    public boolean isWashMachineInactive(String machineId){
        WashMachine machine = ServiceUtils.executeWithCatch(
                () -> washMachineDao.getWashMachine(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX,
                "Invalid machineId [" + machineId + "]");
        return machine.getStatus() == MachineStatus.INACTIVE;
    }
}

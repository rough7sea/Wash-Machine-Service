package com.example.washmachine.service.impl;

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
import com.example.washmachine.service.WashMachineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WashMachineServiceImpl implements WashMachineService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WashMachineServiceImpl.class);

    @Autowired
    @Qualifier(value = "getWashMachineListener")
    // TODO not implemented yet
    private WashMachineListener<WashMachine> washMachineListener;

    @Autowired
    private WashMachineDao washMachineDao;

    @Override
    public List<WashMachine> list() {
        return ServiceUtils.executeWithCatch(
                () -> washMachineDao.getAll(),
                ExceptionId.INTERNAL_ERROR, "Something go wrong");
    }

    @Override
    public WashMachine getWashMachine(String machineId) {
        Validator.validateMachineId(machineId);
        return ServiceUtils.executeWithCatch(
                () -> washMachineDao.getWashMachine(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX,
                "No any machine by id [" + machineId + "]"
        );
    }

    @Override
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

    @Override
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

    @Override
    public boolean isWashMachineInactive(String machineId){
        WashMachine machine = ServiceUtils.executeWithCatch(
                () -> washMachineDao.getWashMachine(machineId),
                ExceptionId.INVALID_MACHINE_ID_EX,
                "Invalid machineId [" + machineId + "]");
        return machine.getStatus() == MachineStatus.INACTIVE;
    }
}

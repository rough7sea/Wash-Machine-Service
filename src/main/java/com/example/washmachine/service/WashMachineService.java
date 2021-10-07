package com.example.washmachine.service;

import java.util.List;

import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.common.MachineStatus;
import com.example.washmachine.common.exception.ExceptionId;
import com.example.washmachine.common.mappers.WashMapper;
import com.example.washmachine.common.util.Callable;
import com.example.washmachine.common.util.ServiceUtils;
import com.example.washmachine.common.util.Validator;
import com.example.washmachine.entity.WashMachine;
import com.example.washmachine.listener.WashMachineListener;
import com.example.washmachine.repositories.WashMachineRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class WashMachineService {

    // TODO not implemented yet
    @Qualifier(value = "getWashMachineListener")
    private final WashMachineListener<WashMachine> washMachineListener;

    private final WashMachineRepository washMachineRepository;

    public WashMachineService(WashMachineListener<WashMachine> washMachineListener,
                              WashMachineRepository washMachineRepository) {
        this.washMachineListener = washMachineListener;
        this.washMachineRepository = washMachineRepository;
    }


    public List<WashMachine> list() {
        return ServiceUtils.executeWithCatch(
                (Callable) washMachineRepository::findAll,
                ExceptionId.INTERNAL_ERROR, "Something go wrong");
    }


    public WashMachine getWashMachine(Long machineId) {
        Validator.validateMachineId(machineId);
        return washMachineRepository.findById(machineId).orElse(null);
    }


    public boolean deleteWashMachine(Long machineId) {
        Validator.validateMachineId(machineId);
        washMachineRepository.deleteById(machineId);
        return true;
    }


    public WashMachine upsertWashMachine(WashMachineDto dto) {
        WashMachine washMachine = WashMapper.INSTANCE.toWashMachine(dto);
        return washMachineRepository.saveAndFlush(washMachine);
    }


    public boolean isWashMachineInactive(WashMachine machine){
        return machine.getStatus() == MachineStatus.INACTIVE;
    }
}

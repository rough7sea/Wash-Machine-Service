package com.example.washmachine.service;

import java.util.List;

import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.entity.WashMachine;

public interface WashMachineService {

    /**
     * Get all machines.
     *
     * @return
     */
    List<WashMachine> list();

    /**
     * Receive machine.
     * @param machineId
     * @return
     */
    WashMachine getWashMachine(String machineId);

    /**
     * Delete machine.
     *
     * @param machineId
     * @return
     */
    boolean deleteWashMachine(String machineId);

    /**
     * Update or insert new machine.
     *
     * @param dto
     * @return
     */
    WashMachine upsertWashMachine(WashMachineDto dto);

    /**
     * Check is machine inactive.
     *
     * @param machineId
     * @return true if inactive, otherwise false.
     */
    boolean isWashMachineInactive(String machineId);
}

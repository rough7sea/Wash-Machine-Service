package com.example.washmachine.service;

import java.util.List;

import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.entity.WashAction;

public interface WashActionService {

    /**
     * Receive all actions.
     *
     * @return
     */
    List<WashAction> list();

    /**
     * Get current action.
     *
     * @param actionId
     * @return
     */
    WashAction getWashAction(String actionId);

    /**
     * Receive current machine actions.
     *
     * @param machineId
     * @return
     */
    List<WashAction> getWashActionByMachineId(String machineId);

    /**
     * Update or add new action status.
     *
     * @param dto
     * @return
     */
    WashAction upsertWashAction(WashActionDto dto);
}

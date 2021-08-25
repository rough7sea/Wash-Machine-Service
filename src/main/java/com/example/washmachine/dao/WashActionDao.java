package com.example.washmachine.dao;

import java.util.List;

import com.example.washmachine.entity.WashAction;

public interface WashActionDao {

    List<WashAction> getAll();

    WashAction getActionById(String actionId);

    List<WashAction> getActionsByMachineId(String machineId);

    WashAction updateAction(WashAction action);

    WashAction insertAction(WashAction action);

    WashAction updateWashActionStatus(WashAction action);

}

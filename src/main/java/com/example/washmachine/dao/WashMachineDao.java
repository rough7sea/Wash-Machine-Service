package com.example.washmachine.dao;

import java.util.List;

import com.example.washmachine.entity.WashMachine;

public interface WashMachineDao {

    List<WashMachine> getAll();

    WashMachine getWashMachine(String id);

    WashMachine insertWashMachine(WashMachine washMachine);

    WashMachine updateWashMachine(WashMachine washMachine);

    boolean deleteWashMachine(String id);
}

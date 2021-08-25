package com.example.washmachine.executor.after.impl;

import com.example.washmachine.entity.WashMachine;
import com.example.washmachine.executor.after.WashMachineAfterExecutor;

public class WashMachineDescriptionEnrichAfterExecutor implements WashMachineAfterExecutor<WashMachine> {

    @Override
    public boolean execute(WashMachine washMachine) {
        return true;
    }
}

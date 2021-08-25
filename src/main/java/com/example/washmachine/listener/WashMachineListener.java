package com.example.washmachine.listener;

import java.util.Collections;
import java.util.List;

import com.example.washmachine.executor.before.WashMachineBeforeExecutor;
import com.example.washmachine.entity.WashMachine;
import com.example.washmachine.executor.after.WashMachineAfterExecutor;

public class WashMachineListener<T extends WashMachine> {

    private List<WashMachineBeforeExecutor<T>> beforeExecutors = Collections.emptyList();

    private List<WashMachineAfterExecutor<T>> afterExecutors = Collections.emptyList();


    public boolean before(T t){
        for (WashMachineBeforeExecutor<T> executor: beforeExecutors){
            if (!executor.execute(t)){
                return false;
            }
        }
        return true;
    }

    public boolean after(T t){
        for (WashMachineAfterExecutor<T> executor: afterExecutors){
            if (!executor.execute(t)){
                return false;
            }
        }
        return true;
    }

    public List<WashMachineBeforeExecutor<T>> getBeforeExecutors() {
        return beforeExecutors;
    }

    public void setBeforeExecutors(List<WashMachineBeforeExecutor<T>> beforeExecutors) {
        this.beforeExecutors = beforeExecutors;
    }

    public List<WashMachineAfterExecutor<T>> getAfterExecutors() {
        return afterExecutors;
    }

    public void setAfterExecutors(List<WashMachineAfterExecutor<T>> afterExecutors) {
        this.afterExecutors = afterExecutors;
    }
}

package com.example.washmachine.common;

import org.apache.commons.lang3.StringUtils;

public enum WashActionStatus {
    PROCESS, STOPPED, PAUSED, COMPLETED;

    public static boolean contains(String status){
        if (StringUtils.isBlank(status)){
            return false;
        }
        for (WashActionStatus washActionStatus : WashActionStatus.values()) {
            if (washActionStatus.name().equals(status)){
                return true;
            }

        }
        return false;
    }
}

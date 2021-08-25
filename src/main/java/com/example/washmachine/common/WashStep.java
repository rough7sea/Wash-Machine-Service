package com.example.washmachine.common;

import org.apache.commons.lang3.StringUtils;

public enum WashStep {
    START, SOAK, WASH, SPIN, RINSE, SPEED_SPIN, FINAL_SPIN, COMPLETE;

    public static boolean contains(String step){
        if (StringUtils.isBlank(step)){
            return false;
        }
        for (WashStep washStep : WashStep.values()) {
            if (washStep.name().equals(step)){
                return true;
            }

        }
        return false;
    }
}

package com.example.washmachine.common;

import com.example.washmachine.entity.WashParams;
import org.apache.commons.lang3.StringUtils;

public enum WashMode {
    /* just default params. Can be used in machine application side. */
    DAILY(new WashParams(600, 3, 60, "default", "default")),
    LINENS(new WashParams(600, 3, 60, "default", "default")),
    JEANS(new WashParams(600, 3, 60, "default", "default")),
    SYNTHETICS(new WashParams(600, 3, 60, "default", "default")),
    COTTON(new WashParams(600, 3, 60, "default", "default")),
    OUTERWEAR(new WashParams(600, 3, 60, "default", "default")),
    BABY(new WashParams(600, 3, 60, "default", "default")),
    WOOL(new WashParams(600, 3, 60, "default", "default")),
    CUSTOM(new WashParams(600, 3, 60, "default", "default"));


    private WashParams defaultParams;

    WashMode(WashParams params) {
        this.defaultParams = params;
    }

    public WashParams getParams() {
        return defaultParams;
    }

    public void setParams(WashParams params) {
        this.defaultParams = params;
    }

    public static boolean contains(String mode){
        if (StringUtils.isBlank(mode)){
            return false;
        }
        for (WashMode washMode : WashMode.values()) {
            if (washMode.name().equals(mode)){
                return true;
            }

        }
        return false;
    }
}

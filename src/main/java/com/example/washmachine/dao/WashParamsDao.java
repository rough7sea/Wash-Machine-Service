package com.example.washmachine.dao;

import com.example.washmachine.entity.WashParams;

public interface WashParamsDao {

    WashParams insertParams(WashParams washParams);

    WashParams getWashParams(String actionId);

}

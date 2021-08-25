package com.example.washmachine.dao;

import java.util.List;

import com.example.washmachine.entity.WashEvent;

public interface WashEventDao {

    WashEvent insertEvent(WashEvent event);

    List<WashEvent> getEventsByActionId(String actionId);

    WashEvent currentActionEvent(String actionId);
}

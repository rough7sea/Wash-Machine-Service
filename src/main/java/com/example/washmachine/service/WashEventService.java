package com.example.washmachine.service;

import java.util.List;

import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.entity.WashEvent;

public interface WashEventService {

    /**
     * Add new event.
     *
     * @param washEventDto
     * @return
     */
    WashEvent insertEvent(WashEventDto washEventDto);

    /**
     * Receive action events.
     *
     * @param actionId
     * @return
     */
    List<WashEvent> getEventsByActionId(String actionId);

    /**
     * Get current action event.
     *
     * @param actionId
     * @return
     */
    WashEvent currentActionEvent(String actionId);

}

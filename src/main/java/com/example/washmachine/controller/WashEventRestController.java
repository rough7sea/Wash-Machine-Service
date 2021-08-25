package com.example.washmachine.controller;

import java.util.List;

import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.entity.WashEvent;
import com.example.washmachine.service.WashEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.washmachine.controller.Paths.WASH_EVENT_BASE_PATH;

@RestController
@RequestMapping(WASH_EVENT_BASE_PATH)
public class WashEventRestController {

    @Autowired
    private WashEventService washEventService;

    /**
     * Add new wash event to action.
     *
     * @param washEventDto
     * @return
     */
    @PostMapping
    public WashEvent insertWashEvent(@RequestBody WashEventDto washEventDto){
        return washEventService.insertEvent(washEventDto);
    }

    /**
     * Get all action events.
     *
     * @param actionId
     * @return
     */
    @GetMapping("action/{actionId}")
    public List<WashEvent> getEventsForAction(@PathVariable String actionId){
        return washEventService.getEventsByActionId(actionId);
    }

    /**
     * Get current action event.
     *
     * @param actionId
     * @return
     */
    @GetMapping("current/{actionId}")
    public WashEvent currentActionEvent(@PathVariable String actionId){
        return washEventService.currentActionEvent(actionId);
    }
}

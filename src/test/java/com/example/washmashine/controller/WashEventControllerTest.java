package com.example.washmashine.controller;

import java.util.Arrays;

import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.common.WashStep;
import com.example.washmachine.controller.WashEventRestController;
import com.example.washmachine.entity.WashEvent;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.washmachine.controller.Paths.WASH_EVENT_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"/wash-event-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/wash-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class WashEventControllerTest extends AbstractTest{

    private static final String ACTION_PROCESS_ID = "384aff4d-f4c3-42e3-bcaf-bc30fee4f1b1";
    private static final String ACTION_STOPPED_ID = "d7b2c7ee-5d50-40dd-8993-b099491f37e1";

    @Autowired
    private WashEventRestController eventRestController;

    @Test
    void eventInsertAndGet() throws Exception{

        WashEventDto eventDto = WashEventDto.builder()
                .actionId(ACTION_PROCESS_ID)
                .step(WashStep.START.name())
                .build();

        // first event
        MvcResult result = mockMvc.perform(post(WASH_EVENT_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(eventDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashEvent event1 = mapFromJson(result.getResponse().getContentAsString(), WashEvent.class);
        Assertions.assertEquals(eventDto.getActionId(), event1.getActionId());
        Assertions.assertEquals(eventDto.getStep(), event1.getStep().name());
        Assertions.assertNotNull(event1.getStartDate());

        // second event
        eventDto.setStep(WashStep.WASH.name());
        result = mockMvc.perform(post(WASH_EVENT_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(eventDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashEvent event2 = mapFromJson(result.getResponse().getContentAsString(), WashEvent.class);
        Assertions.assertEquals(eventDto.getActionId(), event2.getActionId());
        Assertions.assertEquals(eventDto.getStep(), event2.getStep().name());
        Assertions.assertNotNull(event2.getStartDate());

        // check event
        result = mockMvc.perform(get(WASH_EVENT_BASE_PATH + "action/" + ACTION_PROCESS_ID))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashEvent[] events = mapFromJson(result.getResponse().getContentAsString(), WashEvent[].class);
        Assertions.assertEquals(2, events.length);
        Assertions.assertTrue(Arrays.stream(events).anyMatch(e -> e.getStep() == WashStep.START));
        Assertions.assertTrue(Arrays.stream(events).anyMatch(e -> e.getStep() == WashStep.WASH));


        result = mockMvc.perform(get(WASH_EVENT_BASE_PATH + "current/" + ACTION_PROCESS_ID))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        WashEvent currentEvent = mapFromJson(result.getResponse().getContentAsString(), WashEvent.class);
        Assertions.assertEquals(WashStep.WASH, currentEvent.getStep());
    }

    @Test
    void eventFailInsertAndGet() throws Exception{

        WashEventDto eventDto = WashEventDto.builder()
                .actionId(ACTION_STOPPED_ID)
                .step(WashStep.START.name())
                .build();

        // Event to stopped action
        mockMvc.perform(post(WASH_EVENT_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(eventDto)))
                .andExpect(status().is4xxClientError());


        // check all events
        MvcResult result = mockMvc.perform(get(WASH_EVENT_BASE_PATH + "action/" + ACTION_STOPPED_ID))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        WashEvent[] events = mapFromJson(result.getResponse().getContentAsString(), WashEvent[].class);
        Assertions.assertEquals(0, events.length);


        // check current event
        result = mockMvc.perform(get(WASH_EVENT_BASE_PATH + "current/" + ACTION_PROCESS_ID))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        Assertions.assertTrue(StringUtils.isBlank(result.getResponse().getContentAsString()));
    }
}

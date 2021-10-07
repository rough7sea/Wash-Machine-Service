package com.example.washmashine.controller;

import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashMode;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.entity.WashParams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.washmachine.controller.Paths.WASH_ACTON_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = {"/wash-action-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/wash-before.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class WashActionControllerTest extends AbstractTest{

    private static final Long activeMachineUUID = 2L;
    private static final Long inactiveMachineUUID = 1L;

    @Test
    @Order(1)
    void actionFullInsert() throws Exception {
        // create action
        WashParams washParams = WashParams.builder()
                .powder("My powder")
                .conditioner("My conditioner")
                .rinsesCount(3)
                .temperature(60)
                .spinPower(600)
                .build();

        WashActionDto actionDto = WashActionDto.builder()
                .machineId(activeMachineUUID)
                .status(WashActionStatus.PROCESS.name())
                .washMode(WashMode.BABY.name())
                .customParams(washParams)
                .build();

        MvcResult result = mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //check action
        WashAction action = mapFromJson(result.getResponse().getContentAsString(), WashAction.class);
        Assertions.assertEquals(activeMachineUUID, action.getMachineId());
        Assertions.assertEquals(WashActionStatus.PROCESS, action.getStatus());
        Assertions.assertEquals(WashMode.BABY, action.getWashMode());
        Assertions.assertNotNull(action.getCreateDate());
        Assertions.assertNull(action.getUpdateDate());

        //check params
        WashParams customParams = action.getCustomParams();
        Assertions.assertNotNull(customParams);
        Assertions.assertEquals(action.getActionId(), customParams.getActionId());
        Assertions.assertEquals(washParams.getPowder(), customParams.getPowder());
        Assertions.assertEquals(washParams.getConditioner(), customParams.getConditioner());
        Assertions.assertEquals(washParams.getRinsesCount(), customParams.getRinsesCount());
        Assertions.assertEquals(washParams.getSpinPower(), customParams.getSpinPower());
        Assertions.assertEquals(washParams.getTemperature(), customParams.getTemperature());

    }

    @Test
    @Order(2)
    void actionUpsert() throws Exception {
        // create action
        WashActionDto actionDto = WashActionDto.builder()
                .machineId(activeMachineUUID)
                .status(WashActionStatus.PROCESS.name())
                .washMode(WashMode.BABY.name())
                .build();

        MvcResult result = mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashAction action = mapFromJson(result.getResponse().getContentAsString(), WashAction.class);
        Assertions.assertEquals(activeMachineUUID, action.getMachineId());
        Assertions.assertEquals(WashActionStatus.PROCESS, action.getStatus());
        Assertions.assertEquals(WashMode.BABY, action.getWashMode());
        Assertions.assertNull(action.getCustomParams());
        Assertions.assertNotNull(action.getCreateDate());
        Assertions.assertNull(action.getUpdateDate());


        // update action
        actionDto.setActionId(action.getActionId());
        actionDto.setWashMode(WashMode.WOOL.name());
        actionDto.setStatus(WashActionStatus.PAUSED.name());
        result = mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        action = mapFromJson(result.getResponse().getContentAsString(), WashAction.class);
        Assertions.assertEquals(activeMachineUUID, action.getMachineId());
        Assertions.assertEquals(WashActionStatus.PAUSED, action.getStatus());
        Assertions.assertEquals(WashMode.WOOL, action.getWashMode());
        Assertions.assertNull(action.getCustomParams());
        Assertions.assertNotNull(action.getCreateDate());
        Assertions.assertNotNull(action.getUpdateDate());
        Assertions.assertTrue(action.getCreateDate().before(action.getUpdateDate()));


        result = mockMvc.perform(get(WASH_ACTON_BASE_PATH))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashAction[] actions = mapFromJson(result.getResponse().getContentAsString(), WashAction[].class);
        Assertions.assertEquals(1, actions.length);


        // exception insert
        actionDto.setActionId(null);
        mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is4xxClientError());

    }

    @Test
    @Order(3)
    void actionFailUpsert() throws Exception {
        //exception create action
        WashActionDto actionDto = WashActionDto.builder()
                .status(WashActionStatus.PROCESS.name())
                .washMode(WashMode.BABY.name())
                .build();

        mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is4xxClientError());

        // inactive machine id
        actionDto.setMachineId(inactiveMachineUUID);
        mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is4xxClientError());


        MvcResult result = mockMvc.perform(get(WASH_ACTON_BASE_PATH))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashAction[] actions = mapFromJson(result.getResponse().getContentAsString(), WashAction[].class);
        Assertions.assertEquals(0, actions.length);



    }

    @Test
    @Order(4)
    void actionGet() throws Exception {
        // create action
        WashActionDto actionDto = WashActionDto.builder()
                .machineId(activeMachineUUID)
                .status(WashActionStatus.PROCESS.name())
                .washMode(WashMode.BABY.name())
                .build();

        MvcResult result = mockMvc.perform(post(WASH_ACTON_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(actionDto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashAction action = mapFromJson(result.getResponse().getContentAsString(), WashAction.class);


        // get by action id
        result = mockMvc.perform(get(WASH_ACTON_BASE_PATH + action.getActionId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        action = mapFromJson(result.getResponse().getContentAsString(), WashAction.class);
        Assertions.assertEquals(activeMachineUUID, action.getMachineId());
        Assertions.assertEquals(WashActionStatus.PROCESS, action.getStatus());
        Assertions.assertEquals(WashMode.BABY, action.getWashMode());
        Assertions.assertNull(action.getCustomParams());
        Assertions.assertNotNull(action.getCreateDate());
        Assertions.assertNull(action.getUpdateDate());


        // get by machine id
        result = mockMvc.perform(get(WASH_ACTON_BASE_PATH + "machine/" + action.getMachineId()))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashAction[] actions = mapFromJson(result.getResponse().getContentAsString(), WashAction[].class);
        Assertions.assertEquals(1, actions.length);

        action = actions[0];
        Assertions.assertEquals(activeMachineUUID, action.getMachineId());
        Assertions.assertEquals(WashActionStatus.PROCESS, action.getStatus());
        Assertions.assertEquals(WashMode.BABY, action.getWashMode());
        Assertions.assertNull(action.getCustomParams());
        Assertions.assertNotNull(action.getCreateDate());
        Assertions.assertNull(action.getUpdateDate());
    }
}
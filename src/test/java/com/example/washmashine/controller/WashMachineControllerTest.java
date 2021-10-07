package com.example.washmashine.controller;

import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.common.MachineStatus;
import com.example.washmachine.entity.WashMachine;
import org.apache.commons.lang3.BooleanUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.washmachine.controller.Paths.WASH_MACHINE_BASE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Sql(value = {"/wash-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class WashMachineControllerTest extends AbstractTest{

    @Test
    @Order(1)
    void nonMachineTest() throws Exception{
        MvcResult result = mockMvc.perform(get(WASH_MACHINE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashMachine[] list = mapFromJson(result.getResponse().getContentAsString(), WashMachine[].class);
        Assertions.assertEquals(0, list.length);
    }

    @Test
    @Order(2)
    void insertDeleteMachineTest() throws Exception{
        WashMachineDto dto = new WashMachineDto();
        dto.setName(machineName);
        dto.setStatus(MachineStatus.ACTIVE.toString());

        MvcResult result = mockMvc.perform(post(WASH_MACHINE_BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(dto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashMachine machine = mapFromJson(result.getResponse().getContentAsString(), WashMachine.class);

        Assertions.assertNotNull(machine);
        Assertions.assertNotNull(machine.getMachineId());
        Assertions.assertEquals(machineName, machine.getName());
        Assertions.assertEquals(MachineStatus.ACTIVE, machine.getStatus());


        result = mockMvc.perform(get(WASH_MACHINE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashMachine[] list = mapFromJson(result.getResponse().getContentAsString(), WashMachine[].class);
        Assertions.assertEquals(1, list.length);


        result = mockMvc.perform(delete(WASH_MACHINE_BASE_PATH + machine.getMachineId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Assertions.assertTrue(BooleanUtils.toBoolean(result.getResponse().getContentAsString()));


        result = mockMvc.perform(get(WASH_MACHINE_BASE_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        list = mapFromJson(result.getResponse().getContentAsString(), WashMachine[].class);
        Assertions.assertEquals(0, list.length);
    }

    @Test
    @Order(3)
    void upsertMachineTest() throws Exception{

        WashMachineDto dto = new WashMachineDto();
        dto.setName(machineName);
        dto.setStatus(MachineStatus.INACTIVE.toString());

        MvcResult result = mockMvc.perform(
                post(WASH_MACHINE_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(dto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        WashMachine washMachine = mapFromJson(result.getResponse().getContentAsString(), WashMachine.class);
        Assertions.assertNotNull(washMachine);
        Assertions.assertNotNull(washMachine.getCreateDate());
        Assertions.assertEquals(machineName, washMachine.getName());
        Assertions.assertEquals(MachineStatus.INACTIVE, washMachine.getStatus());


        dto.setMachineId(washMachine.getMachineId());
        dto.setName(machineName + " new ");
        dto.setStatus(MachineStatus.ACTIVE.name());

        result = mockMvc.perform(
                post(WASH_MACHINE_BASE_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapToJson(dto)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        washMachine = mapFromJson(result.getResponse().getContentAsString(), WashMachine.class);
        Assertions.assertNotNull(washMachine);
        Assertions.assertEquals(machineName + " new ", washMachine.getName());
        Assertions.assertEquals(MachineStatus.ACTIVE, washMachine.getStatus());
//        Assertions.assertNotNull(washMachine.getCreateDate());
        Assertions.assertNotNull(washMachine.getUpdateDate());
//        Assertions.assertTrue(washMachine.getCreateDate().before(washMachine.getUpdateDate()));
    }
}
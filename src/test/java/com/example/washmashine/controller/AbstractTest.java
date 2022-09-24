package com.example.washmashine.controller;

import java.io.IOException;

import com.example.washmachine.WashMachineServerApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WashMachineServerApplication.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public abstract class AbstractTest {

    protected static String machineName = "My wash machine";
    @Autowired
    protected MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    protected String mapToJson(Object obj) throws JsonProcessingException {
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz) throws IOException {

        return objectMapper.readValue(json, clazz);
    }
}

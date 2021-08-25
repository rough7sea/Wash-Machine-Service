package com.example.washmachine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WashParams {
    private String actionId;
    private int spinPower;
    private int rinsesCount;
    private int temperature;
    private String powder;
    private String conditioner;

    public WashParams(int spinPower, int rinsesCount, int temperature, String powder, String conditioner) {
        this.spinPower = spinPower;
        this.rinsesCount = rinsesCount;
        this.temperature = temperature;
        this.powder = powder;
        this.conditioner = conditioner;
    }
}

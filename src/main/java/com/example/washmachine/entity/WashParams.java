package com.example.washmachine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WashParams {

    @Id
    @Column(name = "action_id")
    private Long actionId;

    @MapsId
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "wash_action", foreignKey = @ForeignKey(name = "FK_params_to_action"))
    private WashAction action;

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

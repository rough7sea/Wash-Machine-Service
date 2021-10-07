package com.example.washmachine.entity;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.washmachine.common.MachineStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WashMachine extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long machineId;

    private String name;
    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    @JsonIgnore
    @OneToMany(mappedBy = "machine", fetch = FetchType.LAZY)
    private List<WashAction> actions;
}

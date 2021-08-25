package com.example.washmachine.entity;

import java.sql.Timestamp;

import com.example.washmachine.common.MachineStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WashMachine {
    private String id;
    private String name;
//    private Description description;
//    private Accessories accessories;
    private Timestamp updateDate;
    private Timestamp createDate;
    private MachineStatus status;
}

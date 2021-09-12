package com.example.washmachine.entity;

import java.sql.Timestamp;

import com.example.washmachine.common.WashActionStatus;
import com.example.washmachine.common.WashMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WashAction {
    private String actionId;
    private String machineId;

    private WashMode washMode;
    private WashActionStatus status;

    private Timestamp updateDate;
    private Timestamp startDate;

    private WashParams customParams;
}

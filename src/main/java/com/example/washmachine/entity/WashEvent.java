package com.example.washmachine.entity;

import java.sql.Timestamp;

import com.example.washmachine.common.WashStep;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WashEvent {
    private String actionId;
    private WashStep step;
    private Timestamp startDate;
}

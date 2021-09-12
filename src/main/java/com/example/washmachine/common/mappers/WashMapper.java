package com.example.washmachine.common.mappers;

import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.api.dto.WashEventDto;
import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.entity.WashAction;
import com.example.washmachine.entity.WashEvent;
import com.example.washmachine.entity.WashMachine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WashMapper {

    WashMapper INSTANCE = Mappers.getMapper(WashMapper.class);

    @Mapping(target = "id", defaultExpression = "java(java.util.UUID.randomUUID().toString())")
    WashMachine toWashMachine(WashMachineDto washMachineDto);

    @Mapping(target = "actionId", defaultExpression = "java(java.util.UUID.randomUUID().toString())")
    WashAction toWashAction(WashActionDto dto);

    WashEvent toWashEvent(WashEventDto dto);
}

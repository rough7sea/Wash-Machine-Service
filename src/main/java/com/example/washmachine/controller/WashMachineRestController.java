package com.example.washmachine.controller;

import java.util.List;

import com.example.washmachine.api.dto.WashMachineDto;
import com.example.washmachine.entity.WashMachine;
import com.example.washmachine.service.WashMachineService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.washmachine.controller.Paths.WASH_MACHINE_BASE_PATH;

@RestController
@RequestMapping(WASH_MACHINE_BASE_PATH)
public class WashMachineRestController {

    private final WashMachineService washMachineService;

    public WashMachineRestController(WashMachineService washMachineService) {
        this.washMachineService = washMachineService;
    }

    /**
     * Receive all machines.
     *
     * @return
     */
    @GetMapping
    public List<WashMachine> getAllWashMachine(){
        return washMachineService.list();
    }

    /**
     * Update or add information about machine.
     *
     * @param dto
     * @return
     */
    @PostMapping
    public WashMachine upsertWashMachine(@RequestBody WashMachineDto dto){
        return washMachineService.upsertWashMachine(dto);
    }

    /**
     * Get current information about wash machine.
     *
     * @param machineId
     * @return
     */
    @GetMapping("{machineId}")
    public WashMachine getWashMachine(@PathVariable Long machineId){
        return washMachineService.getWashMachine(machineId);
    }

    /**
     * Delete wash machine. Actions and events deleted cascade.
     *
     * @param machineId
     * @return
     */
    @DeleteMapping("{machineId}")
    public Boolean deleteWashMachine(@PathVariable Long machineId){
        return washMachineService.deleteWashMachine(machineId);
    }
}

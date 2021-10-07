package com.example.washmachine.controller;

import java.util.List;

import com.example.washmachine.entity.WashAction;
import com.example.washmachine.api.dto.WashActionDto;
import com.example.washmachine.service.WashActionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.washmachine.controller.Paths.WASH_ACTON_BASE_PATH;

@RestController
@RequestMapping(WASH_ACTON_BASE_PATH)
public class WashActionRestController {

    private final WashActionService actionService;

    public WashActionRestController(WashActionService actionService) {
        this.actionService = actionService;
    }

    /**
     * Receive all actions.
     * @return
     */
    @GetMapping
    public List<WashAction> list(){
        return actionService.list();
    }

    /**
     * Start, stop, cancel washing
     *
     * @param actionDto
     * @return
     */
    @PostMapping
    public WashAction upsertWashAction(@RequestBody WashActionDto actionDto){
        return actionService.upsertWashAction(actionDto);
    }

    /**
     * Get current action state.
     *
     * @param actionId
     * @return
     */
    @GetMapping("{actionId}")
    public WashAction getActionById(@PathVariable Long actionId){
        return actionService.getWashAction(actionId);
    }

    /**
     * Get all action for machine.
     *
     * @param machineId
     * @return
     */
    @GetMapping("machine/{machineId}")
    public List<WashAction> getActionsByMachineId(@PathVariable Long machineId){
        return actionService.getWashActionByMachineId(machineId);
    }

}

package com.yaod.workflow.engine.core.controller.health;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yaod
 **/
@RestController
@RequestMapping("health")
public class HealthCheckController {

    @Operation(tags = {"health"})
    @GetMapping("")
    public String health(){
        return "UP";
    }
}

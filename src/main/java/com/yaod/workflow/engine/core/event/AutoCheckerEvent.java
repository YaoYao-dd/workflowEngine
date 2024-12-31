package com.yaod.workflow.engine.core.event;

import lombok.Data;

/**
 * @author Yaod
 **/
@Data
public class AutoCheckerEvent extends Event{
    private String eid;
    private String autoChecker;
    private String workitemId;
    private String itemStepId;
    private String operator;

}

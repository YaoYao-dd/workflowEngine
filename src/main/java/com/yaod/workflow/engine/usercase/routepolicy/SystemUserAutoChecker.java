package com.yaod.workflow.engine.usercase.routepolicy;

import com.yaod.workflow.engine.core.domain.WorkItemStep;
import com.yaod.workflow.engine.core.domain.route.AutoChecker;

/**
 * @author Yaod
 **/
public class SystemUserAutoChecker implements AutoChecker {
    @Override
    public boolean check(WorkItemStep currentStep) {
        System.out.println("default auto checker completed successfully.");
        return true;
    }
}

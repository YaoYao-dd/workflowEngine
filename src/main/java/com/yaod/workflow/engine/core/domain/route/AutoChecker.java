package com.yaod.workflow.engine.core.domain.route;

import com.yaod.workflow.engine.core.domain.WorkItemStep;

/**
 * @author Yaod
 * process for the system owner.
 **/
public interface AutoChecker {
    /**
     * @param currentStep
     * @return return true, the check is successful, process current step , otherwise send back to previous step.
     */
    boolean check(WorkItemStep currentStep);
}

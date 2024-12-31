package com.yaod.workflow.engine.core.domain.route;

import com.yaod.workflow.engine.core.domain.WorkItemStep;

import java.util.List;
/**
 * determine the next step for a workitem,  can be multiple instance after on action on current step.
 * example might be, when an absence request raised, the request can submit it, then route to manager for approval.
 * @author Yaod
 **/
public interface RoutePolicy {

    /**
     * when return null or empty list, it means the workflow item is done.
     * @param step
     * @return
     */
    List<WorkItemStep> processForNext(WorkItemStep step);
}

package com.yaod.workflow.engine.usercase.routepolicy;

import com.yaod.workflow.engine.core.domain.WorkItemStep;
import com.yaod.workflow.engine.core.domain.route.RoutePolicy;

import java.util.Collections;
import java.util.List;

/**
 * Dummy route policy
 * @author Yaod
 **/
public class Case1DefaultRoutePolicy implements RoutePolicy {
    @Override
    public List<WorkItemStep> processForNext(WorkItemStep step) {
        if("yao".equals(step.getOwner())) {
            WorkItemStep nextStep = step.initNextStep();
            nextStep.setOwner("dummyOwner");
            return List.of(nextStep);
        }else if("dummyOwner".equals(step.getOwner())){
            WorkItemStep nextStep = step.initNextStep();
            nextStep.setOwner("SystemUser");
            nextStep.setAutoChecker(SystemUserAutoChecker.class.getName());
            return List.of(nextStep);
        }else{

            return Collections.emptyList();//
        }
    }
}

package com.yaod.workflow.engine.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaod.workflow.engine.core.domain.route.RoutePolicy;
import com.yaod.workflow.engine.core.domain.utils.Utils;
import com.yaod.workflow.engine.usercase.routepolicy.Case1DefaultRoutePolicy;
import com.yaod.workflow.engine.core.domain.enums.ItemStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Yaod
 **/

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkItem {

    private String workItemId;
    private String name;

    private String creator;
    private Date lastUpdateTs;
    private Date dueDate;
    private ItemStatus status=ItemStatus.PENDING;
    private String memo;

    private String routePolicy= Case1DefaultRoutePolicy.class.getName();
    private String routePolicyMeta="";

    private List<WorkItemStep> steps;

    public static WorkItem newInst() {
        var item= new WorkItem();
        item.setWorkItemId(newWorkItemId());
        return item;
    }
    public WorkItemStep newStep(String owner){
        var wis=WorkItemStep.newInst();
        wis.setWorkItemId(workItemId);
        wis.setStatus(ItemStatus.PENDING);
        wis.setOwner(owner);
        return wis;
    }

    public List<WorkItemStep> nextStepsProcessedAfter(WorkItemStep currentStep){
        RoutePolicy routePolicy=retrieveRoutePolicy();
        var nextSteps = routePolicy.processForNext(currentStep);
        nextSteps.forEach(x->x.setPreSid(currentStep.getStepId()));
        return nextSteps;
    }

    private RoutePolicy retrieveRoutePolicy(){
        return cache.computeIfAbsent(this.routePolicy, k->{
            try {
                return (RoutePolicy) Class.forName(k).newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    static Map<String, RoutePolicy> cache=new ConcurrentHashMap<>();
    private static String newWorkItemId(){
        return "workItem-"+ Utils.newUlId();
    }
}


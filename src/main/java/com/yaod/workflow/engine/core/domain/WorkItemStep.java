package com.yaod.workflow.engine.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yaod.workflow.engine.core.domain.enums.ItemStatus;
import com.yaod.workflow.engine.core.domain.utils.Utils;
import lombok.Data;

import java.util.Date;

/**
 * @author Yaod
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorkItemStep {

    private String stepId;
    private String workItemId;
    private String owner;
    private Date receivedTs;
    private Date processedTs;
    private Date dueDate;
    private ItemStatus status;
    private String memo;
    private String preSid;

    private String autoChecker;

    public static WorkItemStep newInst() {
        var step= new WorkItemStep();
        step.setStepId(newStepId());
        return step;
    }

    public WorkItemStep initNextStep(){
        var nextStep=WorkItemStep.newInst();
        nextStep.setWorkItemId(this.getWorkItemId());
        nextStep.setPreSid(this.getStepId());
        nextStep.setStatus(ItemStatus.PENDING);
        return nextStep;
    }

    public void process(String memo){
        this.memo=memo;
        this.status=ItemStatus.PROCESSED;
        this.processedTs=new Date();
    }

    private static String newStepId(){
        return "step-"+ Utils.newUlId();
    }
}

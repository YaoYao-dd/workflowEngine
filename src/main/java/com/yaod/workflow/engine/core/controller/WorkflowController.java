package com.yaod.workflow.engine.core.controller;

import com.yaod.workflow.engine.core.domain.WorkItem;
import com.yaod.workflow.engine.core.domain.WorkItemStep;
import com.yaod.workflow.engine.core.service.WorkflowMgmtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Yaod
 **/
@RestController
@RequestMapping("workflow")
public class WorkflowController {

    WorkflowMgmtService workflowMgmtService;

    public WorkflowController(WorkflowMgmtService workflowMgmtService) {
        this.workflowMgmtService = workflowMgmtService;
    }

    @Operation(tags={"workitem"},description="show workitem by workitemId.", summary = "show workitem detail by Id")
    @GetMapping("{workitemId}")
    public WorkItem getItemById(@PathVariable("workitemId") String workItemId){
        return workflowMgmtService.getEnrichedItemById(workItemId);
    }

    @Operation(tags={"workitem"},description="show workitems created by operator.", summary = "show workitems created by operator.")
    @GetMapping("{operator}/workitems")
    public List<WorkItem> itemsCreatedBy(@PathVariable("operator") String operator){
        return workflowMgmtService.workItemsCreatedBy(operator);
    }

//    @Operation(tags={"workitem"},description="start a new workitem with current operator and item name," +
//            "we should not use this, because mostly we need associate a biz object.", summary = "start a new workitem.")
//    @PostMapping("{operator}/items")
//    public WorkItem startNewWorkItem( @PathVariable("operator") String creator, @RequestHeader("itemName") String itemName){
//        return workflowMgmtService.startNewWorkItem(itemName, creator);
//    }

    //consider pagination is needed.
    @Operation(tags={"workitem step"},description="show all item steps which is pending on specified operator", summary = "show pending itemstep for an operator.")
    @GetMapping("{operator}/pendingItemSteps")
    public List<WorkItemStep> getPendingItemStepBy(@PathVariable("operator") String creator){
        return workflowMgmtService.getPendingItemStepByOwner(creator);
    }

    @Operation(tags={"workitem step"},description="process item step, mark it processed, and move over to next steps.", summary = "process workitem step.")
    @PatchMapping("{operator}/pendingItemSteps/{stepId}/process")
    public boolean processItemStepBy(@PathVariable("operator") String operator, @PathVariable("stepId") String stepId,@RequestBody String momo){
         return workflowMgmtService.processItemStep(stepId,operator, momo);
    }

    @Operation(tags={"workitem step"},description="reject item step, mark it failed.", summary = "reject workitem step.")
    @PatchMapping("{operator}/pendingItemSteps/{stepId}/reject")
    public boolean rejectItemStepBy(@PathVariable("operator") String operator, @PathVariable("stepId") String stepId,@RequestBody String momo){
        return workflowMgmtService.rejectItemStep(stepId,operator, momo);
    }

    @Operation(tags={"workitem step"},description="show all item steps within a specified workitem.", summary = "all item steps under a workitem.")
    @GetMapping("{workitem}/steps")
    public List<WorkItemStep> getStepsByWorkItemId(@PathVariable("workitem") String workitemId){
        return workflowMgmtService.getItemStepByWorkItemId(workitemId);
    }
}

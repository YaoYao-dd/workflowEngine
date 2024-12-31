package com.yaod.workflow.engine.usercase.controller;

import com.yaod.workflow.engine.core.domain.WorkItem;
import com.yaod.workflow.engine.usercase.domain.Case1Vo;
import com.yaod.workflow.engine.usercase.service.Case1WorkflowMgmtService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yaod
 **/
@RestController
@RequestMapping("biz")
public class Case1WorkflowController {

    Case1WorkflowMgmtService workflowMgmtService;

    public Case1WorkflowController(Case1WorkflowMgmtService workflowMgmtService) {
        this.workflowMgmtService = workflowMgmtService;
    }


    @Operation(tags={"workitem"},description="start a new workitem with current operator , item desc and associated biz obj", summary = "start a new workitem with biz object associated.")
    @PostMapping("{operator}/items")
    public WorkItem startNewWorkItem(@PathVariable("operator") String creator, @RequestHeader("workitemName") String workitemName, @RequestBody Case1Vo case1Vo){
        return workflowMgmtService.startNewWorkItemForBiz(workitemName, creator, case1Vo);
    }

    @Operation(tags={"workitem"},description="show associated Biz Object by workitemId.", summary = "show associated biz Object  by workitemId")
    @GetMapping("{workitemId}/associatedObj")
    public Case1Vo getAssociatedBizObjByWorkItemId(@PathVariable("workitemId") String workItemId){
        return workflowMgmtService.getAssociateObjByWorkItemId(workItemId);
    }

}

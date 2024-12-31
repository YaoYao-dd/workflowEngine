package com.yaod.workflow.engine.core.autoprocess;

import com.yaod.workflow.engine.core.domain.route.AutoChecker;
import com.yaod.workflow.engine.core.event.Event;
import com.yaod.workflow.engine.usercase.routepolicy.Case1DefaultRoutePolicy;
import com.yaod.workflow.engine.core.domain.enums.ItemStatus;
import com.yaod.workflow.engine.core.event.AutoCheckerEvent;
import com.yaod.workflow.engine.core.repository.WorkItemMapper;
import com.yaod.workflow.engine.core.service.WorkflowMgmtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author Yaod
 *
 * event listener which subscribe autoapprove action from system user.
 * basically workitemStep has non-empty value in 'AutoChecker' property.
 *
 * @see Case1DefaultRoutePolicy for example.
 **/
@Component
public class SystemActionChecker {

    Logger LOGGER = LoggerFactory.getLogger(SystemActionChecker.class);

    WorkItemMapper workItemMapper;
    WorkflowMgmtService workflowMgmtService;

    public SystemActionChecker(WorkItemMapper workItemMapper, WorkflowMgmtService aWorkflowMgmtService) {
        this.workItemMapper = workItemMapper;
        this.workflowMgmtService=aWorkflowMgmtService;
    }

    @EventListener
    void checkSystemEvent(Event e) {
        LOGGER.debug("new autoChecker event arrived. "+e.toString());
        if (e instanceof AutoCheckerEvent ace) {
            executeAutoChecker(ace);
        }

    }

    private void executeAutoChecker(AutoCheckerEvent ace) {
        var autoCheckStatus= ItemStatus.FAIL;
        var memo="";
        String itemStepId = ace.getItemStepId();
        var step = workItemMapper.findItemStepFor(itemStepId);
        try {

            var autoCheckerMeta = ace.getAutoChecker();
            AutoChecker autoChecker = (AutoChecker)Class.forName(autoCheckerMeta).newInstance();
            var result=autoChecker.check(step);
            autoCheckStatus=result?ItemStatus.PROCESSED:ItemStatus.FAIL;
            memo=result?"auto-approved.":"auto check failed.";
        } catch (Exception exec) {
            memo=exec.getMessage();
        }
        if(autoCheckStatus==ItemStatus.PROCESSED){
            workflowMgmtService.processItemStep(itemStepId, ace.getOperator(), memo);
        }else{
            workflowMgmtService.rejectItemStep(itemStepId, ace.getOperator(),memo, autoCheckStatus);
        }
    }
}

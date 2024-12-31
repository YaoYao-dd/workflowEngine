package com.yaod.workflow.engine.core.service;

import com.yaod.workflow.engine.core.domain.WorkItem;
import com.yaod.workflow.engine.core.domain.WorkItemStep;
import com.yaod.workflow.engine.core.event.EventPublisher;
import com.yaod.workflow.engine.core.event.AutoCheckerEvent;
import com.yaod.workflow.engine.core.exception.ItemNotFoundExecption;
import com.yaod.workflow.engine.core.repository.WorkItemMapper;
import com.yaod.workflow.engine.core.domain.enums.ItemStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author Yaod
 **/
@Service
public class WorkflowMgmtService {
    Logger LOGGER = LoggerFactory.getLogger(WorkflowMgmtService.class);
    private final WorkItemMapper workItemRepository;
    private final EventPublisher eventPublisher;

    public WorkflowMgmtService(WorkItemMapper aWorkItemRepository,@Qualifier("SpringbootPublisher") EventPublisher aEventPublisher) {
        this.workItemRepository=aWorkItemRepository;
        this.eventPublisher= aEventPublisher;
    }

    public WorkItem getEnrichedItemById(String workItemId) {
        return this.workItemRepository.findWorkItemBy(workItemId);
    }

    public List<WorkItem> workItemsCreatedBy(String creator) {
        return  this.workItemRepository.workItemCreatedBy(creator);
    }

    @Transactional
    public WorkItem startNewWorkItem(String itemName, String creator){
        LOGGER.debug("starting a new workitem...");
        WorkItem workItem=WorkItem.newInst();
        workItem.setName(itemName);
        workItem.setCreator(creator);
        workItem.setStatus(ItemStatus.PENDING);

        WorkItemStep wis=workItem.newStep(creator);

        workItemRepository.saveWorkItem(workItem);
        workItemRepository.saveWorkItemStep(wis);

        return workItem;
    }

    public List<WorkItemStep> getPendingItemStepByOwner(String owner) {
        return this.workItemRepository.listStepsGivenStatusForOwner(owner, ItemStatus.PENDING);
    }

    public List<WorkItemStep> getItemStepByWorkItemId(String workItemId) {
        return this.workItemRepository.listStepsGivenAnWorkItem(workItemId);
    }

    @Transactional
    public boolean processItemStep(String itemStepId, String operator, String memo){
        LOGGER.debug("process to approve the workitem step "+itemStepId);

        var currentStep = nonNullCheck(workItemRepository.findItemStepFor(itemStepId),itemStepId) ;

        String workItemId = currentStep.getWorkItemId();
        var currentWorkitem = nonNullCheck(workItemRepository.findWorkItemBy(workItemId), workItemId);
        currentStep.process(memo);
        var nextSteps = currentWorkitem.nextStepsProcessedAfter(currentStep);
        LOGGER.debug("next workitem steps are "+nextSteps);
        if(nextSteps==null || nextSteps.isEmpty()){
            currentWorkitem.setStatus(ItemStatus.FINISHED);
            workItemRepository.updateWorkItem(currentWorkitem);
        }else{
            nextSteps.forEach(workItemRepository::saveWorkItemStep);
            nextSteps.forEach(this::postProcessNextStep);
        }
        workItemRepository.updateStep(currentStep,operator,ItemStatus.PENDING);
        return true;
    }

    @Transactional
    public boolean rejectItemStep(String itemStepId,String operator, String memo, ItemStatus status){
        LOGGER.debug("process to reject the workitem step "+itemStepId);
        var currentStep = nonNullCheck(workItemRepository.findItemStepFor(itemStepId),itemStepId) ;

        String workItemId = currentStep.getWorkItemId();
        var currentWorkitem = nonNullCheck(workItemRepository.findWorkItemBy(workItemId), workItemId);

        currentWorkitem.setStatus(ItemStatus.FAIL);
        currentStep.setStatus(status);
        currentStep.setMemo(memo);
        workItemRepository.updateWorkItem(currentWorkitem);
        workItemRepository.updateStep(currentStep,operator,ItemStatus.PENDING);
        return true;
    }

    @Transactional
    public boolean rejectItemStep(String itemStepId,String operator, String memo){
        return this.rejectItemStep(itemStepId,operator,memo,ItemStatus.REJECTED);
    }

    private void postProcessNextStep(WorkItemStep nextStep) {
        if(Objects.nonNull(nextStep.getAutoChecker())){
            LOGGER.debug("sending event to auto check for system users."+nextStep);
            var autoChecker=nextStep.getAutoChecker();
            var e=new AutoCheckerEvent();
            e.setWorkitemId(nextStep.getWorkItemId());
            e.setItemStepId(nextStep.getStepId());
            e.setAutoChecker(autoChecker);
            e.setOperator(nextStep.getOwner());
            this.eventPublisher.publish(e);
        }
        return;
    }

    <T> T nonNullCheck(T item, String itemId){

        if(Objects.isNull(itemId)){
            throw  ItemNotFoundExecption.raiseNotFoundItemExec(itemId);
        }else{
            return item;
        }
    }


}

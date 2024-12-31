package com.yaod.workflow.engine.usercase.service;

import com.yaod.workflow.engine.core.domain.WorkItem;
import com.yaod.workflow.engine.usercase.domain.Case1Vo;
import com.yaod.workflow.engine.usercase.repository.Case1ItemDetailMapper;
import com.yaod.workflow.engine.core.service.WorkflowMgmtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Yaod
 **/
@Service
public class Case1WorkflowMgmtService {
    Logger LOGGER = LoggerFactory.getLogger(Case1WorkflowMgmtService.class);
    private final WorkflowMgmtService workItemSvc;

    private final Case1ItemDetailMapper itemDetailRepository;
    public Case1WorkflowMgmtService(WorkflowMgmtService aWorkItemSvc, Case1ItemDetailMapper aItemDetailRepository) {
        this.workItemSvc=aWorkItemSvc;
        this.itemDetailRepository=aItemDetailRepository;
    }

    public Case1Vo getAssociateObjByWorkItemId(String workItemId) {
        return this.itemDetailRepository.findWorkItemDetailBy(workItemId);
    }

    @Transactional
    public WorkItem startNewWorkItemForBiz(String itemName, String creator, Case1Vo case1Vo){
        var workItem= workItemSvc.startNewWorkItem(itemName,creator);
        itemDetailRepository.saveWorkItemDetail(workItem.getWorkItemId(),case1Vo);

        LOGGER.debug("started a new workitem: "+workItem.getWorkItemId());
        return workItem;
    }


    void checkOneRowImpacted(int rows, String message){
        if (rows!=1){
            throw new RuntimeException(message);
        }
    }


}

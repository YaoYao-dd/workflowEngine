package com.yaod.workflow.engine.core.repository;

import com.yaod.workflow.engine.core.domain.WorkItem;
import com.yaod.workflow.engine.core.domain.WorkItemStep;
import com.yaod.workflow.engine.core.domain.enums.ItemStatus;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * @author Yaod
 *
 * utilize mybatis as the peristence layer.
 **/
@Mapper
public interface WorkItemMapper {


    //can support nested query to include step while querying workitem by id.
    @Select("select workItemId,name,creator,lastupdatets,duedate,status  from workitem_tb where workItemId = #{workitemId} ")
    @Results(id = "workitemDetail", value = {
            @Result(property = "workItemId",column = "workItemId",
                    javaType = String.class, jdbcType = JdbcType.VARCHAR),
            @Result(property = "steps",column = "workItemId",
                    javaType = List.class,
                    many = @Many(select = "com.yaod.workflow.engine.core.repository.WorkItemMapper.listStepsGivenAnWorkItem", fetchType = FetchType.DEFAULT))
    })
    public WorkItem findWorkItemBy(@Param("workitemId") String workitemId);

    @Select("select workItemId,name,creator,lastupdatets,duedate,status,routePolicy, routePolicyMeta  from workitem_tb where creator = #{creator} ")
    public List<WorkItem> workItemCreatedBy(@Param("creator") String creator);


    @Insert("insert into workitem_tb(workItemId, name, creator,status,routePolicy, routePolicyMeta) values (#{item.workItemId},#{item.name},#{item.creator},#{item.status},#{item.routePolicy},#{item.routePolicyMeta})")
    int saveWorkItem(@Param("item") WorkItem item);

    @Update("update workitem_tb set status=#{item.status} where workItemId = #{item.workItemId}")
    int updateWorkItem(@Param("item") WorkItem item);

    @Insert("insert into workitem_step_tb(stepId, WORKITEMID, owner, PROCESSEDTS,preSid, status) values (#{step.stepId},#{step.workItemId},#{step.owner},#{step.processedTs}, #{step.preSid}, #{step.status})")
    int saveWorkItemStep(@Param("step") WorkItemStep itemStep);

    @Update("update workitem_step_tb set status=#{itemStep.status},memo=#{itemStep.memo},PROCESSEDTS=#{itemStep.processedTs} where stepId = #{itemStep.stepId} and owner=  #{operator} and status=#{oldStatus}")
    public int updateStep(@Param("itemStep") WorkItemStep itemStep,@Param("operator") String operator, @Param("oldStatus") ItemStatus oldStatus);

    @Select("select stepId,name,owner, workItemId,RECEIVEDTS,PROCESSEDTS,DUEDATE,status  from workitem_step_tb where  stepId = #{stepId}")
    public WorkItemStep findItemStepFor(@Param("stepId") String stepId);

    @Select("select stepId,name,owner, workItemId,RECEIVEDTS,PROCESSEDTS,DUEDATE,status  from workitem_step_tb where owner = #{operator} and status=#{status}")
    public List<WorkItemStep> listStepsGivenStatusForOwner(@Param("operator") String operator, @Param("status") ItemStatus status);

    @Select("select stepId,name,owner, workItemId,RECEIVEDTS,PROCESSEDTS,DUEDATE,status  from workitem_step_tb where  workItemId=#{workitemId} order by RECEIVEDTS desc")
    public List<WorkItemStep> listStepsGivenAnWorkItem(@Param("workitemId") String workitemId);

}

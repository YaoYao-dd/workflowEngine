package com.yaod.workflow.engine.usercase.repository;

import com.yaod.workflow.engine.usercase.domain.Case1Vo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Yaod
 *
 * utilize mybatis as the peristence layer.
 **/
@Mapper
public interface Case1ItemDetailMapper {

    @Select("select workItemId, bid, payload from case1_tb where workItemId =#{itemId}")
    public Case1Vo findWorkItemDetailBy(@Param("itemId") String workitemId);

    @Insert("insert into case1_tb(workItemId, bid, payload) values (#{itemId},#{vo.bid},#{vo.payload})")
    int saveWorkItemDetail(@Param("itemId") String itemId, @Param("vo") Case1Vo vo);
}

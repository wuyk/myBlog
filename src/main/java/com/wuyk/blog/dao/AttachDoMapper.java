package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.AttachDo;
import com.wuyk.blog.pojo.vo.AttachVo;

import java.util.List;

public interface AttachDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AttachDo record);

    int insertSelective(AttachDo record);

    AttachDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttachDo record);

    int updateByPrimaryKey(AttachDo record);

    long countByExample(AttachVo example);

    List<AttachDo> selectByExample(AttachVo example);

}
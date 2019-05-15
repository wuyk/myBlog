package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.LogsDo;
import com.wuyk.blog.pojo.vo.LogsVo;

import java.util.List;

public interface LogsDoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LogsDo record);

    int insertSelective(LogsDo record);

    LogsDo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LogsDo record);

    int updateByPrimaryKey(LogsDo record);

    List<LogsDo> selectByExample(LogsVo example);
}
package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.Logs;

public interface LogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Logs record);

    int insertSelective(Logs record);

    Logs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Logs record);

    int updateByPrimaryKey(Logs record);
}
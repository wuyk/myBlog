package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.Options;

public interface OptionsMapper {
    int deleteByPrimaryKey(String name);

    int insert(Options record);

    int insertSelective(Options record);

    Options selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(Options record);

    int updateByPrimaryKey(Options record);
}
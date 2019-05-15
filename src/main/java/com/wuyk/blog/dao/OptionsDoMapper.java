package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.OptionsDo;

public interface OptionsDoMapper {
    int deleteByPrimaryKey(String name);

    int insert(OptionsDo record);

    int insertSelective(OptionsDo record);

    OptionsDo selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(OptionsDo record);

    int updateByPrimaryKey(OptionsDo record);
}
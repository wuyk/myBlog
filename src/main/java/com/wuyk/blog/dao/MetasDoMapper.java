package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.MetasDo;
import com.wuyk.blog.pojo.vo.MetasVo;

public interface MetasDoMapper {
    int deleteByPrimaryKey(Integer mid);

    int insert(MetasDo record);

    int insertSelective(MetasDo record);

    MetasDo selectByPrimaryKey(Integer mid);

    int updateByPrimaryKeySelective(MetasDo record);

    int updateByPrimaryKey(MetasDo record);

    long countByExample(MetasVo example);
}
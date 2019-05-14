package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.Metas;

public interface MetasMapper {
    int deleteByPrimaryKey(Integer mid);

    int insert(Metas record);

    int insertSelective(Metas record);

    Metas selectByPrimaryKey(Integer mid);

    int updateByPrimaryKeySelective(Metas record);

    int updateByPrimaryKey(Metas record);
}
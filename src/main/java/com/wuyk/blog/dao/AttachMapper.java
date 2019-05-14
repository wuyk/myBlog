package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.Attach;

public interface AttachMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Attach record);

    int insertSelective(Attach record);

    Attach selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Attach record);

    int updateByPrimaryKey(Attach record);
}
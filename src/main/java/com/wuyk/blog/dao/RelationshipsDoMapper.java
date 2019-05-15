package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.RelationshipsDoKey;

public interface RelationshipsDoMapper {
    int deleteByPrimaryKey(RelationshipsDoKey key);

    int insert(RelationshipsDoKey record);

    int insertSelective(RelationshipsDoKey record);
}
package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.RelationshipsKey;

public interface RelationshipsMapper {
    int deleteByPrimaryKey(RelationshipsKey key);

    int insert(RelationshipsKey record);

    int insertSelective(RelationshipsKey record);
}
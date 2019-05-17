package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.RelationshipsDoKey;
import com.wuyk.blog.pojo.vo.RelationshipsVo;

import java.util.List;

public interface RelationshipsDoMapper {
    int deleteByPrimaryKey(RelationshipsDoKey key);

    int insert(RelationshipsDoKey record);

    int insertSelective(RelationshipsDoKey record);

    long countByExample(RelationshipsVo example);

    int deleteByExample(RelationshipsVo example);

    List<RelationshipsDoKey> selectByExample(RelationshipsVo example);
}
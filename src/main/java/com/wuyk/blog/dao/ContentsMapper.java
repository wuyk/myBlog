package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.Contents;

public interface ContentsMapper {
    int deleteByPrimaryKey(Integer cid);

    int insert(Contents record);

    int insertSelective(Contents record);

    Contents selectByPrimaryKey(Integer cid);

    int updateByPrimaryKeySelective(Contents record);

    int updateByPrimaryKeyWithBLOBs(Contents record);

    int updateByPrimaryKey(Contents record);
}
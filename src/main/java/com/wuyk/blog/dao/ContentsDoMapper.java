package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.vo.ContentsVo;

import java.util.List;

public interface ContentsDoMapper {
    int deleteByPrimaryKey(Integer cid);

    int insert(ContentsDo record);

    int insertSelective(ContentsDo record);

    ContentsDo selectByPrimaryKey(Integer cid);

    int updateByPrimaryKeySelective(ContentsDo record);

    int updateByPrimaryKeyWithBLOBs(ContentsDo record);

    int updateByPrimaryKey(ContentsDo record);

    List<ContentsDo> selectByExampleWithBLOBs(ContentsVo example);

    List<ContentsDo> selectByExample(ContentsVo example);

    long countByExample(ContentsVo example);
}
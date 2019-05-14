package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.Comments;

public interface CommentsMapper {
    int deleteByPrimaryKey(Integer coid);

    int insert(Comments record);

    int insertSelective(Comments record);

    Comments selectByPrimaryKey(Integer coid);

    int updateByPrimaryKeySelective(Comments record);

    int updateByPrimaryKeyWithBLOBs(Comments record);

    int updateByPrimaryKey(Comments record);
}
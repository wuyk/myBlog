package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.UsersDo;

public interface UsersDoMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(UsersDo record);

    int insertSelective(UsersDo record);

    UsersDo selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(UsersDo record);

    int updateByPrimaryKey(UsersDo record);
}
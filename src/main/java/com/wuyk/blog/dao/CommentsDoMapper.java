package com.wuyk.blog.dao;

import com.wuyk.blog.pojo.CommentsDo;
import com.wuyk.blog.pojo.vo.CommentsVo;

import java.util.List;

public interface CommentsDoMapper {
    int deleteByPrimaryKey(Integer coid);

    int insert(CommentsDo record);

    int insertSelective(CommentsDo record);

    CommentsDo selectByPrimaryKey(Integer coid);

    int updateByPrimaryKeySelective(CommentsDo record);

    int updateByPrimaryKeyWithBLOBs(CommentsDo record);

    int updateByPrimaryKey(CommentsDo record);

    List<CommentsDo> selectByExampleWithBLOBs(CommentsVo example);

    long countByExample(CommentsVo example);
}
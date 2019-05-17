package com.wuyk.blog.service;

import com.wuyk.blog.pojo.RelationshipsDoKey;

import java.util.List;

/**
 * Created by wuyk
 */
public interface IRelationshipsService {

    /**
     * 按住键删除
     * @param cid
     * @param mid
     */
    void deleteById(Integer cid, Integer mid);

    /**
     * 按主键统计条数
     * @param cid
     * @param mid
     * @return 条数
     */
    Long countById(Integer cid, Integer mid);


    /**
     * 保存對象
     * @param relationshipsDoKey
     */
    void insertVo(RelationshipsDoKey relationshipsDoKey);

    /**
     * 根据id搜索
     * @param cid
     * @param mid
     * @return
     */
    List<RelationshipsDoKey> getRelationshipById(Integer cid, Integer mid);
}

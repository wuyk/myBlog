package com.wuyk.blog.service;

import com.wuyk.blog.pojo.MetasDo;

import java.util.List;

/**
 * Created by wuyk
 */
public interface IMetasService {

    /**
     * 根据类型查询项目列表
     * @param types
     * @return
     */
    List<MetasDo> getMetas(String types);

    /**
     * 保存多个项目
     * @param cid
     * @param names
     * @param type
     */
    void saveMetas(Integer cid, String names, String type);

    /**
     * 保存项目
     * @param type
     * @param name
     * @param mid
     */
    void saveMeta(String type, String name, Integer mid);
    /**
     * 保存项目
     * @param metasDo
     */
    void saveMeta(MetasDo metasDo);

    /**
     * 更新项目
     * @param metasDo
     */
    void update(MetasDo metasDo);

    /**
     * 删除项目
     * @param mid
     */
    void delete(int mid);

}

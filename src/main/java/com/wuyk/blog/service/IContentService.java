package com.wuyk.blog.service;

import com.github.pagehelper.PageInfo;
import com.wuyk.blog.pojo.ContentsDo;

/**
 * Created by wuyk
 */
public interface IContentService {

    /**
     *查询文章返回多条数据
     * @param p 当前页
     * @param limit 每页条数
     * @return ContentVo
     */
    PageInfo<ContentsDo> getContents(Integer p, Integer limit);
}

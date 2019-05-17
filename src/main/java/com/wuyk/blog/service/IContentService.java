package com.wuyk.blog.service;

import com.github.pagehelper.PageInfo;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.vo.ContentsVo;

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

    /**
     * @param contentsVo
     * @param page
     * @param limit
     * @return
     */
    PageInfo<ContentsDo> getArticlesWithpage(ContentsVo contentsVo, Integer page, Integer limit);

    /**
     * 根据id或slug获取文章
     *
     * @param id id
     * @return ContentVo
     */
    ContentsDo getContents(String id);

    /**
     * 发布文章
     * @param contentsDo
     */
    String publish(ContentsDo contentsDo);

    /**
     * 根据文章id删除
     * @param cid
     */
    String deleteByCid(Integer cid);

    /**
     * 编辑文章
     * @param contentsDo
     */
    String updateArticle(ContentsDo contentsDo);

    /**
     * 更新原有文章的category
     * @param ordinal
     * @param newCatefory
     */
    void updateCategory(String ordinal,String newCatefory);

    /**
     * 根据主键更新
     * @param contentsDo
     */
    void updateContentByCid(ContentsDo contentsDo);
}

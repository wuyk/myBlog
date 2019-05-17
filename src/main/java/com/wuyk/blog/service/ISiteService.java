package com.wuyk.blog.service;

import com.wuyk.blog.pojo.CommentsDo;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.bo.BackResponseBo;
import com.wuyk.blog.pojo.bo.StatisticsBo;

import java.util.List;

/**
 * 站点服务
 */
public interface ISiteService {

    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
    List<CommentsDo> recentComments(int limit);

    /**
     * 最新发表的文章
     * @param limit
     * @return
     */
    List<ContentsDo> recentContents(int limit);

    /**
     * 获得后台统计数据
     * @return
     */
    StatisticsBo getStatistics();

    /**
     * 系统备份
     * @param bk_type
     * @param bk_path
     * @param fmt
     * @return
     */
    BackResponseBo backup(String bk_type, String bk_path, String fmt) throws Exception;
}

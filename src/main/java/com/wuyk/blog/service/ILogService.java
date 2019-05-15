package com.wuyk.blog.service;

import com.wuyk.blog.pojo.LogsDo;

import java.util.List;

/**
 * 日志
 */
public interface ILogService {

    /**
     * 获取日志分页
     * @param page 当前页
     * @param limit 每页条数
     * @return 日志
     */
    List<LogsDo> getLogs(int page, int limit);

    /**
     *  保存
     * @param action
     * @param data
     * @param ip
     * @param authorId
     */
    void insertLog(String action, String data, String ip, Integer authorId);
}

package com.wuyk.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.dao.LogsDoMapper;
import com.wuyk.blog.pojo.LogsDo;
import com.wuyk.blog.pojo.vo.LogsVo;
import com.wuyk.blog.service.ILogService;
import com.wuyk.blog.utils.DateKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class LogServiceImpl implements ILogService {

    @Resource
    private LogsDoMapper logsDoMapper;

    @Override
    public List<LogsDo> getLogs(int page, int limit) {
        if (page <= 0) {
            page = 1;
        }
        if (limit < 1 || limit > WebConst.MAX_POSTS) {
            limit = 10;
        }
        LogsVo logsVo = new LogsVo();
        logsVo.setOrderByClause("id desc");
        PageHelper.startPage((page - 1) * limit, limit);
        return logsDoMapper.selectByExample(logsVo);
    }

    @Override
    public void insertLog(String action, String data, String ip, Integer authorId) {
        LogsDo logs = new LogsDo();
        logs.setAction(action);
        logs.setData(data);
        logs.setIp(ip);
        logs.setAuthorId(authorId);
        logs.setCreated(DateKit.getCurrentUnixTime());
        logsDoMapper.insert(logs);
    }
}

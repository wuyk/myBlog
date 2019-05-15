package com.wuyk.blog.controller.admin;

import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.CommentsDo;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.LogsDo;
import com.wuyk.blog.pojo.bo.StatisticsBo;
import com.wuyk.blog.service.ILogService;
import com.wuyk.blog.service.ISiteService;
import com.wuyk.blog.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 后台管理首页
 */
@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController {

    @Resource
    private ISiteService siteService;

    @Resource
    private ILogService logService;

    @Resource
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request) {
        logger.info("进入admin管理后台");
        List<CommentsDo> comments = siteService.recentComments(5);
        List<ContentsDo> contents = siteService.recentContents(5);
        StatisticsBo statisticsBo = siteService.getStatistics();
        // 取最新的20条日志
        List<LogsDo> logs = logService.getLogs(1, 5);

        request.setAttribute("comments", comments);
        request.setAttribute("articles", contents);
        request.setAttribute("statistics", statisticsBo);
        request.setAttribute("logs", logs);
        return "admin/index";
    }
}

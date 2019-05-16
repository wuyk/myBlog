package com.wuyk.blog.controller.admin;

import com.wuyk.blog.constant.LogActionEnum;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.controller.BaseController;
import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.CommentsDo;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.LogsDo;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.pojo.bo.StatisticsBo;
import com.wuyk.blog.service.ILogService;
import com.wuyk.blog.service.ISiteService;
import com.wuyk.blog.service.IUserService;
import com.wuyk.blog.utils.GsonUtils;
import com.wuyk.blog.utils.RestResponse;
import com.wuyk.blog.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 后台管理首页
 */
@Controller("adminIndexController")
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class IndexController extends BaseController {

    @Resource
    private ISiteService siteService;

    @Resource
    private ILogService logService;

    @Resource
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

    @GetMapping(value = {"", "/index"})
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

    /**
     * 个人设置页面
     *
     * @return
     */
    @GetMapping(value = "profile")
    public String profile() {
        return "admin/profile";
    }

    /**
     * 查看网页
     *
     * @return
     */
    @GetMapping(value = "myblog")
    public String myblog() {
        return "redirect:/";
    }

    /**
     * 保存个人信息
     */
    @PostMapping(value = "/profile")
    @ResponseBody
    public RestResponse saveProfile(@RequestParam String screenName, @RequestParam String email, HttpServletRequest request, HttpSession session) {
        UsersDo users = this.user(request);
        if (StringUtils.isNotBlank(screenName) && StringUtils.isNotBlank(email)) {
            UsersDo temp = new UsersDo();
            temp.setUid(users.getUid());
            temp.setScreenName(screenName);
            temp.setEmail(email);
            userService.updateByUid(temp);
            logService.insertLog(LogActionEnum.UP_INFO.getAction(), GsonUtils.toJsonString(temp), request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UsersDo original = (UsersDo) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setScreenName(screenName);
            original.setEmail(email);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
        } else {
            return RestResponse.fail("请确认信息输入完整");
        }
        return RestResponse.ok();
    }

    @PostMapping(value = "/password")
    @ResponseBody
    public RestResponse password(@RequestParam String oldPassword, @RequestParam String password, HttpServletRequest request, HttpSession session) {
        UsersDo usersDo = this.user(request);
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(password)) {
            return RestResponse.fail("请确认信息输入完整");
        }

        if (!usersDo.getPassword().equals(TaleUtils.MD5encode(usersDo.getUsername() + oldPassword))) {
            return RestResponse.fail("旧密码错误");
        }
        if (password.length() < 6 || password.length() > 14) {
            return RestResponse.fail("请输入6-14位密码");
        }

        try {
            UsersDo temp = new UsersDo();
            temp.setUid(usersDo.getUid());
            String pwd = TaleUtils.MD5encode(usersDo.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateByUid(temp);
            logService.insertLog(LogActionEnum.UP_PWD.getAction(), null, request.getRemoteAddr(), this.getUid(request));

            //更新session中的数据
            UsersDo original = (UsersDo) session.getAttribute(WebConst.LOGIN_SESSION_KEY);
            original.setPassword(pwd);
            session.setAttribute(WebConst.LOGIN_SESSION_KEY, original);
            return RestResponse.ok();
        } catch (Exception e) {
            String msg = "密码修改失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                logger.error(msg, e);
            }
            return RestResponse.fail("密码修改失败,{}" + msg);
        }
    }
}

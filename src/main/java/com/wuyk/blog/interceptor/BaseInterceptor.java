package com.wuyk.blog.interceptor;

import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.pojo.OptionsDo;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.service.IOptionService;
import com.wuyk.blog.service.IUserService;
import com.wuyk.blog.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
    private static final String USER_AGENT = "user-agent";

    @Resource
    private IUserService userService;

    @Resource
    private IOptionService optionService;

    private MapCache cache = MapCache.single();

    @Resource
    private Commons commons;

    @Resource
    private AdminCommons adminCommons;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String contextPath = httpServletRequest.getContextPath();
        String uri = httpServletRequest.getRequestURI();
        logger.info("UserAgent:{}", httpServletRequest.getHeader(USER_AGENT));
        logger.info("用户访问地址：{}，来路地址：{}", uri, IPKit.getIpAddrByRequest(httpServletRequest));

        //请求拦截处理
        UsersDo user = TaleUtils.getLoginUser(httpServletRequest);
        if (user == null) {
            Integer uid = TaleUtils.getCookieUid(httpServletRequest);
            if (uid != null) {
                //存在安全隐患，cookie可以伪造
                user = userService.queryUserById(uid);
                httpServletRequest.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }
        if (uri.startsWith(contextPath + "/admin") && !uri.startsWith(contextPath + "/admin/login") && user == null) {
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/admin/login");
            return false;
        }
        if (httpServletRequest.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            //默认存储30分钟
            cache.hset(TypeEnum.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            httpServletRequest.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        OptionsDo option = optionService.getOptionByName("site_record");
        httpServletRequest.setAttribute("commons", commons);
        httpServletRequest.setAttribute("option", option);
        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

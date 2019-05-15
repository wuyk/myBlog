package com.wuyk.blog.controller.admin;

import com.wuyk.blog.controller.BaseController;
import com.wuyk.blog.exception.TipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 用户后台登录登出
 */
@Controller
@RequestMapping("/admin")
@Transactional(rollbackFor = TipException.class)
public class LoginController extends BaseController {

    private static final Logger loggger = LoggerFactory.getLogger(LoginController.class);

    @GetMapping(value = "/login")
    public String login() {
        return "admin/login";
    }

}

package com.wuyk.blog.service;

import com.wuyk.blog.pojo.UsersDo;

/**
 * Created by wuyk
 */
public interface IUserService {

    /**
     * 通过uid查找对象
     * @param uid
     * @return
     */
    UsersDo queryUserById(Integer uid);
}

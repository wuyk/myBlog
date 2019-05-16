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

    /**
     * 用戶登录
     * @param username
     * @param password
     * @return
     */
    UsersDo login(String username, String password);

    /**
     * 根据主键更新user对象
     * @param usersDo
     * @return
     */
    void updateByUid(UsersDo usersDo);
}

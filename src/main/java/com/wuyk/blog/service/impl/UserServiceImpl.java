package com.wuyk.blog.service.impl;

import com.wuyk.blog.dao.UsersDoMapper;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wuyk
 */
@Service
public class UserServiceImpl implements IUserService {

    @Resource
    private UsersDoMapper usersDoMapper;

    @Override
    public UsersDo queryUserById(Integer uid) {
        UsersDo usersDo = null;
        if (uid != null) {
            usersDo = usersDoMapper.selectByPrimaryKey(uid);
        }
        return usersDo;
    }
}

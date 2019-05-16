package com.wuyk.blog.service.impl;

import com.wuyk.blog.dao.UsersDoMapper;
import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.UsersDo;
import com.wuyk.blog.pojo.vo.UsersVo;
import com.wuyk.blog.service.IUserService;
import com.wuyk.blog.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public UsersDo login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new TipException("用户名和密码不能为空");
        }
        UsersVo usersVo = new UsersVo();
        UsersVo.Criteria criteria = usersVo.createCriteria();
        criteria.andUsernameEqualTo(username);
        long count = usersDoMapper.countByExample(usersVo);
        if (count < 1) {
            throw new TipException("不存在该用户");
        }
        String pwd = TaleUtils.MD5encode(username + password);
        criteria.andPasswordEqualTo(pwd);
        List<UsersDo> usersDoList = usersDoMapper.selectByExample(usersVo);
        if (usersDoList.size() != 1) {
            throw new TipException("用户名或密码错误");
        }
        return usersDoList.get(0);
    }

    @Override
    public void updateByUid(UsersDo usersDo) {
        if (usersDo == null || usersDo.getUid() == null) {
            throw new TipException("用户信息不完整");
        }
        int i = usersDoMapper.updateByPrimaryKeySelective(usersDo);
        if (i != 1) {
            throw new TipException("更新用户信息失败");
        }
    }


}

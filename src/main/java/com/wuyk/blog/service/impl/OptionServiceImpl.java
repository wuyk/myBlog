package com.wuyk.blog.service.impl;

import com.wuyk.blog.dao.OptionsDoMapper;
import com.wuyk.blog.pojo.OptionsDo;
import com.wuyk.blog.service.IOptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wuyk
 */
@Service
public class OptionServiceImpl implements IOptionService {

    @Resource
    private OptionsDoMapper optionsDoMapper;

    @Override
    public OptionsDo getOptionByName(String name) {
        return optionsDoMapper.selectByPrimaryKey(name);
    }
}

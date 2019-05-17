package com.wuyk.blog.service.impl;

import com.wuyk.blog.dao.OptionsDoMapper;
import com.wuyk.blog.pojo.OptionsDo;
import com.wuyk.blog.service.IOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Override
    public List<OptionsDo> getOptions() {
        return optionsDoMapper.selectAll();
    }

    @Override
    @Transactional
    public void saveOptions(Map<String, String> options) {
        if (null != options && !options.isEmpty()) {
            options.forEach(this::insertOption);
        }
    }

    @Transactional
    public void insertOption(String name, String value) {
        OptionsDo optionsDo = new OptionsDo();
        optionsDo.setName(name);
        optionsDo.setValue(value);
        if (optionsDoMapper.selectByPrimaryKey(name) == null) {
            optionsDoMapper.insertSelective(optionsDo);
        } else {
            optionsDoMapper.updateByPrimaryKeySelective(optionsDo);
        }
    }
}

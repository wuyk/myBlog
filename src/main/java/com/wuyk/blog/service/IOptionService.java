package com.wuyk.blog.service;

import com.wuyk.blog.pojo.OptionsDo;

import java.util.List;
import java.util.Map;

/**
 * Created by wuyk
 */
public interface IOptionService {

    OptionsDo getOptionByName(String name);

    List<OptionsDo> getOptions();

    /**
     * 保存一组配置
     *
     * @param options
     */
    void saveOptions(Map<String, String> options);
}

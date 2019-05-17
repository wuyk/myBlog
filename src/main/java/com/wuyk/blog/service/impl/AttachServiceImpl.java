package com.wuyk.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuyk.blog.dao.AttachDoMapper;
import com.wuyk.blog.pojo.AttachDo;
import com.wuyk.blog.pojo.vo.AttachVo;
import com.wuyk.blog.service.IAttachService;
import com.wuyk.blog.utils.DateKit;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class AttachServiceImpl implements IAttachService {

    @Resource
    private AttachDoMapper attachDoMapper;

    @Override
    public PageInfo<AttachDo> getAttachs(Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        AttachVo attachVo = new AttachVo();
        attachVo.setOrderByClause("id desc");
        List<AttachDo> attachDoList = attachDoMapper.selectByExample(attachVo);
        return new PageInfo<>(attachDoList);
    }

    @Override
    public void save(String fname, String fkey, String ftype, Integer author) {
        AttachDo attachDo = new AttachDo();
        attachDo.setFname(fname);
        attachDo.setAuthorId(author);
        attachDo.setFkey(fkey);
        attachDo.setFtype(ftype);
        attachDo.setCreated(DateKit.getCurrentUnixTime());
        attachDoMapper.insertSelective(attachDo);
    }

    @Override
    public AttachDo selectById(Integer id) {
        if(null != id){
            return attachDoMapper.selectByPrimaryKey(id);
        }
        return null;
    }

    @Override
    public void deleteById(Integer id) {
        if (null != id) {
            attachDoMapper.deleteByPrimaryKey( id);
        }
    }
}

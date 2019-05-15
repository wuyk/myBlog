package com.wuyk.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.dao.ContentsDoMapper;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.service.IContentService;
import com.wuyk.blog.pojo.vo.ContentsVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class ContentServiceImpl implements IContentService {

    private static final Logger logger = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Resource
    private ContentsDoMapper contentsDoMapper;

    @Override
    public PageInfo<ContentsDo> getContents(Integer p, Integer limit) {
        logger.debug("Enter getContents method");
        ContentsVo example = new ContentsVo();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(TypeEnum.ARTICLE.getType()).andStatusEqualTo(TypeEnum.PUBLISH.getType());
        PageHelper.startPage(p, limit);
        List<ContentsDo> data = contentsDoMapper.selectByExampleWithBLOBs(example);
        PageInfo<ContentsDo> pageInfo = new PageInfo<>(data);
        logger.debug("Exit getContents method");
        return pageInfo;
    }
}

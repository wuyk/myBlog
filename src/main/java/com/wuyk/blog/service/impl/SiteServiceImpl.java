package com.wuyk.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.dao.AttachDoMapper;
import com.wuyk.blog.dao.CommentsDoMapper;
import com.wuyk.blog.dao.ContentsDoMapper;
import com.wuyk.blog.dao.MetasDoMapper;
import com.wuyk.blog.pojo.CommentsDo;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.bo.StatisticsBo;
import com.wuyk.blog.pojo.vo.AttachVo;
import com.wuyk.blog.pojo.vo.CommentsVo;
import com.wuyk.blog.pojo.vo.ContentsVo;
import com.wuyk.blog.pojo.vo.MetasVo;
import com.wuyk.blog.service.ISiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class SiteServiceImpl implements ISiteService {

    private static final Logger logger = LoggerFactory.getLogger(SiteServiceImpl.class);

    @Resource
    private CommentsDoMapper commentsDoMapper;

    @Resource
    private ContentsDoMapper contentsDoMapper;

    @Resource
    private AttachDoMapper attachDoMapper;

    @Resource
    private MetasDoMapper metasDoMapper;

    @Override
    public List<CommentsDo> recentComments(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        CommentsVo commentsVo = new CommentsVo();
        commentsVo.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        return commentsDoMapper.selectByExampleWithBLOBs(commentsVo);
    }

    @Override
    public List<ContentsDo> recentContents(int limit) {
        if (limit < 0 || limit > 10) {
            limit = 10;
        }
        ContentsVo contentsVo = new ContentsVo();
        contentsVo.createCriteria().andStatusEqualTo(TypeEnum.PUBLISH.getType()).andTypeEqualTo(TypeEnum.ARTICLE.getType());
        contentsVo.setOrderByClause("created desc");
        PageHelper.startPage(1, limit);
        return contentsDoMapper.selectByExampleWithBLOBs(contentsVo);
    }

    @Override
    public StatisticsBo getStatistics() {
        StatisticsBo statistics = new StatisticsBo();

        ContentsVo contentsVo = new ContentsVo();
        contentsVo.createCriteria().andTypeEqualTo(TypeEnum.ARTICLE.getType()).andStatusEqualTo(TypeEnum.PUBLISH.getType());
        Long articles =   contentsDoMapper.countByExample(contentsVo);

        Long comments = commentsDoMapper.countByExample(new CommentsVo());

        Long attachs = attachDoMapper.countByExample(new AttachVo());

        MetasVo metaVoExample = new MetasVo();
        metaVoExample.createCriteria().andTypeEqualTo(TypeEnum.LINK.getType());
        Long links = metasDoMapper.countByExample(metaVoExample);

        statistics.setArticles(articles);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setLinks(links);
        return statistics;
    }
}

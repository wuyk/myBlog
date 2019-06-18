package com.wuyk.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.constant.WebConst;
import com.wuyk.blog.dao.ContentsDoMapper;
import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.vo.ContentsVo;
import com.wuyk.blog.service.IContentService;
import com.wuyk.blog.service.IMetasService;
import com.wuyk.blog.service.IRelationshipsService;
import com.wuyk.blog.utils.DateKit;
import com.wuyk.blog.utils.TaleUtils;
import com.wuyk.blog.utils.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class ContentServiceImpl implements IContentService {

    @Resource
    private ContentsDoMapper contentsDoMapper;

    @Resource
    private IMetasService metasService;

    @Resource
    private IRelationshipsService relationshipsService;

    @Override
    public PageInfo<ContentsDo> getContents(Integer p, Integer limit) {
        ContentsVo example = new ContentsVo();
        example.setOrderByClause("created desc");
        example.createCriteria().andTypeEqualTo(TypeEnum.ARTICLE.getType()).andStatusEqualTo(TypeEnum.PUBLISH.getType());
        PageHelper.startPage(p, limit);
        List<ContentsDo> data = contentsDoMapper.selectByExampleWithBLOBs(example);
        return new PageInfo<>(data);
    }

    @Override
    public PageInfo<ContentsDo> getArticlesWithpage(ContentsVo contentsVo, Integer page, Integer limit) {
        PageHelper.startPage(page, limit);
        List<ContentsDo> contentsDoList = contentsDoMapper.selectByExampleWithBLOBs(contentsVo);
        return new PageInfo<>(contentsDoList);
    }

    @Override
    public ContentsDo getContents(String id) {
        if (StringUtils.isNotBlank(id)) {
            if (Tools.isNumber(id)) {
                return contentsDoMapper.selectByPrimaryKey(Integer.valueOf(id));
            } else {
                ContentsVo contentsVo = new ContentsVo();
                contentsVo.createCriteria().andSlugEqualTo(id);
                List<ContentsDo> contentVos = contentsDoMapper.selectByExampleWithBLOBs(contentsVo);
                if (contentVos.size() != 1) {
                    throw new TipException("查询失败");
                }
                return contentVos.get(0);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public String publish(ContentsDo contentsDo) {
        if (null == contentsDo) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(contentsDo.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(contentsDo.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = contentsDo.getTitle().length();
        if (titleLength > WebConst.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = contentsDo.getContent().length();
        if (contentLength > WebConst.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == contentsDo.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isNotBlank(contentsDo.getSlug())) {
            if (contentsDo.getSlug().length() < 5) {
                return "路径太短了";
            }
            if (!TaleUtils.isPath(contentsDo.getSlug())) return "您输入的路径不合法";
            ContentsVo contentsVo = new ContentsVo();
            contentsVo.createCriteria().andTypeEqualTo(contentsDo.getType()).andSlugEqualTo(contentsDo.getSlug());
            long count = contentsDoMapper.countByExample(contentsVo);
            if (count > 0) return "该路径已经存在，请重新输入";
        } else {
            contentsDo.setSlug(null);
        }

        contentsDo.setContent(EmojiParser.parseToAliases(contentsDo.getContent()));

        int time = DateKit.getCurrentUnixTime();
        contentsDo.setCreated(time);
        contentsDo.setModified(time);
        contentsDo.setHits(0);
        contentsDo.setCommentsNum(0);

        String tags = contentsDo.getTags();
        String categories = contentsDo.getCategories();
        contentsDoMapper.insert(contentsDo);
        Integer cid = contentsDo.getCid();
        metasService.saveMetas(cid, tags, TypeEnum.TAG.getType());
        metasService.saveMetas(cid, categories, TypeEnum.CATEGORY.getType());
        return WebConst.SUCCESS_RESULT;
    }

    @Override
    @Transactional
    public String deleteByCid(Integer cid) {
        ContentsDo contentsDo = this.getContents(cid + "");
        if (null != contentsDo) {
            contentsDoMapper.deleteByPrimaryKey(cid);
            relationshipsService.deleteById(cid, null);
            return WebConst.SUCCESS_RESULT;
        }
        return "数据为空";
    }

    @Override
    @Transactional
    public String updateArticle(ContentsDo contentsDo) {
        if (null == contentsDo) {
            return "文章对象为空";
        }
        if (StringUtils.isBlank(contentsDo.getTitle())) {
            return "文章标题不能为空";
        }
        if (StringUtils.isBlank(contentsDo.getContent())) {
            return "文章内容不能为空";
        }
        int titleLength = contentsDo.getTitle().length();
        if (titleLength > WebConst.MAX_TITLE_COUNT) {
            return "文章标题过长";
        }
        int contentLength = contentsDo.getContent().length();
        if (contentLength > WebConst.MAX_TEXT_COUNT) {
            return "文章内容过长";
        }
        if (null == contentsDo.getAuthorId()) {
            return "请登录后发布文章";
        }
        if (StringUtils.isBlank(contentsDo.getSlug())) {
            contentsDo.setSlug(null);
        }
        int time = DateKit.getCurrentUnixTime();
        contentsDo.setModified(time);
        Integer cid = contentsDo.getCid();
        contentsDo.setContent(EmojiParser.parseToAliases(contentsDo.getContent()));

        contentsDoMapper.updateByPrimaryKeySelective(contentsDo);
        relationshipsService.deleteById(cid, null);
        metasService.saveMetas(cid, contentsDo.getTags(), TypeEnum.TAG.getType());
        metasService.saveMetas(cid, contentsDo.getCategories(), TypeEnum.CATEGORY.getType());
        return WebConst.SUCCESS_RESULT;
    }

    @Override
    public void updateCategory(String ordinal, String newCatefory) {
        ContentsDo contentsDo = new ContentsDo();
        contentsDo.setCategories(newCatefory);
        ContentsVo example = new ContentsVo();
        example.createCriteria().andCategoriesEqualTo(ordinal);
        contentsDoMapper.updateByExampleSelective(contentsDo, example);
    }

    @Override
    public void updateContentByCid(ContentsDo contentsDo) {
        if (null != contentsDo && null != contentsDo.getCid()) {
            contentsDoMapper.updateByPrimaryKeySelective(contentsDo);
        }
    }
}

package com.wuyk.blog.service.impl;

import com.wuyk.blog.constant.TypeEnum;
import com.wuyk.blog.dao.MetasDoMapper;
import com.wuyk.blog.exception.TipException;
import com.wuyk.blog.pojo.ContentsDo;
import com.wuyk.blog.pojo.MetasDo;
import com.wuyk.blog.pojo.RelationshipsDoKey;
import com.wuyk.blog.pojo.vo.MetasVo;
import com.wuyk.blog.service.IContentService;
import com.wuyk.blog.service.IMetasService;
import com.wuyk.blog.service.IRelationshipsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class MetasServiceImpl implements IMetasService {

    @Resource
    private MetasDoMapper metasDoMapper;

    @Resource
    private IContentService contentService;

    @Resource
    private IRelationshipsService relationshipsService;

    @Override
    public List<MetasDo> getMetas(String types) {
        if (StringUtils.isNotBlank(types)) {
            MetasVo metasVo = new MetasVo();
            metasVo.setOrderByClause("sort desc, mid desc");
            metasVo.createCriteria().andTypeEqualTo(types);
            return metasDoMapper.selectByExample(metasVo);
        }
        return null;
    }

    @Override
    @Transactional
    public void saveMeta(String type, String name, Integer mid) {
        if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(name)) {
            MetasVo metasVo = new MetasVo();
            metasVo.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
            List<MetasDo> metaVos = metasDoMapper.selectByExample(metasVo);
            MetasDo metasDo;
            if (metaVos.size() != 0) {
                throw new TipException("已经存在该项");
            } else {
                metasDo = new MetasDo();
                metasDo.setName(name);
                if (null != mid) {
                    MetasDo original = metasDoMapper.selectByPrimaryKey(mid);
                    metasDo.setMid(mid);
                    metasDoMapper.updateByPrimaryKeySelective(metasDo);
                    //更新原有文章的categories
                    contentService.updateCategory(original.getName(), name);
                } else {
                    metasDo.setType(type);
                    metasDoMapper.insertSelective(metasDo);
                }
            }
        }
    }

    @Override
    @Transactional
    public void saveMeta(MetasDo metasDo) {
        if (null != metasDo) {
            metasDoMapper.insertSelective(metasDo);
        }
    }

    @Override
    public void update(MetasDo metasDo) {
        if (null != metasDo && null != metasDo.getMid()) {
            metasDoMapper.updateByPrimaryKeySelective(metasDo);
        }
    }

    @Override
    @Transactional
    public void delete(int mid) {
        MetasDo metasDo = metasDoMapper.selectByPrimaryKey(mid);
        if (null != metasDo) {
            String type = metasDo.getType();
            String name = metasDo.getName();

            metasDoMapper.deleteByPrimaryKey(mid);

            List<RelationshipsDoKey> rlist = relationshipsService.getRelationshipById(null, mid);
            if (null != rlist) {
                for (RelationshipsDoKey r : rlist) {
                    ContentsDo contents = contentService.getContents(String.valueOf(r.getCid()));
                    if (null != contents) {
                        ContentsDo temp = new ContentsDo();
                        temp.setCid(r.getCid());
                        if (type.equals(TypeEnum.CATEGORY.getType())) {
                            temp.setCategories(reMeta(name, contents.getCategories()));
                        }
                        if (type.equals(TypeEnum.TAG.getType())) {
                            temp.setTags(reMeta(name, contents.getTags()));
                        }
                        contentService.updateContentByCid(temp);
                    }
                }
            }
            relationshipsService.deleteById(null, mid);
        }
    }

    @Override
    @Transactional
    public void saveMetas(Integer cid, String names, String type) {
        if (null == cid) {
            throw new TipException("项目关联id不能为空");
        }
        if (StringUtils.isNotBlank(names) && StringUtils.isNotBlank(type)) {
            String[] nameArr = StringUtils.split(names, ",");
            for (String name : nameArr) {
                this.saveOrUpdate(cid, name, type);
            }
        }
    }

    private void saveOrUpdate(Integer cid, String name, String type) {
        MetasVo metasVo = new MetasVo();
        metasVo.createCriteria().andTypeEqualTo(type).andNameEqualTo(name);
        List<MetasDo> metaVos = metasDoMapper.selectByExample(metasVo);

        int mid;
        MetasDo metasDo;
        if (metaVos.size() == 1) {
            metasDo = metaVos.get(0);
            mid = metasDo.getMid();
        } else if (metaVos.size() > 1) {
            throw new TipException("查询到多条数据");
        } else {
            metasDo = new MetasDo();
            metasDo.setSlug(name);
            metasDo.setName(name);
            metasDo.setType(type);
            metasDoMapper.insertSelective(metasDo);
            mid = metasDo.getMid();
        }
        if (mid != 0) {
            Long count = relationshipsService.countById(cid, mid);
            if (count == 0) {
                RelationshipsDoKey relationships = new RelationshipsDoKey();
                relationships.setCid(cid);
                relationships.setMid(mid);
                relationshipsService.insertVo(relationships);
            }
        }
    }

    private String reMeta(String name, String metas) {
        String[] ms = StringUtils.split(metas, ",");
        StringBuilder sbuf = new StringBuilder();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }
}

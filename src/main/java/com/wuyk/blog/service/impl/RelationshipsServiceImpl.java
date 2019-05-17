package com.wuyk.blog.service.impl;

import com.wuyk.blog.dao.RelationshipsDoMapper;
import com.wuyk.blog.pojo.RelationshipsDoKey;
import com.wuyk.blog.pojo.vo.RelationshipsVo;
import com.wuyk.blog.service.IRelationshipsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by wuyk
 */
@Service
public class RelationshipsServiceImpl implements IRelationshipsService {
    @Resource
    private RelationshipsDoMapper relationshipsDoMapper;

    @Override
    public void deleteById(Integer cid, Integer mid) {
        RelationshipsVo relationshipVoExample = new RelationshipsVo();
        RelationshipsVo.Criteria criteria = relationshipVoExample.createCriteria();
        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        relationshipsDoMapper.deleteByExample(relationshipVoExample);
    }

    @Override
    public List<RelationshipsDoKey> getRelationshipById(Integer cid, Integer mid) {
        RelationshipsVo relationshipVoExample = new RelationshipsVo();
        RelationshipsVo.Criteria criteria = relationshipVoExample.createCriteria();
        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        return relationshipsDoMapper.selectByExample(relationshipVoExample);
    }

    @Override
    public void insertVo(RelationshipsDoKey relationshipVoKey) {
        relationshipsDoMapper.insert(relationshipVoKey);
    }

    @Override
    public Long countById(Integer cid, Integer mid) {
        RelationshipsVo relationshipVoExample = new RelationshipsVo();
        RelationshipsVo.Criteria criteria = relationshipVoExample.createCriteria();
        if (cid != null) {
            criteria.andCidEqualTo(cid);
        }
        if (mid != null) {
            criteria.andMidEqualTo(mid);
        }
        return relationshipsDoMapper.countByExample(relationshipVoExample);
    }
}

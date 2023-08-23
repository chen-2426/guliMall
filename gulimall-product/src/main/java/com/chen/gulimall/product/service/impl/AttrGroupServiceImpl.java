package com.chen.gulimall.product.service.impl;

import com.chen.gulimall.product.VO.AttrGroupWithAttrVO;
import com.chen.gulimall.product.VO.AttrVO;
import com.chen.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.chen.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.chen.gulimall.product.entity.AttrEntity;
import com.chen.gulimall.product.service.AttrService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.AttrGroupDao;
import com.chen.gulimall.product.entity.AttrGroupEntity;
import com.chen.gulimall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    private String key;
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>()
                .and(StringUtils.isNotEmpty(key), (qw) -> {
                    qw.eq("attr_group_id", key)
                            .or()
                            .like("attr_group_name", key);
                });
        if (catelogId == 0) {
            return new PageUtils(this.page(new Query<AttrGroupEntity>().getPage(params), wrapper));
        }
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                wrapper.eq("catelog_id", catelogId));
        return new PageUtils(page);
    }

    /**
     * 根据id查询分组及属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrVO>  getWithAttrById(Long catelogId) {
        //查询分组
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId);
        List<AttrGroupEntity> list = this.list(queryWrapper);

        ArrayList<AttrGroupWithAttrVO> res = new ArrayList<>();
        for (AttrGroupEntity attrGroupEntity : list) {
            AttrGroupWithAttrVO attrGroupWithAttrVO = new AttrGroupWithAttrVO();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupWithAttrVO);
            //查询所有属性
            List<AttrEntity> attrEntity = attrService.getAttrRelationship(attrGroupWithAttrVO.getAttrGroupId());
            attrGroupWithAttrVO.setAttrs(attrEntity);
            res.add(attrGroupWithAttrVO );
        }
        return res;
    }



}
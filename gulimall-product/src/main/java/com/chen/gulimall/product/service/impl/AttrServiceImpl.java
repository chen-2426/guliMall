package com.chen.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chen.gulimall.base.utils.ProductConstant;
import com.chen.gulimall.product.VO.AttrRespVO;
import com.chen.gulimall.product.VO.AttrVO;
import com.chen.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.chen.gulimall.product.dao.AttrGroupDao;
import com.chen.gulimall.product.dao.CategoryDao;
import com.chen.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.chen.gulimall.product.entity.AttrGroupEntity;
import com.chen.gulimall.product.entity.CategoryEntity;
import com.chen.gulimall.product.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.AttrDao;
import com.chen.gulimall.product.entity.AttrEntity;
import com.chen.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrDao attrDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public PageUtils queryBaseAttrPage(Long catelogId, Map<String, Object> params, String type) {
        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<>();
        if(catelogId!=0){
            queryWrapper.eq("catelog_id",catelogId)
                    .eq("attr_type","base".equalsIgnoreCase(type)? ProductConstant.ATTR_TYPE.BASE.getCode():ProductConstant.ATTR_TYPE.SALE.getCode());
        }
        String key =(String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            queryWrapper.and((qw)->{
                qw.like("attr_name",key).or().like("attr_id",key);
            });
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                queryWrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrEntity> records = page.getRecords();
        List<AttrRespVO> respVOS = records.stream().map((record) -> {
            AttrRespVO attrRespVO = new AttrRespVO();
            BeanUtils.copyProperties(record, attrRespVO);
            if("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attr_id = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", record.getAttrId()));
                if (attr_id != null) {
                    Long attrGroupId = attr_id.getAttrGroupId();
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrGroupId);
                    attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
            CategoryEntity categoryEntity = categoryDao.selectById(record.getCatelogId());
            if (categoryEntity != null) {
                attrRespVO.setCatelogName(categoryEntity.getName());
            }
            return attrRespVO;
        }).collect(Collectors.toList());
        pageUtils.setList(respVOS);
        return pageUtils;
    }

    @Override
    public AttrRespVO getAttrInfo(Long attrId) {
        AttrRespVO attrRespVO = new AttrRespVO();
        AttrEntity byId = this.getById(attrId);
        BeanUtils.copyProperties(byId,attrRespVO);
        if(byId.getAttrType()==ProductConstant.ATTR_TYPE.BASE.getCode()){
        AttrAttrgroupRelationEntity attr_id = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", byId.getAttrId()));
        if(attr_id!=null){
            attrRespVO.setAttrGroupId(attr_id.getAttrGroupId());
            AttrGroupEntity attrGroupEntity = attrGroupDao.selectOne(new QueryWrapper<AttrGroupEntity>().eq("attr_group_id", attr_id.getAttrGroupId()));
            if(attrGroupEntity!=null){
                attrRespVO.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        }

        attrRespVO.setCatelogPath(categoryService.findCatelogPath(byId.getCatelogId()));
        CategoryEntity categoryEntity = categoryDao.selectById(byId.getCatelogId());
        if(categoryEntity!=null){
            attrRespVO.setCatelogName(categoryEntity.getName());
        }
        return attrRespVO;
    }

    @Override
    public void updateAttr(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        if(attr.getAttrType()==ProductConstant.ATTR_TYPE.BASE.getCode()){
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
        attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
        Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        if(count>0){
            attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity,new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
        }else {
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }
        }
    }


    @Override
    @Transactional
    public void saveAttr(AttrVO attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
//      保存基本数据
        this.save(attrEntity);
        if(attr.getAttrType()==ProductConstant.ATTR_TYPE.BASE.getCode()&& attr.getAttrGroupId()!=null){
            //        保存关联关系
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attr.getAttrId());
            attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
        }

    }

    @Override
    public AttrVO getAttrVoById(Long attrId) {
        AttrEntity attrEntity = this.getById(attrId);
        AttrVO attrVO = new AttrVO();
        BeanUtils.copyProperties(attrEntity,attrVO);
        return attrVO;
    }

    /**
     * 根据分组id查找所有关联的属性
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getAttrRelationship(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities = attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));

        List<Long> collect = attrAttrgroupRelationEntities.stream().map(
                entity -> entity.getAttrId()

        ).collect(Collectors.toList());
        if(collect.size()==0){
            return null;
        }
        List<AttrEntity> attrEntities = this.listByIds(collect);
        return attrEntities;

    }

    /**
     * 获取未关联的分组属性
     * @param params
     * @param attrgroupId
     * @return
     */
    @Override
    public PageUtils getNoAttrRelationship(Map<String, Object> params, Long attrgroupId) {
        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrgroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        //查询同类别组
        List<AttrGroupEntity> attrGroupEntities = attrGroupDao.selectList(
                new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<Long> attrGroupIds = attrGroupEntities.stream()
                .map(entity -> entity.getAttrGroupId())
                .collect(Collectors.toList());
        //查询已关联组的属性
        List<AttrAttrgroupRelationEntity> relationEntities = attrAttrgroupRelationDao.selectList(
                new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .in("attr_group_id", attrGroupIds));
        List<Long> attrIds = relationEntities.stream()
                .map((entity) -> entity.getAttrId())
                .collect(Collectors.toList());

        QueryWrapper<AttrEntity> queryWrapper = new QueryWrapper<AttrEntity>()
                .eq("catelog_id",catelogId)
                .eq("attr_type",ProductConstant.ATTR_TYPE.BASE.getCode());
        if(attrIds!=null&&attrIds.size()>0){
            queryWrapper.notIn("attr_id",attrIds);
        }
        //模糊查询
        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            queryWrapper.like("attr_name",key).or().like("attr_id",key);
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), queryWrapper);
        return new PageUtils(page);
    }

    @Override
    public List<Long> selectSearchAttrIds(List<Long> attrIds) {
        List<AttrEntity> list = this.list(new QueryWrapper<AttrEntity>().in("attr_id", attrIds).and(qw ->{
            qw.eq("search_type",1); }
        ));
        List<Long> collect = list.stream().map(entity -> entity.getAttrId()).collect(Collectors.toList());
        return collect;
    }


}
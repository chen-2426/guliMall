package com.chen.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chen.gulimall.product.dao.BrandDao;
import com.chen.gulimall.product.dao.CategoryDao;
import com.chen.gulimall.product.entity.BrandEntity;
import com.chen.gulimall.product.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.CategoryBrandRelationDao;
import com.chen.gulimall.product.entity.CategoryBrandRelationEntity;
import com.chen.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    BrandDao brandDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        CategoryEntity categoryEntity = categoryDao.selectById(brandId);
        BrandEntity brandEntity = brandDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updateBrandName(Long brandId, String name) {
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(brandId);
        entity.setBrandName(name);
        update(entity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity entity = new CategoryBrandRelationEntity();
        entity.setBrandId(catId);
        entity.setBrandName(name);
        update(entity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catId));
    }

    @Override
    public List<BrandEntity> getBrandsByCatId(long catId) {
        List<CategoryBrandRelationEntity> relationEntity = this.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> collect = null;
        if(relationEntity!=null&&relationEntity.size()>0){
            collect = relationEntity.stream().map(entity ->
                    brandDao.selectById(entity.getBrandId())
            ).collect(Collectors.toList());

        }
        return collect;
    }


}
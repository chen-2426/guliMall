package com.chen.gulimall.product.service.impl;

import com.chen.gulimall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.BrandDao;
import com.chen.gulimall.product.entity.BrandEntity;
import com.chen.gulimall.product.service.BrandService;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    BrandDao brandDao;
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<BrandEntity> queryWrapper = new QueryWrapper<>();
        if( StringUtils.isNotEmpty(key)){
            queryWrapper.like("brand_id",key)
                    .or()
                    .like("name",key);
        }
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),queryWrapper

        );

        return new PageUtils(page);
    }

    @Override
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);
        if(StringUtils.isNotEmpty(brand.getName())){
            categoryBrandRelationService.updateBrandName(brand.getBrandId(),brand.getName());
            //todo 更新品牌相关关联
        }
    }

}
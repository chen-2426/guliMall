package com.chen.gulimall.product.service.impl;

import com.chen.gulimall.product.VO.Web.SkuItemSaleAttrVo;
import com.chen.gulimall.product.entity.SkuInfoEntity;
import com.chen.gulimall.product.service.SkuInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.SkuSaleAttrValueDao;
import com.chen.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.chen.gulimall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuItemSaleAttrVo> getSaleAttrsBySpuId(Long spuId) {
        List<SkuItemSaleAttrVo> res= skuSaleAttrValueDao.getSkuItemSaleAttrVoBySpuId(spuId);
        return res;
    }

    @Override
    public List<String> getStringListById(Long id) {
        return skuSaleAttrValueDao.getSkuItemSaleAttrValuesAsStringList(id);
    }

}
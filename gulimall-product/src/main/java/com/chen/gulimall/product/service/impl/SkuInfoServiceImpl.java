package com.chen.gulimall.product.service.impl;

import com.chen.gulimall.product.VO.Web.SkuItemSaleAttrVo;
import com.chen.gulimall.product.VO.Web.SkuItemVO;
import com.chen.gulimall.product.VO.Web.SpuItemAttrGroupVo;
import com.chen.gulimall.product.entity.SkuImagesEntity;
import com.chen.gulimall.product.entity.SpuInfoDescEntity;
import com.chen.gulimall.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.SkuInfoDao;
import com.chen.gulimall.product.entity.SkuInfoEntity;
import rx.internal.schedulers.CachedThreadScheduler;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            wrapper.and(w ->w.like("sku_name",key).or().like("sku_id",key));
        }

        String catelogId = (String) params.get("catelogId");
        if(StringUtils.isNotEmpty(catelogId)&&!"0".equalsIgnoreCase(catelogId)){
            wrapper.eq("catalog_id",catelogId);
        }

        String brandId = (String) params.get("brandId");
        if(StringUtils.isNotEmpty(brandId)&&!"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brandId",brandId);
        }

        String min = (String) params.get("min");
        if(StringUtils.isNotEmpty(min)&&!"0".equalsIgnoreCase(min)){
            wrapper.ge("price",min);
        }

        String max = (String) params.get("max");
        if(StringUtils.isNotEmpty(min)&&!"0".equalsIgnoreCase(max)){
            wrapper.le("price",max);
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkusBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

    @Override
    public SkuItemVO item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVO skuItemVO = new SkuItemVO();
        //基本信息
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity info = getById(skuId);
            skuItemVO.setInfo(info);
            return info;
        }, executor);

        //图片
        CompletableFuture imagesFuture =  CompletableFuture.runAsync(()-> {
            List<SkuImagesEntity> images = skuImagesService.getBySkuId(skuId);
            skuItemVO.setImages(images);
        }, executor);

        //spu得销售属性组合
        CompletableFuture<Void> saleAttrVosFuture = infoFuture.thenAcceptAsync(res -> {
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrsBySpuId(res.getSpuId());
            skuItemVO.setSaleAttr(saleAttrVos);
        });
        //spu的介绍
        CompletableFuture<Void> SpuinfoFuture = infoFuture.thenAcceptAsync(res -> {
            SpuInfoDescEntity Spuinfo = spuInfoDescService.getById(res.getSpuId());
            skuItemVO.setDesc(Spuinfo);
        });

        //spu的规格参数
        CompletableFuture<Void> attrsFuture = infoFuture.thenAcceptAsync(res -> {
            List<SpuItemAttrGroupVo> attrs = attrGroupService.getWithAttrBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVO.setGroupAttrs(attrs);
        });

        CompletableFuture.allOf(imagesFuture,saleAttrVosFuture,SpuinfoFuture,attrsFuture).get();

        return skuItemVO;
    }

}
package com.chen.gulimall.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.chen.gulimall.base.TO.SkuReductionTo;
import com.chen.gulimall.base.TO.SpuBoundsTo;
import com.chen.gulimall.base.TO.es.SkuEsModel;
import com.chen.gulimall.base.Constant.ProductConstant;
import com.chen.gulimall.base.utils.R;
import com.chen.gulimall.product.VO.*;
import com.chen.gulimall.product.entity.*;
import com.chen.gulimall.product.feign.CouponFeignService;
import com.chen.gulimall.product.feign.SearchFeignService;
import com.chen.gulimall.product.feign.WareFeignService;
import com.chen.gulimall.product.service.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.base.utils.Query;

import com.chen.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SpuInfoDescService spuInfoDescService;
    @Autowired
    SpuImagesService spuImagesService;
    @Autowired
    AttrService attrService;
    @Autowired
    ProductAttrValueService attrValueService;
    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImagesService skuImagesService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    CouponFeignService couponFeignService;
    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    WareFeignService wareFeignService;
    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * //TODO 高级部分优化异常处理
     *
     * @param spuInfo
     */
    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVO spuInfo) {
        //保存基本信息
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.save(spuInfoEntity);
        //保存图片信息
        List<String> decript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",", decript));
        spuInfoDescService.save(spuInfoDescEntity);
        //保存图片集
        List<String> images = spuInfo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);
        //保存规格参数
        List<BaseAttrs> baseAttrs = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(attr.getAttrId());
            AttrEntity id = attrService.getById(attr.getAttrId());
            productAttrValueEntity.setAttrName(id.getAttrName());
            productAttrValueEntity.setAttrValue(attr.getAttrValues());
            productAttrValueEntity.setQuickShow(attr.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveBatch(collect);
        //保存积分信息 跨库
        Bounds bounds = spuInfo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds, spuBoundsTo);
        spuBoundsTo.setSpuId(spuInfoEntity.getId());
        R r1 = couponFeignService.saveSpuBounds(spuBoundsTo);
        if ((int) r1.get("code") != 0) {
            log.error("couponFeignService保存Bound失败");
        }
        //保存当前spu对应的所有sku信息;
        //==========sku基本信息===============
        List<Skus> skus = spuInfo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                //===========sku的图片信息=============
                String defaultImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaultImg = image.getImgUrl();
                    }
                }
                //===========sku的图片信息=============
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.save(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();
                //===========sku的图片信息=============
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter(entity -> {
                    //true 为需要，false 为不需要
                    return StringUtils.isNotEmpty(entity.getImgUrl());
                }).collect(Collectors.toList());
                //没有图片，不保存路径
                skuImagesService.saveBatch(imagesEntities);


                //===========sku的图片信息=============
                //sku的销售属性
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                // sku的优惠信息 （跨库）
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1) {
                    R r = couponFeignService.saveSkuReduction(skuReductionTo);
                    if ((int) r.get("code") != 0) {
                        log.error("couponFeignService保存Sku失败");
                    }
                }

            });
        }

    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (StringUtils.isNotEmpty(key)) {
            queryWrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        String status = (String) params.get("status");
        if (StringUtils.isNotEmpty(status)) {
            queryWrapper.eq("publish_status", status);
        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.isNotEmpty(brandId) & !"0".equalsIgnoreCase(brandId)) {
            queryWrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.isNotEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("catalog_id", catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 商品上架
     *
     * @param spuId
     */
    @Override
    public void up(Long spuId) {

        List<SkuInfoEntity> skuInfoEntities = skuInfoService.getSkusBySpuId(spuId);
        List<Long> skuids = skuInfoEntities.stream().map(entity -> entity.getSkuId()).collect(Collectors.toList());
        //todo  品牌名字信息  一个spu一个品牌，所以可共用
        SpuInfoEntity spu = this.getById(spuId);
        Long catalogId = spu.getCatalogId();
        CategoryEntity category = categoryService.getById(catalogId);
        Long brandId = spu.getBrandId();
        BrandEntity brand = brandService.getById(brandId);

        //todo  查询sku可以用来被检索的规格属性 可共用
        List<ProductAttrValueEntity> entities = attrValueService.baseAttrlistforspu(spuId);
        List<Long> attrids = entities.stream().map(entity -> entity.getAttrId()).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrids);
        List<SkuEsModel.Attrs> attrs = entities.stream().filter(entity ->
                searchAttrIds.contains(entity.getAttrId())
        ).map(entity -> {
            SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(entity, attr);
            return attr;
        }).collect(Collectors.toList());
        Map<Long, Long> map = null;
        try {
            //todo  查库存
            List<HasStockVo> data = wareFeignService.HasStock(skuids).getData(new TypeReference<List<HasStockVo>>(){});
            map = data.stream().collect(Collectors.toMap(HasStockVo::getSkuId, HasStockVo::getStocknums));
        } catch (Exception e) {
            log.error("库存服务异常");
        }
        //封装每个sku信息作为es对象
        Map<Long, Long> finalMap = map;
        List<SkuEsModel> collect = skuInfoEntities.stream().map(sku -> {
                    SkuEsModel esModel = new SkuEsModel();
                    BeanUtils.copyProperties(sku, esModel);
                    //todo 非同名信息
                    esModel.setSkuPrice(sku.getPrice());
                    esModel.setSkuImg(sku.getSkuDefaultImg());
                    //todo  查库存
                    esModel.setHasStock(finalMap ==null?true: finalMap.get(sku.getSkuId())==null?false:finalMap.get(sku.getSkuId())>0);
                    //todo  品牌,类型名字信息
                    esModel.setBrandName(brand.getName());
                    esModel.setBrandImg(brand.getLogo());
                    esModel.setCatalogName(category.getName());
                    //todo  热度评分
                    esModel.setHotScore(0L);
                    //todo 属性配置
                    esModel.setAttrs(attrs);
                    return esModel;
                }
        ).collect(Collectors.toList());
        //发送给es
        R r = searchFeignService.productStatusUp(collect);
        if((int)r.get("code") == 0){
            this.updateSpuStatus(spuId, ProductConstant.SPU_STATUS.SPU_UP);

        }else{
            //todo 调用失败，重试机制
        }

    }

    @Override
    public void updateSpuStatus(Long spuId, int spuUp) {
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setId(spuId);
        spuInfoEntity.setPublishStatus(spuUp);
        spuInfoEntity.setUpdateTime(new Date());
        this.updateById(spuInfoEntity);
    }

}
package com.chen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.product.VO.CategoryVO;
import com.chen.gulimall.product.VO.Web.l2CategoryVO;
import com.chen.gulimall.product.entity.CategoryEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:05
 */
public interface CategoryService extends IService<CategoryEntity> {
    boolean removeMenuByIds(Collection<? extends Serializable> idList);

    PageUtils queryPage(Map<String, Object> params);
    List<CategoryVO> listWithTree();

    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Categorys();

    Map<String, List<l2CategoryVO>> getCatalogJson();
}


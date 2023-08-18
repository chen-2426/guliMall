package com.chen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.product.DTO.CategoryDTO;
import com.chen.gulimall.product.entity.CategoryEntity;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Arrays;
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
    List<CategoryDTO> listWithTree();
}


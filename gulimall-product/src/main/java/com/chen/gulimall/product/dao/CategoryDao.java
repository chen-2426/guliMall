package com.chen.gulimall.product.dao;

import com.chen.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:05
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}

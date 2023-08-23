package com.chen.gulimall.product.dao;

import com.chen.gulimall.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 品牌
 * 
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
	@Select("select * from pms_brand")
	List<BrandEntity> testList();
}

package com.chen.gulimall.order.dao;

import com.chen.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author chen
 * @email 
 * @date 2023-08-14 16:11:45
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}

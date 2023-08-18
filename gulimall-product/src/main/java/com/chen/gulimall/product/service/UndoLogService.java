package com.chen.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.product.entity.UndoLogEntity;

import java.util.Map;

/**
 * 
 *
 * @author chen
 * @email 
 * @date 2023-08-13 23:35:04
 */
public interface UndoLogService extends IService<UndoLogEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


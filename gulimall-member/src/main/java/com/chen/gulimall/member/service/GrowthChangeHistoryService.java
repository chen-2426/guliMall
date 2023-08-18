package com.chen.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.gulimall.base.utils.PageUtils;
import com.chen.gulimall.member.entity.GrowthChangeHistoryEntity;

import java.util.Map;

/**
 * 成长值变化历史记录
 *
 * @author chen
 * @email 
 * @date 2023-08-14 16:04:19
 */
public interface GrowthChangeHistoryService extends IService<GrowthChangeHistoryEntity> {

    PageUtils queryPage(Map<String, Object> params);
}


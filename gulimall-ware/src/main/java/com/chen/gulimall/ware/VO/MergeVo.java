package com.chen.gulimall.ware.VO;

import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/25 16:10
 * @description
 */
@Data
public class MergeVo {
    private Long purchaseId;
    private List<Long> items;
}

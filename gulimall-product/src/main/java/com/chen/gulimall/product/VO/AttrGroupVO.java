package com.chen.gulimall.product.VO;

import lombok.Data;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/23 15:18
 * @description
 */
@Data
public class AttrGroupVO {
    private static final long serialVersionUID = 1L;

    /**
     * 分组id
     */
    private Long attrGroupId;
    /**
     * 组名
     */
    private String attrGroupName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String descript;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long catelogId;
    /**
     * catelogId全路径
     */
    private Long[] catelogPath;
}

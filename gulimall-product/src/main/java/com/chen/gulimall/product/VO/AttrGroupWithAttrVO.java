package com.chen.gulimall.product.VO;

import com.chen.gulimall.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/22 20:01
 * @description
 */
@Data
public class AttrGroupWithAttrVO {
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
     *     属性组
     */
    private List<AttrEntity> attrs;
}

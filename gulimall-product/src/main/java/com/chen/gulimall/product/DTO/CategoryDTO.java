package com.chen.gulimall.product.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.chen.gulimall.product.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/17 14:03
 * @description
 */
@Data
public class CategoryDTO  extends CategoryEntity {
    private List<CategoryDTO> children;

}

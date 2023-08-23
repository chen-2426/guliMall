package com.chen.gulimall.product.VO;

import lombok.Data;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/23 15:16
 * @description
 */
@Data
public class AttrRespVO extends AttrVO {
    public String catelogName;
    public String groupName;
    public Long[] catelogPath;
}

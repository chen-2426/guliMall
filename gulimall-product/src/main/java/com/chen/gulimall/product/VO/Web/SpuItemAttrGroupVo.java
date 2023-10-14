package com.chen.gulimall.product.VO.Web;

import com.chen.gulimall.product.VO.Attr;
import lombok.Data;

import java.util.List;

@Data
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrs;
}

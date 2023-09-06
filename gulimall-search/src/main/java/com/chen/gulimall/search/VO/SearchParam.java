package com.chen.gulimall.search.VO;

import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/4 17:12
 * @description
 */

@Data
public class SearchParam {
    private String keyword; //匹配关键字
    private Long catalog3Id; //三级分类Id;
    /**
     *  saleCount_asc/desc
     *  skuPrice_asc/desc
     *  hotScore_asc/desc
     */
    private String sort; //排序条件
    //过滤条件
    private Integer hasStock; //是否有货
    private String  skuPrice; //价格区间查询
    private List<Long> brandId; //按照品牌进行查询，可以多选
    private  List<String> attrs;//按照属性进行筛选
    private Integer pageNum;//页码
}

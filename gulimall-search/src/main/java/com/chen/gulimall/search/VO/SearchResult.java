package com.chen.gulimall.search.VO;

import com.chen.gulimall.base.TO.es.SkuEsModel;
import lombok.Data;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/4 19:36
 * @description
 */
@Data
public class SearchResult {
    private List<SkuEsModel> products;
    private Integer pageNum;
    private Long total;
    private List<BrandVO> brands;
    private List<AttrVO> attrs;
    private List<CatalogVO> catalogs;
    private List<NavVo> navs;

    @Data
    public static class NavVo {
        private String navName;
        private String navValue;
        private String link;
    }
    @Data
    public static class BrandVO{
        private Long brandId;
        private String brandName;
        private String brandImg;
    }
    @Data
    public static class AttrVO{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }

    @Data
    public static class CatalogVO{
        private Long catalogId;
        private String catalogName;
    }
}

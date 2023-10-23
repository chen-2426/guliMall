package com.chen.gulimall.search.service.impl;


import com.alibaba.fastjson.JSON;
import com.chen.gulimall.base.TO.es.SkuEsModel;
import com.chen.gulimall.search.VO.SearchParam;
import com.chen.gulimall.search.VO.SearchResult;
import com.chen.gulimall.search.config.GulimallElasticSearchConfig;
import com.chen.gulimall.search.constant.Esconstant;
import com.chen.gulimall.search.service.MallSearchService;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.elasticsearch.search.sort.SortOrder;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/4 18:37
 * @description
 */
@EnableScheduling  //开启定时任务 对应定时任务 @Scheduled
@EnableAsync //开启异步功能 对应异步执行方法 @Async
@Service
public class MallSearchServiceImpl implements MallSearchService {
    @Autowired
    private RestHighLevelClient client;

    @Override
    public SearchResult search(SearchParam searchParam) {
        SearchResult result = null;

        SearchRequest searchRequest = buildSearchRequest(searchParam);

        try {
            SearchResponse search = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            result = buildSearchResult(search,searchParam);
            //结果封装
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //    构建结果
    private SearchResult buildSearchResult(SearchResponse search,SearchParam searchParam) {

        SearchResult result = new SearchResult();
        SearchHits hits = search.getHits();
        SearchHit[] searchHits = hits.getHits();
        ArrayList<SkuEsModel> skuEsModels = new ArrayList<>();
//        List<SearchResult.NavVo> navVos = new ArrayList<>();
        if(searchHits!=null&&searchHits.length>0){
            for (SearchHit searchHit : searchHits) {
                String sourceAsString = searchHit.getSourceAsString();
                SkuEsModel esModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                if(StringUtils.isNotEmpty(searchParam.getKeyword())){
                    HighlightField skuTitle = searchHit.getHighlightFields().get("skuTitle");;
                    String string = skuTitle.getFragments()[0].string();
                    esModel.setSkuTitle(string);
                }
                skuEsModels.add(esModel);
            }
        }
        result.setProducts(skuEsModels);

        ParsedNested attr_agg = search.getAggregations().get("attr_agg");
        ArrayList<SearchResult.AttrVO> attrVOS = new ArrayList<>();
        ParsedLongTerms attr_id_agg = attr_agg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVO attrVO = new SearchResult.AttrVO();
            attrVO.setAttrId(Long.parseLong(bucket.getKeyAsString()));
            ParsedStringTerms attr_name_agg = bucket.getAggregations().get("attr_name_agg");
            attrVO.setAttrName(attr_name_agg.getBuckets().get(0).getKeyAsString());
            ParsedStringTerms attr_value_agg = bucket.getAggregations().get("attr_value_agg");
            List<String> attrValues = attr_value_agg.getBuckets().stream().map(attr_value_aggBucket ->
                    attr_value_aggBucket.getKeyAsString()
            ).collect(Collectors.toList());
            attrVO.setAttrValue(attrValues);
            attrVOS.add(attrVO);
        }
        result.setAttrs(attrVOS);


        ArrayList<SearchResult.BrandVO> brandVOS = new ArrayList<>();
        ParsedLongTerms brand_agg = search.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVO brandVO = new SearchResult.BrandVO();
            brandVO.setBrandId(Long.parseLong(bucket.getKeyAsString()));
            ParsedLongTerms brand_img_agg = bucket.getAggregations().get("brand_img_agg");
            brandVO.setBrandName(brand_img_agg.getBuckets().get(0).getKeyAsString());
            ParsedLongTerms brand_name_agg = bucket.getAggregations().get("brand_name_agg");
            brandVO.setBrandImg(brand_name_agg.getBuckets().get(0).getKeyAsString());
        }
        result.setBrands(brandVOS);


        ArrayList<SearchResult.CatalogVO> catalogVOS = new ArrayList<>();
        ParsedLongTerms catalog_agg =  search.getAggregations().get("catalog_agg");
        List<? extends Terms.Bucket> catalogAggBuckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : catalogAggBuckets) {
            SearchResult.CatalogVO catalogVO = new SearchResult.CatalogVO();
            catalogVO.setCatalogId(Long.parseLong(bucket.getKeyAsString()));
            ParsedLongTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            catalogVO.setCatalogName(catalog_name_agg.getBuckets().get(0).getKeyAsString());
            catalogVOS.add(catalogVO);
        }
        result.setCatalogs(catalogVOS);


        long total = hits.getTotalHits().value;
        long TotalPageNum = total / Esconstant.PRODUCT_PAGE_SIZE;
        TotalPageNum+= total%Esconstant.PRODUCT_PAGE_SIZE>0?1:0;
        result.setPageNum(searchParam.getPageNum());
        result.setTotal(TotalPageNum);

//
//        result.setNavs(navVos);

        return result;
    }

    // 创建请求
    private SearchRequest buildSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //查询

        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", searchParam.getKeyword()));
        }
        if (searchParam.getCatalog3Id()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", searchParam.getCatalog3Id()));
        }
        if (searchParam.getBrandId()!=null&&searchParam.getBrandId().size()>0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId",searchParam.getBrandId()));
        }
        if(searchParam.getHasStock()!=null){
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock",searchParam.getHasStock()==1));
        }


        if(searchParam.getAttrs()!=null&&searchParam.getAttrs().size()>0){
            List<String> attrs = searchParam.getAttrs();
            for (String attr : attrs) {
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
                String[] s = attr.split("_");
                String Id = s[0];
                String[] value = s[1].split(":");
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId",Id));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue",value));
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQuery);
            }
        }


        if(searchParam.getSkuPrice()!=null){
            String skuPrice = searchParam.getSkuPrice();
            String[] split = skuPrice.split("-");
            RangeQueryBuilder skuPrice1 = QueryBuilders.rangeQuery("skuPrice");
            if(split.length==2){
                skuPrice1.lte(split[0])
                        .gte(split[1]);
            }else{
                if(skuPrice.startsWith("_")){
                    skuPrice1.lte(split[0]);
                }else {
                    skuPrice1.gte(split[0]);
                }
            }
            boolQueryBuilder.filter(skuPrice1);

        }
        searchSourceBuilder.query(boolQueryBuilder);

//       排序，高亮、分页
//        排序
        if(StringUtils.isNotEmpty(searchParam.getSort())){
            String sort = searchParam.getSort();
            String[] s = sort.split("_");
            SortOrder order = s[1].equalsIgnoreCase("asc")?SortOrder.ASC: SortOrder.DESC;
            searchSourceBuilder.sort(s[0],order);
        }
//        分页
        searchSourceBuilder.from((searchParam.getPageNum()-1)*Esconstant.PRODUCT_PAGE_SIZE);
        searchSourceBuilder.size(Esconstant.PRODUCT_PAGE_SIZE);
//      高亮
        if(StringUtils.isNotEmpty(searchParam.getKeyword())){
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            searchSourceBuilder.highlighter(highlightBuilder);
        }
//        聚合
        TermsAggregationBuilder brand_agg = AggregationBuilders.terms("brand_agg");
        brand_agg.field("brandId").size(50);
        brand_agg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName"));
        brand_agg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg"));
        searchSourceBuilder.aggregation(brand_agg);

        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalogId");
        TermsAggregationBuilder catalogNameAgg = AggregationBuilders.terms("catalog_name_agg").field("catalogName");
        catalogAgg.subAggregation(catalogNameAgg);
        searchSourceBuilder.aggregation(catalogAgg);

        NestedAggregationBuilder nestedAggregationBuilder = new NestedAggregationBuilder("attrs", "attrs");
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attr_name_agg").field("attrs.attrName");
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue");
        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);
        nestedAggregationBuilder.subAggregation(attrIdAgg);
        searchSourceBuilder.aggregation(nestedAggregationBuilder);

        SearchRequest request = new SearchRequest(new String[]{Esconstant.PRODUCT_INDEX}, searchSourceBuilder);
        return request;
    }
}

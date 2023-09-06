package com.chen.gulimall.search.service;

import com.chen.gulimall.search.VO.SearchParam;
import com.chen.gulimall.search.VO.SearchResult;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/4 18:36
 * @description
 */
public interface MallSearchService {
    SearchResult search(SearchParam searchParam);
}

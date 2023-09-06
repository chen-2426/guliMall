package com.chen.gulimall.search.controller;

import com.chen.gulimall.search.VO.SearchParam;
import com.chen.gulimall.search.VO.SearchResult;
import com.chen.gulimall.search.service.MallSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/9/4 16:43
 * @description
 */
public class SearchController{
    @Autowired
    MallSearchService mallSearchService;

    /**
     * 封装请求用的参数
     * @param searchParam
     * @return
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam searchParam, Model model){
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result",model);
        return "list";
    }
}

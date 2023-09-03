package com.chen.gulimall.product.VO.Web;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/8/17 14:03
 * @description
 */
@Data
public class l2CategoryVO {
    private String l1CategoryId;
    private List<l3CategoryVO> l3CategoryList;
    private String id;
    private String name;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class l3CategoryVO{
        private String l2CategoryId;
        private String id;
        private String name;
    }
}


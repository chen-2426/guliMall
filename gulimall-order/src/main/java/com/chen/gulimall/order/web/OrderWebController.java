package com.chen.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author chenxi
 * @version 1.0
 * @date 2023/10/16 16:15
 * @description
 */
@Controller
public class OrderWebController {
    @GetMapping("/toTrade")
    public String toTrade(){
        return "confirm";
    }
}
